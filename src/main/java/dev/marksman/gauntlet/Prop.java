package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Contravariant;
import dev.marksman.gauntlet.prop.Props;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.Error.error;
import static dev.marksman.gauntlet.EvalResult.evalResult;

public interface Prop<A> extends Contravariant<A, Prop<?>> {
    EvalResult test(Context context, A data);

    Name getName();

    @Override
    default <B> Prop<B> contraMap(Fn1<? super B, ? extends A> fn) {
        return Props.mapped(fn, this);
    }

    default Prop<A> and(Prop<A> other) {
        return Props.conjunction(this, other);
    }

    default Prop<A> or(Prop<A> other) {
        return Props.disjunction(this, other);
    }

    default Prop<A> xor(Prop<A> other) {
        return Props.exclusiveDisjunction(this, other);
    }

    default Prop<A> not() {
        return Props.negation(this);
    }

    default Prop<A> implies(Prop<A> other) {
        return Props.implication(this, other);
    }

    default Prop<A> iff(Prop<A> other) {
        return Props.biconditional(this, other);
    }

    default Prop<A> rename(Name name) {
        return Props.named(name, this);
    }

    default EvalResult safeTest(Context context, A data) {
        try {
            return test(context, data);
        } catch (Exception e) {
            return evalResult(error(just(getName()), e));
        }
    }

    static <A> Prop<A> prop(Fn1<A, Boolean> predicate) {
        return Props.prop(predicate);
    }

    static <A> Prop<A> prop(Name name, Fn1<A, Boolean> predicate) {
        return Props.prop(name, predicate);
    }

    static <A> Prop<A> prop(String name, Fn1<A, Boolean> predicate) {
        return prop(Name.name(name), predicate);
    }

    static <A> Prop<A> pass() {
        return Props.pass();
    }

    static <A> Prop<A> fail() {
        return Props.fail();
    }

    static <A> Prop<A> fail(String failureReason) {
        return Props.fail(failureReason);
    }
}
