package dev.marksman.gauntlet;

public interface GeneratorTestRunner {
    <A> Outcome<A> run(GeneratorTest<A> testData);
}
