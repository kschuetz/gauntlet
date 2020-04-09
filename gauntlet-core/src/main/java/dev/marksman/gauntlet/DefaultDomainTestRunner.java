package dev.marksman.gauntlet;

public final class DefaultDomainTestRunner implements DomainTestRunner {
    private static final DefaultDomainTestRunner INSTANCE = new DefaultDomainTestRunner();

    @Override
    public <A> DomainTestResult<A> run(DomainTestExecutionParameters executionParameters, DomainTest<A> testData) {
        return null;
    }


    public static DefaultDomainTestRunner defaultGeneratorTestRunner() {
        return INSTANCE;
    }
}
