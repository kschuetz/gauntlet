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
import static dev.marksman.gauntlet.ExistentialTestResult.unproved;
import static dev.marksman.gauntlet.ResultCollector.existentialResultCollector;
import static dev.marksman.gauntlet.TestRunnerUtils.getBlockTimeout;
import static dev.marksman.gauntlet.TestRunnerUtils.getDeadline;
import static dev.marksman.gauntlet.TestRunnerUtils.readBlock;

public final class ExistentialTestRunner {
    private static final ExistentialTestRunner INSTANCE = new ExistentialTestRunner();
    private static final int BLOCK_SIZE = 100;

    static ExistentialTestRunner existentialTestRunner() {
        return INSTANCE;
    }

    public <A> IO<Either<Abnormal<A>, ExistentialTestResult<A>>> run(TestRunnerSettings settings,
                                                                     Iterable<A> samples,
                                                                     Prop<A> property) {
        return getDeadline(settings.getTimeout())
                .flatMap(deadline -> io(() -> {
                    Iterator<A> iterator = samples.iterator();
                    ExistentialTestResult.Unproved<A> accumulator = unproved(0);
                    ImmutableVector<A> block = readBlock(BLOCK_SIZE, iterator);
                    while (!block.isEmpty()) {
                        Duration blockTimeout = getBlockTimeout(deadline, LocalDateTime.now());
                        Either<Abnormal<A>, ExistentialTestResult<A>> blockResult = runBlock(settings.getExecutor(), blockTimeout, block, property);

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

                        block = readBlock(BLOCK_SIZE, iterator);
                    }
                    return right(accumulator);
                }));
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
