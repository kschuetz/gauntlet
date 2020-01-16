package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

class FilterShrink<A> implements Shrink<A> {
    private final Shrink<A> underlying;
    private final Fn1<A, Boolean> predicate;

    FilterShrink(Shrink<A> underlying, Fn1<A, Boolean> predicate) {
        this.underlying = underlying;
        this.predicate = predicate;
    }

    @Override
    public ImmutableFiniteIterable<A> apply(A input) {
        return underlying.apply(input).filter(predicate);
    }
}