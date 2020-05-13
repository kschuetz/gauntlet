package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.util.MapperChain;
import dev.marksman.kraftwerk.Seed;

final class MappedSupplyStrategy<A, B> implements SupplyStrategy<B> {
    private final SupplyStrategy<A> underlying;
    private final MapperChain mapperChain;

    MappedSupplyStrategy(SupplyStrategy<A> underlying, MapperChain mapperChain) {
        this.underlying = underlying;
        this.mapperChain = mapperChain;
    }

    @SuppressWarnings("unchecked")
    static <A, B> SupplyStrategy<B> mappedSupply(Fn1<? super A, ? extends B> fn,
                                                 SupplyStrategy<A> underlying) {
        return new MappedSupplyStrategy<>(underlying, MapperChain.mapperChain((Fn1<Object, Object>) fn));
    }

    @Override
    public StatefulSupply<B> createSupply() {
        return new MappedSupply(underlying.createSupply());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return underlying.getSupplyTree();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B1> SupplyStrategy<B1> fmap(Fn1<? super B, ? extends B1> fn) {
        return new MappedSupplyStrategy<>(underlying, mapperChain.append((Fn1<Object, Object>) fn));
    }

    class MappedSupply implements StatefulSupply<B> {
        private final StatefulSupply<A> underlying;

        MappedSupply(StatefulSupply<A> underlying) {
            this.underlying = underlying;
        }

        @SuppressWarnings("unchecked")
        @Override
        public GeneratorOutput<B> getNext(Seed input) {
            return underlying.getNext(input)
                    .fmap(a -> (B) mapperChain.getFn().apply(a));
        }
    }
}
