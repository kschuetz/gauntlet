package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.*;


class Disjunction<A> implements Prop<A> {
    private final ImmutableNonEmptyFiniteIterable<Prop<A>> operands;
    private final Name name;

    Disjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        this.operands = operands;
        this.name = Name.name(String.join(" ∨ ",
                operands.fmap(p -> p.getName().getValue())));
    }

    @Override
    public Prop<A> or(Prop<A> other) {
        return new Disjunction<>((other instanceof Disjunction<?>)
                ? operands.concat(((Disjunction<A>) other).operands)
                : operands.append(other));
    }

    @Override
    public Either<Errors, EvalResult> test(Context context, A data) {
        return Evaluator.evaluate(EvalState.failure(Failure.failure(name, "All disjuncts failed")),
                context,
                data,
                this::evalFn,
                operands);
    }

    public Choice3<Errors, EvalResult, EvalState> evalFn(EvalInfo<A> info) {
        return null;
    }


    @Override
    public Name getName() {
        return name;
    }
}
