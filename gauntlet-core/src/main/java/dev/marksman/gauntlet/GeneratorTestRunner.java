package dev.marksman.gauntlet;

public interface GeneratorTestRunner {
    <A> GeneratorTestResult<A> run(GeneratorTest<A> testData);
}
