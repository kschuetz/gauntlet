package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.io.IO;

public interface DomainTestRunner {
    <A> IO<DomainTestResult<A>> run(DomainTestExecutionParameters executionParameters, DomainTest<A> testData);
}
