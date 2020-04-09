package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.FailureReasons;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.FailureReasons.failureReasons;

final class AlwaysFail<A> implements Prop<A> {
    private static final String NAME = "Fail";
    private static final AlwaysFail<?> INSTANCE = new AlwaysFail<>(failureReasons("Always fails"));

    private final FailureReasons failureReasons;

    AlwaysFail(FailureReasons failureReasons) {
        this.failureReasons = failureReasons;
    }

    @Override
    public EvalResult test(A data) {
        return EvalFailure.evalFailure(this, failureReasons);
    }

    @Override
    public String getName() {
        return NAME;
    }

    static <A> AlwaysFail<A> alwaysFail(FailureReasons failureReasons) {
        return new AlwaysFail<>(failureReasons);
    }

    @SuppressWarnings("unchecked")
    static <A> AlwaysFail<A> alwaysFail() {
        return (AlwaysFail<A>) INSTANCE;
    }

}
