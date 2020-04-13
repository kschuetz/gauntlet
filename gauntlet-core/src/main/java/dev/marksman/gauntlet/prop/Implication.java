package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.Cause.propertyFailed;
import static dev.marksman.gauntlet.Cause.propertyPassed;
import static dev.marksman.gauntlet.EvalFailure.evalFailure;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.Reasons.reasons;

final class Implication<A> implements Prop<A> {
    final Prop<A> antecedent;
    final Prop<A> consequent;
    private final String name;

    Implication(Prop<A> antecedent, Prop<A> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.name = antecedent.getName() + " => " + consequent.getName();
    }

    @Override
    public EvalResult evaluate(A data) {
        // success + success -> success
        // success + failure -> failure
        // failure + _ -> success

        return antecedent.evaluate(data)
                .match(success -> consequent.evaluate(data)
                                .match(id(),
                                        f1 -> evalFailure(this, reasons("Implication failed."))
                                                .addCause(propertyPassed(antecedent))
                                                .addCause(propertyFailed(f1))),
                        failure -> evalSuccess());
    }

    @Override
    public String getName() {
        return name;
    }
}
