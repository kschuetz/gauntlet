package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.io.IO;

public interface GeneratorTestRunner {
    <A> IO<GeneratorTestResult<A>> run(GeneratorTest<A> testData);
}
