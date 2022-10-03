package software.kes.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.gauntlet.util.MapperChain;

import static software.kes.gauntlet.filter.MultiFilter.multiFilter;

final class MappedFilter<A, B> implements Filter<A> {
    private final MapperChain mapperChain;
    private final Filter<B> underlying;

    private MappedFilter(MapperChain mapperChain, Filter<B> underlying) {
        this.mapperChain = mapperChain;
        this.underlying = underlying;
    }

    static <A, B> MappedFilter<A, B> mappedFilter(MapperChain mapperChain,
                                                  Filter<B> underlying) {
        return new MappedFilter<>(mapperChain, underlying);
    }

    @Override
    public Filter<A> add(Fn1<? super A, Boolean> predicate) {
        return multiFilter(this).add(predicate);
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
}
