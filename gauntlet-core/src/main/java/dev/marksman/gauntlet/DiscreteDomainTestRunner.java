package dev.marksman.gauntlet;

public interface DiscreteDomainTestRunner {
    <A> DiscreteDomainTestResult<A> run(DiscreteDomainTestExecutionParameters executionParameters, DiscreteDomainTest<A> testData);
}
