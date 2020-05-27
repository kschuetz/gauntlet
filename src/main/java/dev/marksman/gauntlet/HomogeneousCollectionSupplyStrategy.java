package dev.marksman.gauntlet;

import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class HomogeneousCollectionSupplyStrategy implements SupplyStrategy<Vector<?>> {
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
    public StatefulSupply<Vector<?>> createSupply() {
        return new HomogeneousCollectionSupply(arbitrarySupply.createSupply());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return arbitrarySupply.getSupplyTree();
    }

    class HomogeneousCollectionSupply implements StatefulSupply<Vector<?>> {
        private final StatefulSupply<Arbitrary<?>> arbitrarySupply;

        HomogeneousCollectionSupply(StatefulSupply<Arbitrary<?>> arbitrarySupply) {
            this.arbitrarySupply = arbitrarySupply;
        }

        public GeneratorOutput<Vector<?>> getNext(Seed input) {
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

            Vector<?> output = builder.build();
            return GeneratorOutput.success(state, output);
        }
    }
}
