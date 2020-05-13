package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Seed;

class LayeredSupplyStrategy<Inner, Outer> implements SupplyStrategy<Tuple2<Inner, Outer>> {
    private final SupplyStrategy<Arbitrary<Outer>> outerSupply;
    private final int sampleCount;
    private final GeneratorParameters generatorParameters;

    LayeredSupplyStrategy(SupplyStrategy<Arbitrary<Outer>> outerSupply, int sampleCount, GeneratorParameters generatorParameters) {
        this.outerSupply = outerSupply;
        this.sampleCount = sampleCount;
        this.generatorParameters = generatorParameters;
    }

    @Override
    public StatefulSupply<Tuple2<Inner, Outer>> createSupply() {
        return new LayeredSupply();
    }

    @Override
    public SupplyTree getSupplyTree() {
        throw new UnsupportedOperationException("TODO");
    }

    class LayeredSupply implements StatefulSupply<Tuple2<Inner, Outer>> {
        @Override
        public GeneratorOutput<Tuple2<Inner, Outer>> getNext(Seed input) {
            throw new UnsupportedOperationException("TODO");
        }
    }
}
