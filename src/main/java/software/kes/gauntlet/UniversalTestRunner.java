package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.collectionviews.ImmutableVector;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static software.kes.gauntlet.Abnormal.exhausted;
import static software.kes.gauntlet.EvaluateSampleTask.evaluateSampleTask;

final class UniversalTestRunner {
    private static final int BLOCK_SIZE = 100;
    private final Clock clock;

    public UniversalTestRunner(Clock clock) {
        this.clock = clock;
    }

    static UniversalTestRunner universalTestRunner(Clock clock) {
        return new UniversalTestRunner(clock);
    }

    public <Sample, A> Either<Abnormal<Sample>, UniversalTestResult<Sample>> run(TestRunnerSettings settings,
                                                                                 Fn1<Sample, A> getSampleValue,
                                                                                 Prop<A> property,
                                                                                 SampleReader<Sample> sampleReader) {
        Instant deadline = TestRunnerUtils.getDeadline(settings.getTimeout(), clock.instant());
        UniversalTestResult.Unfalsified<Sample> accumulator = UniversalTestResult.unfalsified(0);
        SampleBlock<Sample> block = sampleReader.readBlock(BLOCK_SIZE);
        while (!block.isEmpty()) {
            Duration blockTimeout = TestRunnerUtils.getBlockTimeout(deadline, clock.instant());
            Either<Abnormal<Sample>, UniversalTestResult<Sample>> blockResult = runBlock(settings.getExecutor(), blockTimeout,
                    getSampleValue, block.getSamples(), property);

            Abnormal<Sample> abnormal = blockResult.projectA().orElse(null);
            if (abnormal != null) {
                return left(abnormal.addToSuccessCount(accumulator.getSuccessCount()));
            }
            UniversalTestResult<Sample> utr = blockResult.projectB().orElseThrow(AssertionError::new);

            UniversalTestResult.Falsified<Sample> falsified = utr.projectB().orElse(null);
            if (falsified != null) {
                return right(accumulator.combine(falsified));
            }

            UniversalTestResult.Unfalsified<Sample> unfalsified = utr.projectA().orElseThrow(AssertionError::new);
            accumulator = accumulator.combine(unfalsified);

            block = sampleReader.readBlock(BLOCK_SIZE);
        }
        SupplyFailure supplyFailure = block.getSupplyFailure().orElse(null);
        if (supplyFailure == null) {
            return right(accumulator);
        } else {
            return left(exhausted(supplyFailure, accumulator.getSuccessCount()));
        }
    }

    private <Sample, A> Either<Abnormal<Sample>, UniversalTestResult<Sample>> runBlock(Executor executor,
                                                                                       Duration timeout,
                                                                                       Fn1<Sample, A> getSampleValue,
                                                                                       ImmutableVector<Sample> elements,
                                                                                       Prop<A> property) {
        ResultCollector.UniversalResultCollector<Sample> collector = ResultCollector.universalResultCollector(elements);
        int elementCount = elements.size();
        for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
            A sample = getSampleValue.apply(elements.unsafeGet(elementIndex));
            EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, elementIndex, sample);
            executor.execute(task);
        }
        return collector.getResultBlocking(timeout);
    }
}
