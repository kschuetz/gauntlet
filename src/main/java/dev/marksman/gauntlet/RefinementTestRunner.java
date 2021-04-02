package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;

final class RefinementTestRunner {
    public static final Duration TIMEOUT_TODO = Duration.ofMinutes(1);
    private final Clock clock;

    public RefinementTestRunner(Clock clock) {
        this.clock = clock;
    }

    private static <A> ImmutableVector<A> readBlock(int size, Iterator<A> source) {
        VectorBuilder<A> builder = Vector.builder(size);
        int i = 0;
        while (i < size && source.hasNext()) {
            builder = builder.add(source.next());
            i += 1;
        }
        return builder.build();
    }

    static RefinementTestRunner refinementTestRunner(Clock clock) {
        return new RefinementTestRunner(clock);
    }

    public <A> Maybe<RefinedCounterexample<A>> run(RefinementTest<A> refinementTest) {
        Instant deadline = clock.instant().plus(refinementTest.getTimeout());

        Session<A> session = new Session<>(refinementTest.getExecutor(),
                refinementTest.getShrinkStrategy(), refinementTest.getProperty(), refinementTest.getMaximumShrinkCount(),
                deadline, refinementTest.getBlockSize());

        return session.run(refinementTest.getSample());
    }

    private static class Session<A> {
        private final Executor executor;
        private final ShrinkStrategy<A> shrinkStrategy;
        private final Prop<A> property;
        private final int maximumShrinkCount;
        private final Instant deadline;
        private final int blockSize;

        private Session(Executor executor, ShrinkStrategy<A> shrinkStrategy, Prop<A> property, int maximumShrinkCount, Instant deadline, int blockSize) {
            this.executor = executor;
            this.shrinkStrategy = shrinkStrategy;
            this.property = property;
            this.maximumShrinkCount = maximumShrinkCount;
            this.deadline = deadline;
            this.blockSize = blockSize;
        }

        Maybe<RefinedCounterexample<A>> run(A initialSample) {
            RefinedCounterexample<A> candidate = null;
            int shrinkCount = 0;
            A sample = initialSample;
            while (shrinkCount < maximumShrinkCount) {
                RefinedCounterexample<A> newCandidate = refine(shrinkCount, sample)
                        .orElse(null);
                if (newCandidate == null) {
                    break;
                } else {
                    candidate = newCandidate;
                    shrinkCount = candidate.getShrinkCount();
                    sample = candidate.getCounterexample().getSample();
                }

            }
            return maybe(candidate);
        }

        private Maybe<RefinedCounterexample<A>> refine(int shrinkCount,
                                                       A sample) {
            if (shrinkCount >= maximumShrinkCount) {
                return nothing();
            }

            Iterator<A> source = shrinkStrategy.apply(sample).iterator();
            while (shrinkCount < maximumShrinkCount) {
                int actualBlockSize = Math.min(blockSize, maximumShrinkCount - shrinkCount);
                ImmutableVector<A> block = readBlock(actualBlockSize, source);
                if (block.isEmpty()) {
                    return nothing();
                }
                Either<Abnormal<A>, UniversalTestResult<A>> result = runBlock(block, TIMEOUT_TODO);

                Abnormal<A> error = result.projectA().orElse(null);
                if (error != null) {
                    // an error occurred - abort
                    return nothing();
                }
                UniversalTestResult<A> utr = result.projectB().orElseThrow(AssertionError::new);

                UniversalTestResult.Falsified<A> falsified = utr.projectB().orElse(null);
                if (falsified != null) {
                    return just(RefinedCounterexample.refinedCounterexample(falsified.getCounterexample(), 1 + shrinkCount + falsified.getSuccessCount()));
                }

                UniversalTestResult.Unfalsified<A> unfalsified = utr.projectA().orElseThrow(AssertionError::new);
                shrinkCount += unfalsified.getSuccessCount();
            }
            return nothing();
        }

        private Either<Abnormal<A>, UniversalTestResult<A>> runBlock(ImmutableVector<A> samples,
                                                                     Duration timeout) {
            int sampleCount = samples.size();
            ResultCollector.UniversalResultCollector<A> collector = universalResultCollector(samples);
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, sampleIndex, samples.unsafeGet(sampleIndex));
                executor.execute(task);
            }
            return collector.getResultBlocking(timeout);
        }
    }
}
