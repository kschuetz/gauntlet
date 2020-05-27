package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

class HomogeneousCollectionSupplyStrategy implements SupplyStrategy<ImmutableVector<?>> {
    private final SupplyStrategy<Arbitrary<?>> arbitrarySupply;
    private final Generate<Integer> sizeGenerator;
    private final GeneratorParameters generatorParameters;

    HomogeneousCollectionSupplyStrategy(SupplyStrategy<Arbitrary<?>> arbitrarySupply,
                                        Generate<Integer> sizeGenerator,
                                        GeneratorParameters generatorParameters) {
        this.arbitrarySupply = arbitrarySupply;
        this.sizeGenerator = sizeGenerator;
        this.generatorParameters = generatorParameters;
    }

    @Override
    public StatefulSupply<ImmutableVector<?>> createSupply() {
        return new HomogeneousCollectionSupply(arbitrarySupply.createSupply());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return arbitrarySupply.getSupplyTree();
    }

    class HomogeneousCollectionSupply implements StatefulSupply<ImmutableVector<?>> {
        private final StatefulSupply<Arbitrary<?>> arbitrarySupply;

        HomogeneousCollectionSupply(StatefulSupply<Arbitrary<?>> arbitrarySupply) {
            this.arbitrarySupply = arbitrarySupply;
        }

        public GeneratorOutput<ImmutableVector<?>> getNext(Seed input) {
            Result<? extends Seed, Integer> sizeResult = sizeGenerator.apply(input);
            Seed state = sizeResult.getNextState();
            int size = sizeResult.getValue();

            GeneratorOutput<Arbitrary<?>> arbitraryOutput = arbitrarySupply.getNext(state);
            if (arbitraryOutput.isFailure()) {
                return GeneratorOutput.failure(state, arbitraryOutput.getValue().projectA().orElseThrow(AssertionError::new));
            }
            state = arbitraryOutput.getNextState();
            Arbitrary<?> arbitrary = arbitraryOutput.getValue().orThrow(AssertionError::new);
            StatefulSupply<?> elementSupply = arbitrary.supplyStrategy(generatorParameters).createSupply();
            VectorBuilder<Object> builder = Vector.builder();

            while (size > 0) {
                GeneratorOutput<?> current = elementSupply.getNext(state);
                state = current.getNextState();
                if (current.isFailure()) {
                    return GeneratorOutput.failure(state, current.getValue().projectA().orElseThrow(AssertionError::new));
                }

                builder = builder.add(current.getValue().orThrow(AssertionError::new));
                size--;
            }

            ImmutableVector<?> output = builder.build();
            return GeneratorOutput.success(state, output);
        }
    }
}
