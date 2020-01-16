package dev.marksman.gauntlet;

public interface PropTest<A> {
    TestResult<A> run();

    TestResult<A> runWithSeed(long seedValue);
}
