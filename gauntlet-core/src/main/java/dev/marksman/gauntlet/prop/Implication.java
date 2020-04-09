package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;

class Implication<A> implements Prop<A> {
    final Prop<A> antecedent;
    final Prop<A> consequent;
    private final String name;

    Implication(Prop<A> antecedent, Prop<A> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.name = antecedent.getName() + " => " + consequent.getName();
    }

    @Override
    public EvalResult test(A data) {
        // success + success -> success
        // success + failure -> failure
        // failure + _ -> success

        return antecedent.test(data)
                .match(success -> consequent.test(data)
                                .match(id(),
                                        f1 -> EvalFailure.evalFailure(this, failureReasons("Implication failed."))
                                                .addCause(f1)),
                        failure -> evalSuccess());
    }

    @Override
    public String getName() {
        return name;
    }
}
