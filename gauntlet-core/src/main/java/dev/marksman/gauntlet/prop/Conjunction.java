package dev.marksman.gauntlet.prop;

import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;


final class Conjunction<A> implements Prop<A> {
    private final ImmutableNonEmptyFiniteIterable<Prop<A>> operands;
    private final String name;

    Conjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        this.operands = operands;
        this.name = String.join(" ∧ ",
                operands.fmap(Prop::getName));
    }

    @Override
    public Prop<A> and(Prop<A> other) {
        return new Conjunction<>((other instanceof Conjunction<?>)
                ? operands.concat(((Conjunction<A>) other).operands)
                : operands.append(other));
    }

    @Override
    public EvalResult test(A data) {
        return operands.foldLeft((acc, operand) -> combine(acc, operand.test(data)),
                (EvalResult) evalSuccess());
    }

    private EvalResult combine(EvalResult acc, EvalResult item) {
        // success + success -> success
        // success + failure -> failure
        // failure + success -> failure
        // failure + failure -> failure

        return acc
                .match(success -> item
                                .match(__ -> item,
                                        f1 -> EvalFailure.evalFailure(this, failureReasons("Conjuncts failed."))
                                                .addCause(f1)),

                        f1 -> item.match(__ -> f1,
                                f1::addCause));
    }

    @Override
    public String getName() {
        return name;
    }

}
