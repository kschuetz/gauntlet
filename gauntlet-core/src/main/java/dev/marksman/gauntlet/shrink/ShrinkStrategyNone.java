package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

class ShrinkStrategyNone<A> implements ShrinkStrategy<A> {
    private static final ShrinkStrategyNone<?> INSTANCE = new ShrinkStrategyNone<>();

    @Override
    public ImmutableFiniteIterable<A> apply(A input) {
        return emptyImmutableFiniteIterable();
    }

    @Override
    public ShrinkStrategy<A> filter(Fn1<? super A, Boolean> predicate) {
        return this;
    }

    @Override
    public <B> ShrinkStrategy<B> convert(Iso<A, A, B, B> iso) {
        return shrinkNone();
    }

    @Override
    public <B> ShrinkStrategy<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return shrinkNone();
    }

    @SuppressWarnings("unchecked")
    static <A> ShrinkStrategy<A> shrinkNone() {
        return (ShrinkStrategy<A>) INSTANCE;
    }
}
