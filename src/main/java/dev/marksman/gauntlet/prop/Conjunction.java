package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.adt.Either;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.*;


class Conjunction<A> implements Prop<A> {
    private final ImmutableNonEmptyFiniteIterable<Prop<A>> operands;
    private final Name name;

    Conjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        this.operands = operands;
        this.name = Name.name(String.join(" ∧ ",
                operands.fmap(p -> p.getName().getValue())));
    }

    @Override
    public Prop<A> and(Prop<A> other) {
        return new Conjunction<>((other instanceof Conjunction<?>)
                ? operands.concat(((Conjunction<A>) other).operands)
                : operands.append(other));
    }

    @Override
    public Either<Errors, EvalResult> test(Context context, A data) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Name getName() {
        return name;
    }
}
