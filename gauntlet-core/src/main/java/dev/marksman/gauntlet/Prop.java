package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Contravariant;
import dev.marksman.gauntlet.prop.Props;

import static dev.marksman.gauntlet.prop.Props.biconditional;
import static dev.marksman.gauntlet.prop.Props.conjunction;
import static dev.marksman.gauntlet.prop.Props.disjunction;
import static dev.marksman.gauntlet.prop.Props.exclusiveDisjunction;
import static dev.marksman.gauntlet.prop.Props.implication;
import static dev.marksman.gauntlet.prop.Props.mapped;
import static dev.marksman.gauntlet.prop.Props.named;
import static dev.marksman.gauntlet.prop.Props.negation;

public interface Prop<A> extends Contravariant<A, Prop<?>>, Named {
    EvalResult evaluate(A data);

    String getName();

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

    default Prop<A> rename(String name) {
        return named(name, this);
    }

    default Prop<A> safe() {
        return Props.safe(this);
    }

    static <A> Prop<A> predicate(Fn1<? super A, Boolean> predicate) {
        return Props.predicate(predicate);
    }

    static <A> Prop<A> predicate(String name, Fn1<? super A, Boolean> predicate) {
        return Props.predicate(name, predicate);
    }

    static <A> Prop<A> prop(Fn1<? super A, SimpleResult> evaluator) {
        return Props.prop(evaluator);
    }

    static <A> Prop<A> prop(String name, Fn1<? super A, SimpleResult> evaluator) {
        return Props.prop(name, evaluator);
    }

    static <A> Prop<A> alwaysPass() {
        return Props.alwaysPass();
    }

    static <A> Prop<A> alwaysFail() {
        return Props.alwaysFail();
    }

    static <A> Prop<A> alwaysFail(String failureReason) {
        return Props.alwaysFail(failureReason);
    }

    static <A> Prop<A> dynamic(Fn1<A, Prop<A>> selector) {
        return Props.dynamic(selector);
    }
}
