package dev.marksman.gauntlet.util;

import com.jnape.palatable.lambda.functions.Fn1;

import static dev.marksman.gauntlet.util.CompoundFilterChain.compoundFilterChain;
import static dev.marksman.gauntlet.util.SimpleFilterChain.simpleFilterChain;

final class EmptyFilterChain<A> implements FilterChain<A> {

    private static final EmptyFilterChain<?> INSTANCE = new EmptyFilterChain<>();

    @Override
    public FilterChain<A> add(Fn1<A, Boolean> filter) {
        return simpleFilterChain(filter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z> FilterChain<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return compoundFilterChain(MapperChain.mapperChain((Fn1<Object, Object>) fn), this);
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
    static <A> EmptyFilterChain<A> emptyFilterChain() {
        return (EmptyFilterChain<A>) INSTANCE;
    }

}
