package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import dev.marksman.gauntlet.prop.Combinators;

public interface Prop<A> {
    Either<Errors, EvalResult> test(Context context, A data);

    Name getName();

    default Prop<A> and(Prop<A> other) {
        return Combinators.conjunction(this, other);
    }

    default Prop<A> or(Prop<A> other) {
        return Combinators.disjunction(this, other);
    }

    default Prop<A> not() {
        throw new UnsupportedOperationException("todo");
    }

    default Prop<A> implies(Prop<A> other) {
        throw new UnsupportedOperationException("todo");
    }

    default Prop<A> iff(Prop<A> other) {
        throw new UnsupportedOperationException("todo");
    }

    default Prop<A> named(Name name) {
        return Combinators.named(name, this);
    }
}
