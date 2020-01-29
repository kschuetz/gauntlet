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
        // success + error -> error
        // failure + _ -> success
        // error + success -> error
        // error + failure -> error
        // error + error -> error
        return antecedent.safeTest(context, data)
                .match(success ->
                                consequent.safeTest(context, data)
                                        .match(EvalResult::evalResult,
                                                f1 -> evalResult(failure(this, "Implication failed.")
                                                        .addCause(f1)),
                                                EvalResult::evalResult),
                        failure -> success(),
                        e1 ->
                                consequent.safeTest(context, data)
                                        .match(__ -> evalResult(e1),
                                                __ -> evalResult(e1),
                                                e2 -> evalResult(e1.combine(e2))));
    }

    @Override
    public Name getName() {
        return name;
    }
}
