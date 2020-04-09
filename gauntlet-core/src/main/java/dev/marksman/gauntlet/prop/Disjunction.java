package dev.marksman.gauntlet.prop;

import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;


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

    @Override
    public EvalResult test(A data) {
        EvalResult result = EvalFailure.evalFailure(this, failureReasons("All disjuncts failed."));
        for (Prop<A> prop : operands) {
            EvalResult test = prop.test(data);
            if (test.isSuccess()) {
                return test;
            } else {
                result = combine(result, test);
            }
        }
        return result;
    }

    private EvalResult combine(EvalResult acc, EvalResult item) {
        // success + _ -> success
        // failure + success -> success
        // failure + failure -> failure

        return acc
                .match(id(),
                        f1 -> item.match(id(),
                                f1::addCause));
    }

    @Override
    public String getName() {
        return name;
    }
}
