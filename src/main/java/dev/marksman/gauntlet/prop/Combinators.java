package dev.marksman.gauntlet.prop;

import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

public final class Combinators {

    private Combinators() {
    }

    public static <A> Prop<A> conjunction(Prop<A> first, Prop<A> second) {
        return new Conjunction<>(Vector.of(first, second));
    }

    public static <A> Prop<A> conjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        return new Conjunction<>(operands);
    }

    public static <A> Prop<A> named(Name name, Prop<A> prop) {
        while (prop instanceof Named<?>) {
            prop = ((Named<A>) prop).underlying;
        }
        return new Named<>(name, prop);
    }
}
