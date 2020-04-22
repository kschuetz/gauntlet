package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;

public interface RefinementTestRunner {
    <A> IO<Maybe<RefinedCounterexample<A>>> run(RefinementTestExecutionParameters executionParameters, RefinementTest<A> testData);
}

