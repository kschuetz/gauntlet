package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class GeneratorTestResult<A> {
    TestResult<A> result;
    long initialSeedValue;

    public static <A> GeneratorTestResult<A> generatorTestResult(TestResult<A> testResult,
                                                                 long initialSeedValue) {
        return new GeneratorTestResult<>(testResult, initialSeedValue);
    }
}
