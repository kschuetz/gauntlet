package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;
import static dev.marksman.gauntlet.Name.name;

final class Negation<A> implements Prop<A> {
    final Prop<A> operand;
    private final Name name;

    Negation(Prop<A> operand) {
        this.operand = operand;
        this.name = name("~" + operand.getName());
    }

    @Override
    public Prop<A> not() {
        return operand;
    }

    @Override
    public EvalResult test(Context context, A data) {
        // success -> failure
        // failure -> success
        // error -> error
        return operand.safeTest(context, data)
                .match(__ -> evalResult(failure(name, "Failure expected.")),
                        __ -> success(),
                        EvalResult::evalResult);
    }

    @Override
    public Name getName() {
        return name;
    }
}
