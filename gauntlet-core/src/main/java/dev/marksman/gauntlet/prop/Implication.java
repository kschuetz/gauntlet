package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;

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
                                .match(EvalResult::evalResult,
                                        f1 -> evalResult(failure(this, "Implication failed.")
                                                .addCause(f1))),
                        failure -> success());
    }

    @Override
    public String getName() {
        return name;
    }
}
