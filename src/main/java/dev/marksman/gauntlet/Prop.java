package dev.marksman.gauntlet;

import dev.marksman.gauntlet.prop.Combinators;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.Error.error;
import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.prop.Combinators.*;

public interface Prop<A> {
    EvalResult test(Context context, A data);

    Name getName();

    default Prop<A> and(Prop<A> other) {
        return conjunction(this, other);
    }

    default Prop<A> or(Prop<A> other) {
        return disjunction(this, other);
    }

    default Prop<A> not() {
        return negation(this);
    }

    default Prop<A> implies(Prop<A> other) {
        return implication(this, other);
    }

    default Prop<A> iff(Prop<A> other) {
        return biconditional(this, other);
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
