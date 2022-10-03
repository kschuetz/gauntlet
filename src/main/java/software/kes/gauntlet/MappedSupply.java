package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.gauntlet.util.MapperChain;
import software.kes.kraftwerk.Seed;

final class MappedSupply<A, B> implements Supply<B> {
    private final Supply<A> underlying;
    private final MapperChain mapperChain;

    MappedSupply(Supply<A> underlying, MapperChain mapperChain) {
        this.underlying = underlying;
        this.mapperChain = mapperChain;
    }

    @SuppressWarnings("unchecked")
    static <A, B> Supply<B> mappedSupply(Fn1<? super A, ? extends B> fn,
                                         Supply<A> underlying) {
        return new MappedSupply<>(underlying, MapperChain.mapperChain((Fn1<Object, Object>) fn));
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.mapping(underlying.getSupplyTree());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B1> Supply<B1> fmap(Fn1<? super B, ? extends B1> fn) {
        return new MappedSupply<>(underlying, mapperChain.append((Fn1<Object, Object>) fn));
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeneratorOutput<B> getNext(Seed input) {
        return underlying.getNext(input)
                .fmap(a -> (B) mapperChain.getFn().apply(a))
                .mapFailure(failure -> failure.modifySupplyTree(SupplyTree::mapping));
    }
}
