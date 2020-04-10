package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableVector;

import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.DomainTestResult.domainTestResult;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.ResultCollector.existentialResultCollector;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;

public final class DefaultDomainTestRunner implements DomainTestRunner {
    private static final DefaultDomainTestRunner INSTANCE = new DefaultDomainTestRunner();

    @Override
    public <A> DomainTestResult<A> run(DomainTestExecutionParameters executionParameters, DomainTest<A> testData) {
        Executor executor = executionParameters.getExecutor();
        Domain<A> domain = testData.getDomain();
        ImmutableVector<A> elements = domain.getElements();
        ResultCollector<A> collector = testData.getQuantifier() == EXISTENTIAL
                ? existentialResultCollector(elements)
                : universalResultCollector(elements);
        int elementCount = elements.size();
        for (int elementIndex = 0; elementIndex < elementCount; elementIndex++) {
            EvaluateSampleTask<A> task = evaluateSampleTask(collector, testData.getProperty(), elementIndex, elements.unsafeGet(elementIndex));
            executor.execute(task);
        }
        return domainTestResult(collector.getResultBlocking(testData.getTimeout().orElse(executionParameters.getDefaultTimeout())));
    }


    public static DefaultDomainTestRunner defaultDomainTestRunner() {
        return INSTANCE;
    }
}
