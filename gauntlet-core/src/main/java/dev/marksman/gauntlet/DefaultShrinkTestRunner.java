package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;

public final class DefaultShrinkTestRunner implements ShrinkTestRunner {
    private static final DefaultShrinkTestRunner INSTANCE = new DefaultShrinkTestRunner();

    @Override
    public <A> IO<Maybe<RefinedCounterexample<A>>> run(ShrinkTestExecutionParameters executionParameters,
                                                       ShrinkTest<A> testData) {
        // TODO: DefaultShrinkTestRunner
        return io(nothing());
    }

    public static DefaultShrinkTestRunner defaultShrinkTestRunner() {
        return INSTANCE;
    }
}
