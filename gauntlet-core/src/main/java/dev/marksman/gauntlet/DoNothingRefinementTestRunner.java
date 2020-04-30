package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;

public final class DoNothingRefinementTestRunner implements RefinementTestRunner {
    private static final DoNothingRefinementTestRunner INSTANCE = new DoNothingRefinementTestRunner();

    public static DoNothingRefinementTestRunner doNothingRefinementTestRunner() {
        return INSTANCE;
    }

    @Override
    public <A> IO<Maybe<RefinedCounterexample<A>>> run(RefinementTestExecutionParameters executionParameters, RefinementTest<A> testData) {
        return io(nothing());
    }
}
