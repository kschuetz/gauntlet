package dev.marksman.gauntlet.prop;

import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.Cause.propertyPassed;
import static dev.marksman.gauntlet.EvalFailure.evalFailure;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.Reasons.reasons;

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
    public EvalResult evaluate(A data) {
        // success -> failure
        // failure -> success

        return operand.evaluate(data)
                .match(__ -> evalFailure(this, reasons("Failure expected"),
                        Vector.of(propertyPassed(operand))),
                        __ -> evalSuccess());
    }

    @Override
    public String getName() {
        return name;
    }
}
