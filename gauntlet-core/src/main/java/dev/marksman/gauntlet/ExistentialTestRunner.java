package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import dev.marksman.collectionviews.ImmutableVector;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static dev.marksman.gauntlet.Abnormal.exhausted;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.ExistentialTestResult.unproved;
import static dev.marksman.gauntlet.ResultCollector.existentialResultCollector;
import static dev.marksman.gauntlet.TestRunnerUtils.getBlockTimeout;
import static dev.marksman.gauntlet.TestRunnerUtils.getDeadline;

public final class ExistentialTestRunner {
    private static final ExistentialTestRunner INSTANCE = new ExistentialTestRunner();
    private static final int BLOCK_SIZE = 100;

    static ExistentialTestRunner existentialTestRunner() {
        return INSTANCE;
    }

    public <A> Either<Abnormal<A>, ExistentialTestResult<A>> run(TestRunnerSettings settings,
                                                                 Prop<A> property,
                                                                 SampleReader<A> sampleReader) {
        LocalDateTime deadline = getDeadline(settings.getTimeout()).unsafePerformIO();
        ExistentialTestResult.Unproved<A> accumulator = unproved(0);
        SampleBlock<A> block = sampleReader.readBlock(BLOCK_SIZE);
        while (!block.isEmpty()) {
            Duration blockTimeout = getBlockTimeout(deadline, LocalDateTime.now());
            Either<Abnormal<A>, ExistentialTestResult<A>> blockResult = runBlock(settings.getExecutor(), blockTimeout, block.getSamples(), property);

            Abnormal<A> abnormal = blockResult.projectA().orElse(null);
            if (abnormal != null) {
                return left(abnormal);
            }
            ExistentialTestResult<A> utr = blockResult.projectB().orElseThrow(AssertionError::new);

            ExistentialTestResult.Proved<A> proved = utr.projectB().orElse(null);
            if (proved != null) {
                return right(accumulator.combine(proved));
            }

            ExistentialTestResult.Unproved<A> unproved = utr.projectA().orElseThrow(AssertionError::new);
            accumulator = accumulator.combine(unproved);

            block = sampleReader.readBlock(BLOCK_SIZE);
        }
        SupplyFailure supplyFailure = block.getSupplyFailure().orElse(null);
        if (supplyFailure == null) {
            return right(accumulator);
        } else {
            return left(exhausted(supplyFailure, 0));
        }
    }

    private <A> Either<Abnormal<A>, ExistentialTestResult<A>> runBlock(Executor executor,
                                                                       Duration timeout,
                                                                       ImmutableVector<A> elements,
                                                                       Prop<A> property) {
        ResultCollector.ExistentialResultCollector<A> collector = existentialResultCollector(elements);
        int elementCount = elements.size();
        for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
            EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, elementIndex, elements.unsafeGet(elementIndex));
            executor.execute(task);
        }
        return collector.getResultBlocking(timeout);
    }

}
