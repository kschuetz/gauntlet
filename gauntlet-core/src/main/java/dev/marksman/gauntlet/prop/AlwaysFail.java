package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.Reasons;

import static dev.marksman.gauntlet.EvalFailure.evalFailure;
import static dev.marksman.gauntlet.Reasons.reasons;

final class AlwaysFail<A> implements Prop<A> {
    private static final String NAME = "Fail";
    private static final AlwaysFail<?> INSTANCE = new AlwaysFail<>(reasons("Always fails"));

    private final Reasons reasons;

    AlwaysFail(Reasons reasons) {
        this.reasons = reasons;
    }

    @Override
    public EvalResult evaluate(A data) {
        return evalFailure(this, reasons);
    }

    @Override
    public String getName() {
        return NAME;
    }

    static <A> AlwaysFail<A> alwaysFail(Reasons reasons) {
        return new AlwaysFail<>(reasons);
    }

    @SuppressWarnings("unchecked")
    static <A> AlwaysFail<A> alwaysFail() {
        return (AlwaysFail<A>) INSTANCE;
    }

}
