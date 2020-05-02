package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.RefinedCounterexample.refinedCounterexample;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;

public final class RefinementTestRunner {
    public static final Duration TIMEOUT_TODO = Duration.ofMinutes(1);
    private static final RefinementTestRunner INSTANCE = new RefinementTestRunner();

    private static <A> ImmutableVector<A> readBlock(int size, Iterator<A> source) {
        VectorBuilder<A> builder = Vector.builder(size);
        int i = 0;
        while (i < size && source.hasNext()) {
            builder = builder.add(source.next());
            i += 1;
        }
        return builder.build();
    }

    static RefinementTestRunner refinementTestRunner() {
        return INSTANCE;
    }

    public <A> IO<Maybe<RefinedCounterexample<A>>> run(RefinementTest<A> refinementTest) {
        return io(() -> {
            LocalDateTime deadline = LocalDateTime.now().plus(refinementTest.getTimeout());

            Session<A> session = new Session<>(refinementTest.getExecutor(),
                    refinementTest.getShrinkStrategy(), refinementTest.getProperty(), refinementTest.getMaximumShrinkCount(),
                    deadline, refinementTest.getBlockSize());

            return session.run(refinementTest.getSample());
        });
    }

    private static class Session<A> {
        private final Executor executor;
        private final ShrinkStrategy<A> shrinkStrategy;
        private final Prop<A> property;
        private final int maximumShrinkCount;
        private final LocalDateTime deadline;
        private final int blockSize;

        private Session(Executor executor, ShrinkStrategy<A> shrinkStrategy, Prop<A> property, int maximumShrinkCount, LocalDateTime deadline, int blockSize) {
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
                TestResult<A> result = runBlock(block, TIMEOUT_TODO);

                if (result instanceof TestResult.Falsified<?>) {
                    TestResult.Falsified<A> falsified = (TestResult.Falsified<A>) result;
                    return just(refinedCounterexample(falsified.getCounterexample(), 1 + shrinkCount + falsified.getSuccessCount()));
                } else if (result instanceof TestResult.Passed<?>) {
                    TestResult.Passed<A> passed = (TestResult.Passed<A>) result;
                    shrinkCount += passed.getSuccessCount();
                } else if (result instanceof TestResult.Proved<?>) {
                    throw new IllegalStateException("TestResult.Proved should never happen in a shrink test");
                } else if (result instanceof TestResult.Unproved<?>) {
                    throw new IllegalStateException("TestResult.Unproved should never happen in a shrink test");
                } else {
                    // an error occurred - abort
                    return nothing();
                }
            }
            return nothing();
        }

        private TestResult<A> runBlock(ImmutableVector<A> samples,
                                       Duration timeout) {
            int sampleCount = samples.size();
            ResultCollector<A> collector = universalResultCollector(samples);
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, sampleIndex, samples.unsafeGet(sampleIndex));
                executor.execute(task);
            }
            return collector.getResultBlocking(timeout);
        }
    }
}
