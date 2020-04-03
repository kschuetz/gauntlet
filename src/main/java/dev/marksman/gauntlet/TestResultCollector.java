package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableVector;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.AscribedFailure.ascribedFailure;

class TestResultCollector<A> implements TestResultReceiver {
    private final ImmutableVector<A> samples;
    private final Object lock = new Object();
    private final Boolean[] reported;
    private final CountDownLatch latch;
    private volatile Maybe<AscribedFailure<A>> earliestFailure;
    private volatile boolean done;

    public TestResultCollector(ImmutableVector<A> samples) {
        int sampleCount = samples.size();
        this.samples = samples;
        this.earliestFailure = nothing();
        this.reported = new Boolean[sampleCount];
        this.done = false;
        this.latch = new CountDownLatch(sampleCount);
    }

    @Override
    public boolean shouldRun(int sampleIndex) {
        synchronized (lock) {
            if (done) {
                return false;
            } else {
                return earliestFailure.match(__ -> true,
                        ef -> ef.getSampleIndex() < sampleIndex);
            }
        }
    }

    @Override
    public void reportResult(int sampleIndex, TestTaskResult result) {
//        synchronized (lock) {
//            if (done || reported[sampleIndex]) {
//                return;
//            }
//            Maybe<Failure> maybeFailure = result.flatMap(CoProduct2::projectB);
//            maybeFailure.toOptional().ifPresent(failure -> handleFailure(sampleIndex, failure));
//            reported[sampleIndex] = true;
//            latch.countDown();
//        }
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public Maybe<AscribedFailure<A>> getResultBlocking() throws InterruptedException {
        latch.await();
        return earliestFailure;
    }

    private void handleFailure(int sampleIndex, Failure failure) {
        earliestFailure = just(earliestFailure
                .match(__ -> ascribedFailure(sampleIndex, samples.unsafeGet(sampleIndex), failure),
                        ef -> sampleIndex < ef.getSampleIndex()
                                ? ascribedFailure(sampleIndex, samples.unsafeGet(sampleIndex), failure)
                                : ef));
    }

}