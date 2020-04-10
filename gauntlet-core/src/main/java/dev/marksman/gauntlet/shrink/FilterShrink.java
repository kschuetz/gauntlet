package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;

final class FilterShrink<A> implements Shrink<A> {
    private final Shrink<A> underlying;
    private final Filter<A> filter;

    FilterShrink(Shrink<A> underlying, Filter<A> filter) {
        this.underlying = underlying;
        this.filter = filter;
    }

    @Override
    public Shrink<A> filter(Fn1<? super A, Boolean> predicate) {
        return new FilterShrink<>(underlying, filter.add(predicate));
    }

    @Override
    public ImmutableFiniteIterable<A> apply(A input) {
        return underlying.apply(input).filter(filter);
    }
}