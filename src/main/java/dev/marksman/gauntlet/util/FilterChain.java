package dev.marksman.gauntlet.util;

import com.jnape.palatable.lambda.functions.Fn1;

import static dev.marksman.gauntlet.util.SimpleFilterChain.simpleFilterChain;

public interface FilterChain<A> extends Fn1<A, Boolean> {
    FilterChain<A> add(Fn1<A, Boolean> filter);

    @Override
    <Z> FilterChain<Z> contraMap(Fn1<? super Z, ? extends A> fn);

    static <A> FilterChain<A> filterChain() {
        return simpleFilterChain();
    }

    static <A> FilterChain<A> filterChain(Fn1<A, Boolean> f) {
        return simpleFilterChain(f);
    }
}
