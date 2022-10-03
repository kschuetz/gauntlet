package software.kes.gauntlet.prop;

import software.kes.enhancediterables.ImmutableNonEmptyFiniteIterable;
import software.kes.gauntlet.Cause;
import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

import static software.kes.gauntlet.EvalFailure.evalFailure;
import static software.kes.gauntlet.EvalSuccess.evalSuccess;
import static software.kes.gauntlet.Reasons.reasons;

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
        Accumulator accumulator = Accumulator.accumulator();
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
