package dev.marksman.gauntlet;

public interface GeneratorTestRunner {
    <A> GeneratorTestResult<A> run(GeneratorTestExecutionParameters executionParameters, GeneratorTest<A> testData);
}
