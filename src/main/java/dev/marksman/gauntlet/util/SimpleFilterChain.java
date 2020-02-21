package dev.marksman.gauntlet.util;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.util.CompoundFilterChain.compoundFilterChain;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class SimpleFilterChain<A> implements FilterChain<A> {
    private final ImmutableFiniteIterable<Fn1<A, Boolean>> filters;

    @Override
    public FilterChain<A> add(Fn1<A, Boolean> filter) {
        return new SimpleFilterChain<>(filters.prepend(filter));
    }

    @Override
    public Boolean checkedApply(A a) {
        for (Fn1<A, Boolean> filter : filters) {
            if (!filter.apply(a)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z> FilterChain<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return compoundFilterChain(MapperChain.mapperChain((Fn1<Object, Object>) fn), this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static <A> FilterChain<A> simpleFilterChain(Fn1<A, Boolean> filter) {
        return new SimpleFilterChain<>(ImmutableFiniteIterable.of(filter));
    }

}
