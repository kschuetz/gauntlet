package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Failure;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;

class Biconditional<A> implements Prop<A> {
    final Prop<A> antecedent;
    final Prop<A> consequent;
    private final String name;

    Biconditional(Prop<A> antecedent, Prop<A> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.name = antecedent.getName() + " <=> " + consequent.getName();
    }

    @Override
    public EvalResult test(Context context, A data) {
        // success + success -> success
        // success + failure -> failure
        // failure + success -> failure
        // failure + failure -> success
        return antecedent.test(context, data)
                .match(success -> consequent.test(context, data)
                                .match(EvalResult::evalResult,
                                        f1 -> evalResult(createFailure()
                                                .addCause(f1))),
                        failure -> consequent.test(context, data)
                                .match(__ -> evalResult(createFailure()
                                                .addCause(failure)),
                                        __ -> success()));
    }

    @Override
    public String getName() {
        return name;
    }

    private Failure createFailure() {
        return failure(this, "Biconditional failed.");
    }
}
