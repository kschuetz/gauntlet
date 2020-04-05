package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;

import java.time.Duration;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;

class TestResultCollector<A> implements TestResultReceiver {
    private final ImmutableVector<A> samples;
    private final ReentrantLock lock;
    private final TreeSet<Integer> notReported;
    private final Condition doneLatch;
    private volatile boolean done;
    private volatile int firstFailureIndex;
    private volatile TestTaskResult result;

    public TestResultCollector(ImmutableVector<A> samples) {
        int sampleCount = samples.size();
        this.samples = samples;
        this.firstFailureIndex = sampleCount;
        this.lock = new ReentrantLock();
        this.doneLatch = lock.newCondition();
        this.done = false;
        this.notReported = Vector.range(sampleCount).toCollection(TreeSet::new);
        this.result = TestTaskResult.success();
    }

    @Override
    public boolean shouldRun(int sampleIndex) {
        lock.lock();
        try {
            return sampleIndex < firstFailureIndex;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void reportResult(int sampleIndex, TestTaskResult result) {
        lock.lock();
        try {
            if (sampleIndex >= firstFailureIndex) {
                return;
            }
            if (result.isFailure() || result.isError()) {
                firstFailureIndex = sampleIndex;
                this.result = result;
            }

            notReported.remove(sampleIndex);
            if (getFirstUnreportedIndex() >= firstFailureIndex) {
                this.done = true;
                doneLatch.signalAll();
            }

        } finally {
            lock.unlock();
        }

    }

    public Outcome<A> getResultBlocking(Duration timeout) {
        lock.lock();
        try {
            if (!await(timeout)) {
                return Outcome.timedOut(getPassedSamples(), timeout);
            }
            return getOutcome();
        } catch (InterruptedException e) {
            return Outcome.interrupted(getPassedSamples(), maybe(e.getMessage()));
        } finally {
            lock.unlock();
        }

    }

    private Outcome<A> getOutcome() {
        return result.match(__ -> Outcome.passed(samples),
                failure -> Outcome.falsified(getPassedSamples(), samples.unsafeGet(firstFailureIndex), failure, Vector.empty()),
                error -> Outcome.error(getPassedSamples(), samples.unsafeGet(firstFailureIndex), error));
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

    private int getFirstUnreportedIndex() {
        lock.lock();
        try {
            if (notReported.isEmpty()) {
                return samples.size();
            } else {
                return notReported.first();
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
                    .foldLeft((acc, si) -> notReported.contains(si._2())
                                    ? acc
                                    : acc.add(si._1()),
                            VectorBuilder.<A>builder())
                    .build();
        } finally {
            lock.unlock();
        }
    }

}
