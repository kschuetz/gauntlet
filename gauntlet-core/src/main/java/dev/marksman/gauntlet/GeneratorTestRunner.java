package dev.marksman.gauntlet;

public interface GeneratorTestRunner {
    <A> GeneratorTestResult<A> run(GauntletEnvironment environment, GeneratorTest<A> testData);
}
