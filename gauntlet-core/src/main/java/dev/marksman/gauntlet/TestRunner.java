package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;

import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.ResultCollector.existentialResultCollector;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;

public final class TestRunner {
    private static final TestRunner INSTANCE = new TestRunner();

    static TestRunner testRunner() {
        return INSTANCE;
    }

    public <A> IO<TestResult<A>> run(TestRunnerSettings settings,
                                     Quantifier quantifier,
                                     ImmutableVector<A> elements,
                                     Prop<A> property) {
        return io(() -> {
            Executor executor = settings.getExecutor();
            ResultCollector<A> collector = quantifier == EXISTENTIAL
                    ? existentialResultCollector(elements)
                    : universalResultCollector(elements);
            int elementCount = elements.size();
            for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
                EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, elementIndex, elements.unsafeGet(elementIndex));
                executor.execute(task);
            }
            return collector.getResultBlocking(settings.getTimeout());
        });
    }
}
