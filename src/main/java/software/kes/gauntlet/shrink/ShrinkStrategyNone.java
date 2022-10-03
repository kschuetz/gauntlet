package software.kes.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import software.kes.enhancediterables.ImmutableFiniteIterable;

import static software.kes.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

final class ShrinkStrategyNone<A> implements ShrinkStrategy<A> {
    private static final ShrinkStrategyNone<?> INSTANCE = new ShrinkStrategyNone<>();

    @SuppressWarnings("unchecked")
    static <A> ShrinkStrategy<A> shrinkNone() {
        return (ShrinkStrategy<A>) INSTANCE;
    }

    @Override
    public ImmutableFiniteIterable<A> apply(A input) {
        return emptyImmutableFiniteIterable();
    }

    @Override
    public ShrinkStrategy<A> filter(Fn1<? super A, Boolean> predicate) {
        return this;
    }

    @Override
    public <B> ShrinkStrategy<B> convert(Iso<? super A, ? extends A, ? extends B, ? super B> iso) {
        return shrinkNone();
    }

    @Override
    public <B> ShrinkStrategy<B> convert(Fn1<? super A, ? extends B> ab, Fn1<? super B, ? extends A> ba) {
        return shrinkNone();
    }
}
