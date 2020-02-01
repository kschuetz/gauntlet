package dev.marksman.gauntlet.util;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.util.SimpleFilterChain.simpleFilterChain;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CompoundFilterChain<A, B> implements FilterChain<A> {
    private final MapperChain mapperChain;
    private final FilterChain<B> underlying;

    @Override
    public FilterChain<A> add(Fn1<A, Boolean> filter) {
        return simpleFilterChain(this).add(filter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z> FilterChain<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return new CompoundFilterChain<>(mapperChain.prepend((Fn1<Object, Object>) fn), underlying);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean checkedApply(A a) {
        B b = (B) mapperChain.apply(a);
        return underlying.apply(b);
    }

    static <A, B> CompoundFilterChain<A, B> compoundFilterChain(MapperChain mapperChain,
                                                                FilterChain<B> underlying) {
        return new CompoundFilterChain<>(mapperChain, underlying);
    }

}
