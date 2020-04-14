package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;

public interface ShrinkTestRunner {
    <A> IO<Maybe<RefinedCounterexample<A>>> run(ShrinkTestExecutionParameters executionParameters, ShrinkTest<A> testData);
}

