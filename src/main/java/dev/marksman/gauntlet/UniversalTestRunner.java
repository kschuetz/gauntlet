package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static dev.marksman.gauntlet.Abnormal.exhausted;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;
import static dev.marksman.gauntlet.TestRunnerUtils.getBlockTimeout;
import static dev.marksman.gauntlet.TestRunnerUtils.getDeadline;
import static dev.marksman.gauntlet.UniversalTestResult.unfalsified;

final class UniversalTestRunner {
    private static final UniversalTestRunner INSTANCE = new UniversalTestRunner();
    private static final int BLOCK_SIZE = 100;

    static UniversalTestRunner universalTestRunner() {
        return INSTANCE;
    }

    public <Sample, A> Either<Abnormal<Sample>, UniversalTestResult<Sample>> run(TestRunnerSettings settings,
                                                                                 Fn1<Sample, A> getSampleValue,
                                                                                 Prop<A> property,
                                                                                 SampleReader<Sample> sampleReader) {
        LocalDateTime deadline = getDeadline(settings.getTimeout(), LocalDateTime.now());
        UniversalTestResult.Unfalsified<Sample> accumulator = unfalsified(0);
        SampleBlock<Sample> block = sampleReader.readBlock(BLOCK_SIZE);
        while (!block.isEmpty()) {
            Duration blockTimeout = getBlockTimeout(deadline, LocalDateTime.now());
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
        ResultCollector.UniversalResultCollector<Sample> collector = universalResultCollector(elements);
        int elementCount = elements.size();
        for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
            A sample = getSampleValue.apply(elements.unsafeGet(elementIndex));
            EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, elementIndex, sample);
            executor.execute(task);
        }
        return collector.getResultBlocking(timeout);
    }
}
