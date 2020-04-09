package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;


final class PredicateProp<A> implements Prop<A> {
    private final String name;
    private final Fn1<? super A, Boolean> predicate;

    PredicateProp(String name, Fn1<? super A, Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public EvalResult test(A data) {
        return predicate.apply(data)
                ? evalSuccess()
                : EvalFailure.evalFailure(this, failureReasons("Predicate failed."));
    }

    @Override
    public String getName() {
        return name;
    }
}
