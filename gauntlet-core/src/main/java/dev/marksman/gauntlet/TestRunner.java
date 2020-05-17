package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.Quantifier.UNIVERSAL;
import static dev.marksman.gauntlet.ResultCollector.existentialResultCollector;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;

public final class TestRunner {
    private static final TestRunner INSTANCE = new TestRunner();
    private static final int BLOCK_SIZE = 100;

    static TestRunner testRunner() {
        return INSTANCE;
    }

    public <A> IO<TestResult<A>> run(TestRunnerSettings settings,
                                     Quantifier quantifier,
                                     Iterable<A> samples,
                                     Prop<A> property) {
        return io(() -> {
            LocalDateTime deadline = LocalDateTime.now().plus(settings.getTimeout());
            Iterator<A> iterator = samples.iterator();
            ResultAccumulator<A> accumulator = initialAccumulator(quantifier);
            ImmutableVector<A> block = readBlock(BLOCK_SIZE, iterator);
            while (!block.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                Duration blockTimeout = now.isBefore(deadline)
                        ? Duration.between(now, deadline)
                        : Duration.ZERO;
                TestResult<A> blockResult = runBlock(settings.getExecutor(), blockTimeout, quantifier, block, property);
                Choice2<TestResult<A>, ResultAccumulator<A>> combined = combineResults(accumulator, blockResult);
                TestResult<A> returnResult = combined.projectA().orElse(null);
                if (returnResult != null) {
                    return returnResult;
                }
                accumulator = combined.projectB().orElse(null);

                block = readBlock(BLOCK_SIZE, iterator);
            }
            return accumulator.getResult();
        });
    }

    private <A> Choice2<TestResult<A>, ResultAccumulator<A>> combineResults(ResultAccumulator<A> accumulator,
                                                                            TestResult<A> blockResult) {
        return accumulator.match(accPassed ->
                        blockResult.match(passed -> Choice2.b(resultAccumulator(accPassed.combine(passed))),
                                proved -> {
                                    throw new IllegalStateException();
                                },
                                falsified -> null,
                                unproved -> {
                                    throw new IllegalStateException();
                                },
                                supplyFailed -> {
                                    throw new IllegalStateException();
                                },
                                error -> Choice2.a(accPassed.combine(error)),
                                timedOut -> Choice2.a(accPassed.combine(timedOut)),
                                interrupted -> Choice2.a(accPassed.combine(interrupted))),
                accUnproved ->
                        blockResult.match(passed -> {
                                    throw new IllegalStateException();
                                },
                                proved -> Choice2.a(accUnproved.combine(proved)),
                                falsified -> {
                                    throw new IllegalStateException();
                                },
                                unproved -> Choice2.b(resultAccumulator(accUnproved.combine(unproved))),
                                supplyFailed -> {
                                    throw new IllegalStateException();
                                },
                                error -> Choice2.a(accUnproved.combine(error)),
                                timedOut -> Choice2.a(accUnproved.combine(timedOut)),
                                interrupted -> Choice2.a(accUnproved.combine(interrupted))));
    }

    private <A> TestResult<A> runBlock(Executor executor,
                                       Duration timeout,
                                       Quantifier quantifier,
                                       ImmutableVector<A> elements,
                                       Prop<A> property) {
        ResultCollector<A> collector = quantifier == EXISTENTIAL
                ? existentialResultCollector(elements)
                : universalResultCollector(elements);
        int elementCount = elements.size();
        for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
            EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, elementIndex, elements.unsafeGet(elementIndex));
            executor.execute(task);
        }
        return collector.getResultBlocking(timeout);
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

    private static <A> ResultAccumulator<A> initialAccumulator(Quantifier quantifier) {
        return quantifier == UNIVERSAL
                ? resultAccumulator(TestResult.passed(0))
                : resultAccumulator(TestResult.unproved(0));
    }

    private static <A> ResultAccumulator<A> resultAccumulator(TestResult.Passed<A> value) {
        return new ResultAccumulator<>(Choice2.a(value));
    }

    private static <A> ResultAccumulator<A> resultAccumulator(TestResult.Unproved<A> value) {
        return new ResultAccumulator<>(Choice2.b(value));
    }

    private static class ResultAccumulator<A> implements CoProduct2<TestResult.Passed<A>, TestResult.Unproved<A>, ResultAccumulator<A>> {
        private final Choice2<TestResult.Passed<A>, TestResult.Unproved<A>> value;

        private ResultAccumulator(Choice2<TestResult.Passed<A>, TestResult.Unproved<A>> value) {
            this.value = value;
        }

        @Override
        public <R> R match(Fn1<? super TestResult.Passed<A>, ? extends R> aFn, Fn1<? super TestResult.Unproved<A>, ? extends R> bFn) {
            return value.match(aFn, bFn);
        }

        public TestResult<A> getResult() {
            return value.match(id(), id());
        }
    }
}
