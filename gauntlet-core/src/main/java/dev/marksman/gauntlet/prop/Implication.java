package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;
import static dev.marksman.gauntlet.Name.name;

class Implication<A> implements Prop<A> {
    final Prop<A> antecedent;
    final Prop<A> consequent;
    private final Name name;

    Implication(Prop<A> antecedent, Prop<A> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.name = name(antecedent.getName() + " => " + consequent.getName());
    }

    @Override
    public EvalResult test(Context context, A data) {
        // success + success -> success
        // success + failure -> failure
        // failure + _ -> success

        return antecedent.test(context, data)
                .match(success -> consequent.test(context, data)
                                .match(EvalResult::evalResult,
                                        f1 -> evalResult(failure(this, "Implication failed.")
                                                .addCause(f1))),
                        failure -> success());
    }

    @Override
    public Name getName() {
        return name;
    }
}
