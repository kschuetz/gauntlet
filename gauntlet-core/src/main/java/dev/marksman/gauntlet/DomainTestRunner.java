package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;

import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.DomainTestResult.domainTestResult;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.ResultCollector.existentialResultCollector;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;

public final class DomainTestRunner {
    private static final DomainTestRunner INSTANCE = new DomainTestRunner();

    static DomainTestRunner domainTestRunner() {
        return INSTANCE;
    }

    public <A> IO<DomainTestResult<A>> run(DomainTestSettings settings,
                                           Quantifier quantifier,
                                           Domain<A> domain,
                                           Prop<A> property) {
        return io(() -> {
            Executor executor = settings.getExecutor();
            ImmutableVector<A> elements = Vector.copyFrom(domain.getElements());
            ResultCollector<A> collector = quantifier == EXISTENTIAL
                    ? existentialResultCollector(elements)
                    : universalResultCollector(elements);
            int elementCount = elements.size();
            for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
                EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, elementIndex, elements.unsafeGet(elementIndex));
                executor.execute(task);
            }
            return domainTestResult(collector.getResultBlocking(settings.getTimeout()));
        });
    }
}
