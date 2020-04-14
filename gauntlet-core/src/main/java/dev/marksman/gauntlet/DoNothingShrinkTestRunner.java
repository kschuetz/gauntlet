package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;

public final class DoNothingShrinkTestRunner implements ShrinkTestRunner {
    private static final DoNothingShrinkTestRunner INSTANCE = new DoNothingShrinkTestRunner();

    @Override
    public <A> IO<Maybe<RefinedCounterexample<A>>> run(ShrinkTestExecutionParameters executionParameters, ShrinkTest<A> testData) {
        return io(nothing());
    }

    public static DoNothingShrinkTestRunner doNothingShrinkTestRunner() {
        return INSTANCE;
    }
}
