package software.kes.gauntlet.filter;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.gauntlet.util.MapperChain;

final class MultiFilter<A> implements Filter<A> {
    private final ImmutableFiniteIterable<Fn1<? super A, Boolean>> filters;

    private MultiFilter(ImmutableFiniteIterable<Fn1<? super A, Boolean>> filters) {
        this.filters = filters;
    }

    static <A> Filter<A> multiFilter(Fn1<? super A, Boolean> filter) {
        return new MultiFilter<>(ImmutableFiniteIterable.of(filter));
    }

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
        return MappedFilter.mappedFilter(MapperChain.mapperChain((Fn1<Object, Object>) fn), this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
