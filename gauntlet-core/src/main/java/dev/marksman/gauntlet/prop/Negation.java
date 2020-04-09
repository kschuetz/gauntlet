package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.FailureReasons;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;

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
                .match(__ -> EvalFailure.evalFailure(this, FailureReasons.failureReasons("Failure expected.")),
                        __ -> evalSuccess());
    }

    @Override
    public String getName() {
        return name;
    }
}
