package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.VectorBuilder;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static dev.marksman.gauntlet.FailedSample.failedSample;

final class UniversalResultCollector<A> implements ResultCollector<A> {
    private final ImmutableVector<A> samples;
    private final ReentrantLock lock;
    private final CheckList reported;
    private final Condition doneLatch;
    private volatile boolean done;
    private volatile int cutoffIndex;
    private volatile TestTaskResult result;

    public UniversalResultCollector(ImmutableVector<A> samples) {
        int sampleCount = samples.size();
        this.samples = samples;
        this.cutoffIndex = sampleCount;
        this.lock = new ReentrantLock();
        this.doneLatch = lock.newCondition();
        this.done = false;
        this.reported = new CheckList(sampleCount);
        this.result = TestTaskResult.success();
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
    public void reportResult(int sampleIndex, TestTaskResult result) {
        lock.lock();
        try {
            if (sampleIndex >= cutoffIndex) {
                return;
            }
            if (result.isFailure() || result.isError()) {
                cutoffIndex = sampleIndex;
                this.result = result;
            }

            reported.mark(sampleIndex);
            if (reported.firstUnmarkedIndex() >= cutoffIndex) {
                this.done = true;
                doneLatch.signalAll();
            }

        } finally {
            lock.unlock();
        }

    }

    public TestResult<A> getResultBlocking(Duration timeout) {
        lock.lock();
        try {
            if (!await(timeout)) {
                return TestResult.timedOut(getPassedSamples(), timeout);
            }
            return result.match(__ -> TestResult.passed(samples),
                    failure -> TestResult.falsified(getPassedSamples(), failedSample(failure, samples.unsafeGet(cutoffIndex))),
                    error -> TestResult.error(getPassedSamples(), samples.unsafeGet(cutoffIndex), error));
        } catch (InterruptedException e) {
            return TestResult.interrupted(getPassedSamples(), maybe(e.getMessage()));
        } finally {
            lock.unlock();
        }

    }

    private boolean await(Duration timeout) throws InterruptedException {
        lock.lock();
        try {
            if (done) {
                return true;
            } else {
                return doneLatch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
            }
        } finally {
            lock.unlock();
        }
    }

    private ImmutableVector<A> getPassedSamples() {
        lock.lock();
        try {
            return samples
                    .zipWithIndex()
                    .foldLeft((acc, si) -> sampleIndexPassed(si._2())
                                    ? acc.add(si._1())
                                    : acc,
                            VectorBuilder.<A>builder())
                    .build();
        } finally {
            lock.unlock();
        }
    }

    private boolean sampleIndexPassed(int index) {
        return index < cutoffIndex && reported.isMarked(index);
    }

}
