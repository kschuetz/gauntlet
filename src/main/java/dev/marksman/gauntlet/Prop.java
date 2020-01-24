package dev.marksman.gauntlet;

import dev.marksman.gauntlet.prop.Combinators;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.Error.error;
import static dev.marksman.gauntlet.EvalResult.evalResult;

public interface Prop<A> {
    EvalResult test(Context context, A data);

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

    default EvalResult safeTest(Context context, A data) {
        try {
            return test(context, data);
        } catch (Exception e) {
            return evalResult(error(just(getName()), e));
        }
    }
}
