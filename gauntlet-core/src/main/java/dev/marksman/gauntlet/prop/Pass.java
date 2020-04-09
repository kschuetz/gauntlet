package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.success;

final class Pass<A> implements Prop<A> {
    private static final String NAME = "Pass";
    private static final Pass<?> INSTANCE = new Pass<>();

    @Override
    public EvalResult test(A data) {
        return success();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @SuppressWarnings("unchecked")
    static <A> Pass<A> pass() {
        return (Pass<A>) INSTANCE;
    }
}
