package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.gauntlet.*;

final class AlwaysFail<A> implements Prop<A> {
    private final Name name;
    private final Failure failure;

    AlwaysFail(Name name, Maybe<String> failureReason) {
        this.name = name;
        this.failure = Failure.failure(name, failureReason.orElse("Always fails."));
    }

    @Override
    public EvalResult test(Context context, A data) {
        return EvalResult.evalResult(failure);
    }

    @Override
    public Name getName() {
        return name;
    }
}
