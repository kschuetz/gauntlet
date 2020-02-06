package dev.marksman.gauntlet.prop;

import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;
import static dev.marksman.gauntlet.Name.name;


final class Conjunction<A> implements Prop<A> {
    private final ImmutableNonEmptyFiniteIterable<Prop<A>> operands;
    private final Name name;

    Conjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        this.operands = operands;
        this.name = name(String.join(" ∧ ",
                operands.fmap(p -> p.getName().getValue())));
    }

    @Override
    public Prop<A> and(Prop<A> other) {
        return new Conjunction<>((other instanceof Conjunction<?>)
                ? operands.concat(((Conjunction<A>) other).operands)
                : operands.append(other));
    }

    @Override
    public EvalResult test(Context context, A data) {
        return operands.foldLeft((acc, operand) -> combine(acc, operand.test(context, data)),
                success());
    }

    private EvalResult combine(EvalResult acc, EvalResult item) {
        // success + success -> success
        // success + failure -> failure
        // failure + success -> failure
        // failure + failure -> failure

        return acc
                .match(success -> item
                                .match(__ -> item,
                                        f1 -> evalResult(failure(this, "Conjuncts failed.")
                                                .addCause(f1))),

                        f1 -> item.match(__ -> evalResult(f1),
                                f2 -> evalResult(f1.addCause(f2))));
    }

    @Override
    public Name getName() {
        return name;
    }
}
