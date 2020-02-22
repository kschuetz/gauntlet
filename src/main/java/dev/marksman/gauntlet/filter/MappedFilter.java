package dev.marksman.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.util.MapperChain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.filter.SimpleFilter.simpleFilterChain;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class MappedFilter<A, B> implements Filter<A> {
    private final MapperChain mapperChain;
    private final Filter<B> underlying;

    @Override
    public Filter<A> add(Fn1<A, Boolean> predicate) {
        return simpleFilterChain(this).add(predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Z> Filter<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return new MappedFilter<>(mapperChain.prepend((Fn1<Object, Object>) fn), underlying);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean checkedApply(A a) {
        B b = (B) mapperChain.apply(a);
        return underlying.apply(b);
    }

    @Override
    public boolean isEmpty() {
        return underlying.isEmpty();
    }

    static <A, B> MappedFilter<A, B> compoundFilterChain(MapperChain mapperChain,
                                                         Filter<B> underlying) {
        return new MappedFilter<>(mapperChain, underlying);
    }

}
