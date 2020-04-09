package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class DomainTestResult<A> {
    TestResult<A> result;

    public static <A> DomainTestResult<A> domainTestResult(TestResult<A> testResult) {
        return new DomainTestResult<>(testResult);
    }
}
