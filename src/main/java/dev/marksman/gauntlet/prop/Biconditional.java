package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.*;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;

class Biconditional<A> implements Prop<A> {
    final Prop<A> antecedent;
    final Prop<A> consequent;
    private final Name name;

    Biconditional(Prop<A> antecedent, Prop<A> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.name = Name.name(antecedent.getName() + " <=> " + consequent.getName());
    }

    @Override
    public EvalResult test(Context context, A data) {
        // success + success -> success
        // success + failure -> failure
        // success + error -> error
        // failure + success -> failure
        // failure + failure -> success
        // failure + error -> error
        // error + success -> error
        // error + failure -> error
        // error + error -> error
        return antecedent.safeTest(context, data)
                .match(success ->
                                consequent.safeTest(context, data)
                                        .match(EvalResult::evalResult,
                                                f1 -> evalResult(createFailure()
                                                        .addCause(consequent.getName(), f1)),
                                                EvalResult::evalResult),
                        failure ->
                                consequent.safeTest(context, data)
                                        .match(__ -> evalResult(createFailure()
                                                        .addCause(antecedent.getName(), failure)),
                                                __ -> success(),
                                                EvalResult::evalResult),
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

    private Failure createFailure() {
        return failure(name, "Biconditional failed.");
    }
}
