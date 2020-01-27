package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;


final class PredicateProp<A> implements Prop<A> {
    private final Name name;
    private final Fn1<A, Boolean> predicate;

    PredicateProp(Name name, Fn1<A, Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public EvalResult test(Context context, A data) {
        return predicate.apply(data)
                ? success()
                : evalResult(failure(name, "Predicate failed."));
    }

    @Override
    public Name getName() {
        return name;
    }
}
