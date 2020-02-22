package dev.marksman.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.util.MapperChain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.filter.MappedFilter.compoundFilterChain;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class SimpleFilter<A> implements Filter<A> {
    private final ImmutableFiniteIterable<Fn1<A, Boolean>> filters;

    @Override
    public Filter<A> add(Fn1<A, Boolean> predicate) {
        return new SimpleFilter<>(filters.prepend(predicate));
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
    public <Z> Filter<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return compoundFilterChain(MapperChain.mapperChain((Fn1<Object, Object>) fn), this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static <A> Filter<A> simpleFilterChain(Fn1<A, Boolean> filter) {
        return new SimpleFilter<>(ImmutableFiniteIterable.of(filter));
    }

}
