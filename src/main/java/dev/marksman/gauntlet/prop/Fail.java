package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.*;

import static dev.marksman.gauntlet.Failure.failure;

final class Fail<A> implements Prop<A> {
    private static final Name NAME = Name.name("Fail");
    private static final String DEFAULT_FAILURE_REASON = "Always fails";
    private static final Fail<?> INSTANCE = new Fail<>(DEFAULT_FAILURE_REASON);

    private final Failure failure;

    Fail(String failureReason) {
        this.failure = failure(NAME, failureReason);
    }

    @Override
    public EvalResult test(Context context, A data) {
        return EvalResult.evalResult(failure);
    }

    @Override
    public Name getName() {
        return NAME;
    }

    static <A> Fail<A> fail(String failureReason) {
        return new Fail<>(failureReason);
    }

    @SuppressWarnings("unchecked")
    static <A> Fail<A> fail() {
        return (Fail<A>) INSTANCE;
    }

}
