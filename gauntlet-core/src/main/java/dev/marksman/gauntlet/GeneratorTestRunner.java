package dev.marksman.gauntlet;

import java.util.concurrent.Executor;

public interface GeneratorTestRunner {
    <A> GeneratorTestResult<A> run(Executor executor, GeneratorTest<A> testData);
}
