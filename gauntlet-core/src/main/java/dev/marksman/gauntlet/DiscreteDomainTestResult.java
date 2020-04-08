package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class DiscreteDomainTestResult<A> {
    TestResult<A> result;

    public static <A> DiscreteDomainTestResult<A> generatorTestResult(TestResult<A> testResult) {
        return new DiscreteDomainTestResult<>(testResult);
    }
}
