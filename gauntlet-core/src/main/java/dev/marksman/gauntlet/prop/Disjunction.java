package dev.marksman.gauntlet.prop;

import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.Cause;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalFailure.evalFailure;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.Reasons.reasons;
import static dev.marksman.gauntlet.prop.Accumulator.accumulator;


final class Disjunction<A> implements Prop<A> {
    private final ImmutableNonEmptyFiniteIterable<Prop<A>> operands;
    private final String name;

    Disjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        this.operands = operands;
        this.name = String.join(" ∨ ",
                operands.fmap(Prop::getName));
    }

    @Override
    public Prop<A> or(Prop<A> other) {
        return new Disjunction<>((other instanceof Disjunction<?>)
                ? operands.concat(((Disjunction<A>) other).operands)
                : operands.append(other));
    }

    // success + _ -> success
    // failure + success -> success
    // failure + failure -> failure
    @Override
    public EvalResult evaluate(A data) {
        Accumulator accumulator = accumulator();
        for (Prop<A> operand : operands) {
            accumulator = accumulator.add(operand.evaluate(data));
            if (accumulator.getSuccessCount() > 0) {
                return evalSuccess();
            }
        }
        return evalFailure(this, reasons("All properties in disjunction failed"),
                accumulator.getFailures().build().fmap(Cause::propertyFailed));
    }

    @Override
    public String getName() {
        return name;
    }
}
