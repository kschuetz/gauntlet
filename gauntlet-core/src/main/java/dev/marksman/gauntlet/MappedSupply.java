package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.util.MapperChain;
import dev.marksman.kraftwerk.Seed;

final class MappedSupply<A, B> implements Supply<B> {
    private final Supply<A> underlying;
    private final MapperChain mapperChain;

    MappedSupply(Supply<A> underlying, MapperChain mapperChain) {
        this.underlying = underlying;
        this.mapperChain = mapperChain;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return underlying.getSupplyTree();
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeneratorOutput<B> getNext(Seed input) {
        return underlying.getNext(input)
                .fmap(a -> (B) mapperChain.getFn().apply(a));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B1> Supply<B1> fmap(Fn1<? super B, ? extends B1> fn) {
        return new MappedSupply<>(underlying, mapperChain.append((Fn1<Object, Object>) fn));
    }

    @SuppressWarnings("unchecked")
    static <A, B> Supply<B> mappedSupply(Fn1<? super A, ? extends B> fn,
                                         Supply<A> underlying) {
        return new MappedSupply<>(underlying, MapperChain.mapperChain((Fn1<Object, Object>) fn));
    }
}
