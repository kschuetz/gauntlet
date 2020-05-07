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

    public <A> IO<DomainTestResult<A>> run(DomainTest<A> domainTest) {
        return io(() -> {
            Executor executor = domainTest.getExecutor();
            Domain<A> domain = domainTest.getDomain();
            ImmutableVector<A> elements = Vector.copyFrom(domain.getElements());
            ResultCollector<A> collector = domainTest.getQuantifier() == EXISTENTIAL
                    ? existentialResultCollector(elements)
                    : universalResultCollector(elements);
            int elementCount = elements.size();
            for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
                EvaluateSampleTask<A> task = evaluateSampleTask(collector, domainTest.getProperty(), elementIndex, elements.unsafeGet(elementIndex));
                executor.execute(task);
            }
            return domainTestResult(collector.getResultBlocking(domainTest.getTimeout()));
        });
    }
}
