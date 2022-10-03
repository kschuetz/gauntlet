package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.VectorBuilder;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static software.kes.gauntlet.Abnormal.error;
import static software.kes.gauntlet.Abnormal.interrupted;
import static software.kes.gauntlet.Abnormal.timedOut;
import static software.kes.gauntlet.Counterexample.counterexample;
import static software.kes.gauntlet.ExistentialTestResult.proved;
import static software.kes.gauntlet.ExistentialTestResult.unproved;
import static software.kes.gauntlet.UniversalTestResult.falsified;
import static software.kes.gauntlet.UniversalTestResult.unfalsified;

abstract class ResultCollector<A> implements ResultReceiver {
    protected final ImmutableVector<A> samples;
    protected final ReentrantLock lock;
    protected final CheckList reported;
    private final Condition doneCondition;
    protected volatile int cutoffIndex;
    private volatile boolean done;

    public ResultCollector(ImmutableVector<A> samples) {
        int sampleCount = samples.size();
        this.samples = samples;
        this.cutoffIndex = sampleCount;
        this.lock = new ReentrantLock();
        this.doneCondition = lock.newCondition();
        this.done = false;
        this.reported = new CheckList(sampleCount);
    }

    static <A> UniversalResultCollector<A> universalResultCollector(ImmutableVector<A> samples) {
        return new UniversalResultCollector<>(samples);
    }

    static <A> ExistentialResultCollector<A> existentialResultCollector(ImmutableVector<A> samples) {
        return new ExistentialResultCollector<>(samples);
    }

    @Override
    public boolean shouldRun(int sampleIndex) {
        lock.lock();
        try {
            return sampleIndex < cutoffIndex;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void reportResult(int sampleIndex, Either<Throwable, EvalResult> result) {
        lock.lock();
        try {
            if (sampleIndex >= cutoffIndex) {
                return;
            }

            reported.mark(sampleIndex);

            result.match(error -> {
                        handleError(sampleIndex, error);
                        return UNIT;
                    },
                    evalResult ->
                            evalResult.match(__ -> {
                                        handleSuccess(sampleIndex);
                                        return UNIT;
                                    },
                                    failure -> {
                                        handleFailure(sampleIndex, failure);
                                        return UNIT;
                                    }));

            checkIfDone();
        } finally {
            lock.unlock();
        }

    }

    protected void checkIfDone() {
        lock.lock();
        try {
            if (reported.firstUnmarkedIndex() >= cutoffIndex) {
                this.done = true;
                doneCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    protected abstract void handleSuccess(int sampleIndex);

    protected abstract void handleFailure(int sampleIndex, EvalFailure failure);

    protected abstract void handleError(int sampleIndex, Throwable error);

    protected final boolean await(Duration timeout) throws InterruptedException {
        lock.lock();
        try {
            if (done) {
                return true;
            } else {
                long timeoutMillis = timeout.toMillis();
                Date deadline = new Date(System.currentTimeMillis() + timeoutMillis);
                boolean finishedOnTime = doneCondition.awaitUntil(deadline);
                // check for spurious wakeup
                while (finishedOnTime && !done) {
                    finishedOnTime = doneCondition.awaitUntil(deadline);
                }
                return finishedOnTime;
            }
        } finally {
            lock.unlock();
        }
    }

    static class UniversalResultCollector<A> extends ResultCollector<A> {
        // status: success, failure, error
        private volatile Choice3<Unit, EvalFailure, Throwable> status;

        UniversalResultCollector(ImmutableVector<A> samples) {
            super(samples);
            this.status = Choice3.a(UNIT);
            checkIfDone();
        }

        @Override
        protected void handleSuccess(int sampleIndex) {
            // do nothing
        }

        @Override
        protected void handleFailure(int sampleIndex, EvalFailure failure) {
            cutoffIndex = sampleIndex;
            this.status = Choice3.b(failure);
        }

        @Override
        protected void handleError(int sampleIndex, Throwable error) {
            cutoffIndex = sampleIndex;
            this.status = Choice3.c(error);
        }

        public Either<Abnormal<A>, UniversalTestResult<A>> getResultBlocking(Duration timeout) {
            lock.lock();
            try {
                boolean finishedOnTime = await(timeout);
                return status.match(__ -> finishedOnTime
                                ? right(unfalsified(getSuccessCount()))
                                : left(timedOut(timeout, getSuccessCount())),
                        failure -> right(falsified(counterexample(failure, samples.unsafeGet(cutoffIndex)),
                                getSuccessCount())),
                        error -> left(error(samples.unsafeGet(cutoffIndex), error, getSuccessCount())));
            } catch (InterruptedException e) {
                return left(interrupted(maybe(e.getMessage()), getSuccessCount()));
            } finally {
                lock.unlock();
            }
        }

        private int getSuccessCount() {
            // TODO: rewrite getSuccessCount
            return samples
                    .zipWithIndex()
                    .foldLeft((acc, si) -> sampleIndexPassed(si._2())
                                    ? acc.add(si._1())
                                    : acc,
                            VectorBuilder.<A>builder())
                    .build()
                    .size();
        }

        private boolean sampleIndexPassed(int index) {
            return index < cutoffIndex && reported.isMarked(index);
        }

    }

    static class ExistentialResultCollector<A> extends ResultCollector<A> {
        // status: unproved, proved, error
        private volatile Choice3<Unit, A, Throwable> status;
        private EvalFailure[] collectedFailures;

        ExistentialResultCollector(ImmutableVector<A> samples) {
            super(samples);
            status = Choice3.a(UNIT);
            collectedFailures = new EvalFailure[samples.size()];
            checkIfDone();
        }

        @Override
        protected void handleSuccess(int sampleIndex) {
            cutoffIndex = sampleIndex;
            status = Choice3.b(samples.unsafeGet(sampleIndex));
        }

        @Override
        protected void handleFailure(int sampleIndex, EvalFailure failure) {
            collectedFailures[sampleIndex] = failure;
        }

        @Override
        protected void handleError(int sampleIndex, Throwable error) {
            cutoffIndex = sampleIndex;
            status = Choice3.c(error);
        }

        public Either<Abnormal<A>, ExistentialTestResult<A>> getResultBlocking(Duration timeout) {
            lock.lock();
            try {
                boolean finishedOnTime = await(timeout);
                return status.match(__ ->
                                finishedOnTime
                                        ? right(unproved(getCounterexampleCount()))
                                        : left(timedOut(timeout, 0)),
                        passedSample -> right(proved(passedSample, getCounterexampleCount())),
                        error -> left(error(samples.unsafeGet(cutoffIndex), error, 0)));
            } catch (InterruptedException e) {
                return left(interrupted(maybe(e.getMessage()), 0));
            } finally {
                lock.unlock();
            }
        }

        private int getCounterexampleCount() {
            int result = 0;
            for (int idx = 0; idx < samples.size(); idx++) {
                EvalFailure failure = collectedFailures[idx];
                if (failure != null) {
                    result += 1;
                }
            }
            return result;
        }
    }
}
