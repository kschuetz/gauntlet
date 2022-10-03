package software.kes.gauntlet.prop;

import software.kes.collectionviews.Vector;
import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

import static software.kes.gauntlet.Cause.propertyPassed;
import static software.kes.gauntlet.EvalFailure.evalFailure;
import static software.kes.gauntlet.EvalSuccess.evalSuccess;
import static software.kes.gauntlet.Reasons.reasons;

final class Negation<A> implements Prop<A> {
    private final Prop<A> operand;
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

    public Prop<A> getOperand() {
        return this.operand;
    }
}
