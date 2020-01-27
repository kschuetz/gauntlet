package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

public final class Combinators {

    private Combinators() {
    }

    public static <A> Prop<A> conjunction(Prop<A> p, Prop<A> q) {
        return new Conjunction<>(Vector.of(p, q));
    }

    public static <A> Prop<A> conjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        return new Conjunction<>(operands);
    }

    public static <A> Prop<A> disjunction(Prop<A> p, Prop<A> q) {
        return new Disjunction<>(Vector.of(p, q));
    }

    public static <A> Prop<A> disjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        return new Disjunction<>(operands);
    }

    public static <A> Prop<A> exclusiveDisjunction(Prop<A> p, Prop<A> q) {
        return new ExclusiveDisjunction<>(p, q);
    }

    public static <A> Prop<A> negation(Prop<A> operand) {
        if (operand instanceof Negation<?>) {
            return ((Negation<A>) operand).operand;
        } else {
            return new Negation<>(operand);
        }
    }

    public static <A> Prop<A> implication(Prop<A> antecedent, Prop<A> consequent) {
        return new Implication<>(antecedent, consequent);
    }

    public static <A> Prop<A> biconditional(Prop<A> antecedent, Prop<A> consequent) {
        return new Biconditional<>(antecedent, consequent);
    }

    public static <A> Prop<A> named(Name name, Prop<A> prop) {
        while (prop instanceof Named<?>) {
            prop = ((Named<A>) prop).underlying;
        }
        return new Named<>(name, prop);
    }

    public static <A, B> Prop<B> mapped(Fn1<? super B, ? extends A> fn, Prop<A> prop) {
        return new Mapped<>(fn, prop);
    }
}
