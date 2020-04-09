package dev.marksman.gauntlet;

public interface DomainTestRunner {
    <A> DomainTestResult<A> run(DomainTestExecutionParameters executionParameters, DomainTest<A> testData);
}
