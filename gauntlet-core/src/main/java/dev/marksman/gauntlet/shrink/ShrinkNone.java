package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

class ShrinkNone<A> implements Shrink<A> {
    private static ShrinkNone<?> INSTANCE = new ShrinkNone<>();

    @Override
    public ImmutableFiniteIterable<A> apply(A input) {
        return emptyImmutableFiniteIterable();
    }

    @Override
    public Shrink<A> filter(Fn1<? super A, Boolean> predicate) {
        return this;
    }

    @Override
    public <B> Shrink<B> convert(Iso<A, A, B, B> iso) {
        return shrinkNone();
    }

    @Override
    public <B> Shrink<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return shrinkNone();
    }

    @SuppressWarnings("unchecked")
    static <A> Shrink<A> shrinkNone() {
        return (Shrink<A>) INSTANCE;
    }
}
