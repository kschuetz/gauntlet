package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Contravariant;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.gauntlet.prop.Facade;
import dev.marksman.gauntlet.prop.Isomorphic;

import static dev.marksman.gauntlet.prop.Facade.biconditional;
import static dev.marksman.gauntlet.prop.Facade.conjunction;
import static dev.marksman.gauntlet.prop.Facade.disjunction;
import static dev.marksman.gauntlet.prop.Facade.exclusiveDisjunction;
import static dev.marksman.gauntlet.prop.Facade.implication;
import static dev.marksman.gauntlet.prop.Facade.mapped;
import static dev.marksman.gauntlet.prop.Facade.negation;

public interface Prop<A> extends Contravariant<A, Prop<?>>, Named {
    static <A> Prop<A> predicate(Fn1<? super A, Boolean> predicate) {
        return Facade.predicate(predicate);
    }

    static <A> Prop<A> predicate(String name, Fn1<? super A, Boolean> predicate) {
        return Facade.predicate(name, predicate);
    }

    static <A> Prop<A> prop(Fn1<? super A, SimpleResult> evaluator) {
        return Facade.prop(evaluator);
    }

    static <A> Prop<A> prop(String name, Fn1<? super A, SimpleResult> evaluator) {
        return Facade.prop(name, evaluator);
    }

    static <A> Prop<A> alwaysPass() {
        return Facade.alwaysPass();
    }

    static <A> Prop<A> alwaysFail() {
        return Facade.alwaysFail();
    }

    static <A> Prop<A> alwaysFail(String failureReason) {
        return Facade.alwaysFail(failureReason);
    }

    static <A> Prop<A> dynamic(Fn1<A, Prop<A>> selector) {
        return Facade.dynamic(selector);
    }

    static <A> Prop<A> named(String name, Prop<A> prop) {
        return Facade.named(name, prop);
    }

    @SafeVarargs
    static <A> Prop<A> allOf(Prop<A> first, Prop<A>... more) {
        return Facade.conjunction(NonEmptyVector.of(first, more));
    }

    @SafeVarargs
    static <A> Prop<A> anyOf(Prop<A> first, Prop<A>... more) {
        return Facade.disjunction(NonEmptyVector.of(first, more));
    }

    @SafeVarargs
    static <A> Prop<A> notAllOf(Prop<A> first, Prop<A>... more) {
        return Facade.conjunction(NonEmptyVector.of(first, more)).not();
    }

    @SafeVarargs
    static <A> Prop<A> noneOf(Prop<A> first, Prop<A>... more) {
        return Facade.disjunction(NonEmptyVector.of(first, more)).not();
    }

    /**
     * Returns a property that passes iff all provided functions return the equivalent outputs for the given input.
     * <p>
     * Outputs are compared using {@link Object#equals} by default, but the strategy for comparison can
     * be overridden using {@link Isomorphic#withEquivalenceRelation(Fn2)}.
     */
    @SafeVarargs
    static <A, B> Isomorphic<A, B> isomorphic(Fn1<? super A, ? extends B> f1,
                                              Fn1<? super A, ? extends B> f2,
                                              Fn1<? super A, ? extends B>... more) {
        return Isomorphic.isomorphic(f1, f2, more);
    }

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
        return Facade.safe(this);
    }

    default <B> Prop<Product2<? super A, ? super B>> zipAnd(Prop<B> other) {
        return Facade.zip2(Prop::and, this, other);
    }

    default <B> Prop<Product2<? super A, ? super B>> zipOr(Prop<B> other) {
        return Facade.zip2(Prop::or, this, other);
    }

    default <B> Prop<Product2<? super A, ? super B>> zipImplies(Prop<B> other) {
        return Facade.zip2(Prop::implies, this, other);
    }

    default <B> Prop<Product2<? super A, ? super B>> zipIff(Prop<B> other) {
        return Facade.zip2(Prop::iff, this, other);
    }

    default <B> Prop<Product2<? super A, ? super B>> zipXor(Prop<B> other) {
        return Facade.zip2(Prop::xor, this, other);
    }
}
