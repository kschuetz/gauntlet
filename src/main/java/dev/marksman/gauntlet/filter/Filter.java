package dev.marksman.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static dev.marksman.gauntlet.filter.EmptyFilter.emptyFilterChain;
import static dev.marksman.gauntlet.filter.NamedFilter.namedFilter;
import static dev.marksman.gauntlet.filter.SimpleFilter.simpleFilterChain;

public interface Filter<A> extends Predicate<A> {
    Filter<A> add(Fn1<A, Boolean> predicate);

    @Override
    <Z> Filter<Z> contraMap(Fn1<? super Z, ? extends A> fn);

    boolean isEmpty();

    default Filter<A> add(String name, Fn1<A, Boolean> predicate) {
        return add(namedFilter(name, predicate));
    }

    static <A> Filter<A> emptyFilter() {
        return emptyFilterChain();
    }

    static <A> Filter<A> filter(Fn1<A, Boolean> f) {
        return simpleFilterChain(f);
    }

    static <A> Filter<A> filter(String name, Fn1<A, Boolean> predicate) {
        return simpleFilterChain(namedFilter(name, predicate));
    }
}
