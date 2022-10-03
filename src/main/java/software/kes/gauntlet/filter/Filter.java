package software.kes.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static software.kes.gauntlet.filter.EmptyFilter.emptyFilterChain;
import static software.kes.gauntlet.filter.MultiFilter.multiFilter;

public interface Filter<A> extends Predicate<A> {
    Filter<A> add(Fn1<? super A, Boolean> predicate);

    @Override
    <Z> Filter<Z> contraMap(Fn1<? super Z, ? extends A> fn);

    boolean isEmpty();

    static <A> Filter<A> emptyFilter() {
        return emptyFilterChain();
    }

    static <A> Filter<A> filter(Fn1<? super A, Boolean> f) {
        return multiFilter(f);
    }
}
