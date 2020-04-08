package dev.marksman.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.util.MapperChain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.filter.MappedFilter.mappedFilter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class MultiFilter<A> implements Filter<A> {
    private final ImmutableFiniteIterable<Fn1<? super A, Boolean>> filters;

    @Override
    public Filter<A> add(Fn1<? super A, Boolean> predicate) {
        return new MultiFilter<>(filters.prepend(predicate));
    }

    @Override
    public Boolean checkedApply(A a) {
        for (Fn1<? super A, Boolean> filter : filters) {
            if (!filter.apply(a)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z> Filter<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return mappedFilter(MapperChain.mapperChain((Fn1<Object, Object>) fn), this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static <A> Filter<A> multiFilter(Fn1<? super A, Boolean> filter) {
        return new MultiFilter<>(ImmutableFiniteIterable.of(filter));
    }

}
