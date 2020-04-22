package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;

final class FilterShrinkStrategy<A> implements ShrinkStrategy<A> {
    private final ShrinkStrategy<A> underlying;
    private final Filter<A> filter;

    FilterShrinkStrategy(ShrinkStrategy<A> underlying, Filter<A> filter) {
        this.underlying = underlying;
        this.filter = filter;
    }

    @Override
    public ShrinkStrategy<A> filter(Fn1<? super A, Boolean> predicate) {
        return new FilterShrinkStrategy<>(underlying, filter.add(predicate));
    }

    @Override
    public ImmutableFiniteIterable<A> apply(A input) {
        return underlying.apply(input).filter(filter);
    }
}