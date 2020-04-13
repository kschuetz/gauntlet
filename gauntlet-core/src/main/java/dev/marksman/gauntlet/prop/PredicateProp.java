package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalFailure.evalFailure;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.Reasons.reasons;


final class PredicateProp<A> implements Prop<A> {
    private final String name;
    private final Fn1<? super A, Boolean> predicate;

    PredicateProp(String name, Fn1<? super A, Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public EvalResult evaluate(A data) {
        return predicate.apply(data)
                ? evalSuccess()
                : evalFailure(this, reasons("Predicate failed"));
    }

    @Override
    public String getName() {
        return name;
    }
}
