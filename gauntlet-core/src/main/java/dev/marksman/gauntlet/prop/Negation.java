package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;

final class Negation<A> implements Prop<A> {
    final Prop<A> operand;
    private final String name;

    Negation(Prop<A> operand) {
        this.operand = operand;
        this.name = "~" + operand.getName();
    }

    @Override
    public Prop<A> not() {
        return operand;
    }

    @Override
    public EvalResult test(A data) {
        // success -> failure
        // failure -> success

        return operand.test(data)
                .match(__ -> evalResult(failure(this, "Failure expected.")),
                        __ -> success());
    }

    @Override
    public String getName() {
        return name;
    }
}
