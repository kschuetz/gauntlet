package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;

final class AlwaysPass<A> implements Prop<A> {
    private static final String NAME = "Pass";
    private static final AlwaysPass<?> INSTANCE = new AlwaysPass<>();

    @Override
    public EvalResult evaluate(A data) {
        return evalSuccess();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @SuppressWarnings("unchecked")
    static <A> AlwaysPass<A> alwaysPass() {
        return (AlwaysPass<A>) INSTANCE;
    }
}
