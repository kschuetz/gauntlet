package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;
import static dev.marksman.gauntlet.TestRunnerUtils.getBlockTimeout;
import static dev.marksman.gauntlet.TestRunnerUtils.getDeadline;
import static dev.marksman.gauntlet.TestRunnerUtils.readBlock;
import static dev.marksman.gauntlet.UniversalTestResult.unfalsified;

public final class UniversalTestRunner {
    private static final UniversalTestRunner INSTANCE = new UniversalTestRunner();
    private static final int BLOCK_SIZE = 100;

    static UniversalTestRunner universalTestRunner() {
        return INSTANCE;
    }

    public <A> IO<Either<Abnormal<A>, UniversalTestResult<A>>> run(TestRunnerSettings settings,
                                                                   Iterable<A> samples,
                                                                   Prop<A> property) {
        return getDeadline(settings.getTimeout())
                .flatMap(deadline -> io(() -> {
                    Iterator<A> iterator = samples.iterator();
                    UniversalTestResult.Unfalsified<A> accumulator = unfalsified(0);
                    ImmutableVector<A> block = readBlock(BLOCK_SIZE, iterator);
                    while (!block.isEmpty()) {
                        Duration blockTimeout = getBlockTimeout(deadline, LocalDateTime.now());
                        Either<Abnormal<A>, UniversalTestResult<A>> blockResult = runBlock(settings.getExecutor(), blockTimeout, block, property);

                        Abnormal<A> abnormal = blockResult.projectA().orElse(null);
                        if (abnormal != null) {
                            return left(abnormal.addToSuccessCount(accumulator.getSuccessCount()));
                        }
                        UniversalTestResult<A> utr = blockResult.projectB().orElseThrow(AssertionError::new);

                        UniversalTestResult.Falsified<A> falsified = utr.projectB().orElse(null);
                        if (falsified != null) {
                            return right(accumulator.combine(falsified));
                        }

                        UniversalTestResult.Unfalsified<A> unfalsified = utr.projectA().orElseThrow(AssertionError::new);
                        accumulator = accumulator.combine(unfalsified);

                        block = readBlock(BLOCK_SIZE, iterator);
                    }
                    return right(accumulator);
                }));
    }

    private <A> Either<Abnormal<A>, UniversalTestResult<A>> runBlock(Executor executor,
                                                                     Duration timeout,
                                                                     ImmutableVector<A> elements,
                                                                     Prop<A> property) {
        ResultCollector.UniversalResultCollector<A> collector = universalResultCollector(elements);
        int elementCount = elements.size();
        for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
            EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, elementIndex, elements.unsafeGet(elementIndex));
            executor.execute(task);
        }
        return collector.getResultBlocking(timeout);
    }

}
