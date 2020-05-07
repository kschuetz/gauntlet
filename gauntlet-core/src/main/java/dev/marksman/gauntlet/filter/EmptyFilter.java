package dev.marksman.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.util.MapperChain;

import static dev.marksman.gauntlet.filter.MappedFilter.mappedFilter;
import static dev.marksman.gauntlet.filter.MultiFilter.multiFilter;

final class EmptyFilter<A> implements Filter<A> {
    private static final EmptyFilter<?> INSTANCE = new EmptyFilter<>();

    @Override
    public Filter<A> add(Fn1<? super A, Boolean> predicate) {
        return multiFilter(predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z> Filter<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return mappedFilter(MapperChain.mapperChain((Fn1<Object, Object>) fn), this);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Boolean checkedApply(A a) {
        return true;
    }

    @SuppressWarnings("unchecked")
    static <A> EmptyFilter<A> emptyFilterChain() {
        return (EmptyFilter<A>) INSTANCE;
    }
}
