package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Contravariant;
import dev.marksman.gauntlet.prop.Props;

import static dev.marksman.gauntlet.prop.Props.*;

public interface Prop<A> extends Contravariant<A, Prop<?>>, Named {
    EvalResult test(Context context, A data);

    Name getName();

    @Override
    default <B> Prop<B> contraMap(Fn1<? super B, ? extends A> fn) {
        return mapped(fn, this);
    }

    default Prop<A> and(Prop<A> other) {
        return conjunction(this, other);
    }

    default Prop<A> or(Prop<A> other) {
        return disjunction(this, other);
    }

    default Prop<A> xor(Prop<A> other) {
        return exclusiveDisjunction(this, other);
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

    default Prop<A> rename(Name name) {
        return named(name, this);
    }

    default Prop<A> safe() {
        return Props.safe(this);
    }

    static <A> Prop<A> predicate(Fn1<? super A, Boolean> predicate) {
        return Props.predicate(predicate);
    }

    static <A> Prop<A> predicate(Name name, Fn1<? super A, Boolean> predicate) {
        return Props.predicate(name, predicate);
    }

    static <A> Prop<A> predicate(String name, Fn1<? super A, Boolean> predicate) {
        return Props.predicate(name, predicate);
    }

    static <A> Prop<A> prop(Fn1<? super A, BasicPropResult> evaluator) {
        return Props.prop(evaluator);
    }

    static <A> Prop<A> prop(Name name, Fn1<? super A, BasicPropResult> evaluator) {
        return Props.prop(name, evaluator);
    }

    static <A> Prop<A> prop(String name, Fn1<? super A, BasicPropResult> evaluator) {
        return Props.prop(name, evaluator);
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

    static <A> Prop<A> dynamic(Fn1<A, Prop<A>> selector) {
        return Props.dynamic(selector);
    }
}
