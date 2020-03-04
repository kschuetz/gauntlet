package dev.marksman.gauntlet;

public interface GeneratorTestRunner {
    <A> Report<A> run(GeneratorTest<A> testData);
}
