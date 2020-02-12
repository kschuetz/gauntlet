package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.util.MapperChain;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class MappedValueSupplier<A, B> implements ValueSupplier<B> {
    private final ValueSupplier<A> underlying;
    private final MapperChain mapperChain;

    MappedValueSupplier(ValueSupplier<A> underlying, MapperChain mapperChain) {
        this.underlying = underlying;
        this.mapperChain = mapperChain;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result<Seed, Maybe<B>> getNext(Seed input) {
        return underlying.getNext(input)
                .fmap(x -> (Maybe<B>) x.fmap(mapperChain.getFn()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B1> ValueSupplier<B1> fmap(Fn1<? super B, ? extends B1> fn) {
        return new MappedValueSupplier<>(underlying, mapperChain.append((Fn1<Object, Object>) fn));
    }

    @SuppressWarnings("unchecked")
    static <A, B> ValueSupplier<B> mappedValueSupplier(Fn1<? super A, ? extends B> fn,
                                                       ValueSupplier<A> underlying) {
        return new MappedValueSupplier<>(underlying, MapperChain.mapperChain((Fn1<Object, Object>) fn));
    }
}
