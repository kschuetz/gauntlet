package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class GeneratorTestResult<A> {
    TestResult<A> result;
    long initialSeedValue;

    public GeneratorTestResult<A> modifyResult(Fn1<TestResult<A>, TestResult<A>> f) {
        return new GeneratorTestResult<>(f.apply(result), initialSeedValue);
    }

    public static <A> GeneratorTestResult<A> generatorTestResult(TestResult<A> testResult,
                                                                 long initialSeedValue) {
        return new GeneratorTestResult<>(testResult, initialSeedValue);
    }
}
