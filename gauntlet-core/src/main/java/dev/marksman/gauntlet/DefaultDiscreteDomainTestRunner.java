package dev.marksman.gauntlet;

public final class DefaultDiscreteDomainTestRunner implements DiscreteDomainTestRunner {
    private static final DefaultDiscreteDomainTestRunner INSTANCE = new DefaultDiscreteDomainTestRunner();

    @Override
    public <A> DiscreteDomainTestResult<A> run(DiscreteDomainTestExecutionParameters executionParameters, DiscreteDomainTest<A> testData) {
        return null;
    }


    public static DefaultDiscreteDomainTestRunner defaultGeneratorTestRunner() {
        return INSTANCE;
    }
}
