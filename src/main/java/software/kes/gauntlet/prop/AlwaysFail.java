package software.kes.gauntlet.prop;

import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;
import software.kes.gauntlet.Reasons;

import static software.kes.gauntlet.EvalFailure.evalFailure;
import static software.kes.gauntlet.Reasons.reasons;

final class AlwaysFail<A> implements Prop<A> {
    private static final String NAME = "Fail";
    private static final AlwaysFail<?> INSTANCE = new AlwaysFail<>(reasons("Always fails"));

    private final Reasons reasons;

    AlwaysFail(Reasons reasons) {
        this.reasons = reasons;
    }

    static <A> AlwaysFail<A> alwaysFail(Reasons reasons) {
        return new AlwaysFail<>(reasons);
    }

    @SuppressWarnings("unchecked")
    static <A> AlwaysFail<A> alwaysFail() {
        return (AlwaysFail<A>) INSTANCE;
    }

    @Override
    public EvalResult evaluate(A data) {
        return evalFailure(this, reasons);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
