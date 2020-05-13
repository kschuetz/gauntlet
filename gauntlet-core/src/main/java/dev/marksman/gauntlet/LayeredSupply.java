package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Seed;

class LayeredSupply<Inner, Outer> implements Supply<Tuple2<Inner, Outer>> {
    private final Supply<Arbitrary<Outer>> outerSupply;
    private final int sampleCount;
    private final GeneratorParameters generatorParameters;

    LayeredSupply(Supply<Arbitrary<Outer>> outerSupply, int sampleCount, GeneratorParameters generatorParameters) {
        this.outerSupply = outerSupply;
        this.sampleCount = sampleCount;
        this.generatorParameters = generatorParameters;
    }

    @Override
    public GeneratorOutput<Tuple2<Inner, Outer>> getNext(Seed input) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public SupplyTree getSupplyTree() {
        return null;
    }
}
