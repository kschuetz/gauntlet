package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.aggregator.Aggregator;

import static dev.marksman.gauntlet.SupplyTree.collection;

final class CollectionSupply<A, Builder, Out> implements Supply<Out> {
    private final Supply<A> elementSupplier;
    private final Generate<Integer> sizeGenerator;
    private final Aggregator<A, Builder, Out> aggregator;

    CollectionSupply(Supply<A> elementSupplier, Generate<Integer> sizeGenerator, Aggregator<A, Builder, Out> aggregator) {
        this.elementSupplier = elementSupplier;
        this.sizeGenerator = sizeGenerator;
        this.aggregator = aggregator;
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        Result<? extends Seed, Integer> sizeResult = sizeGenerator.apply(input);
        Seed state = sizeResult.getNextState();
        int size = sizeResult.getValue();
        Builder builder = aggregator.builder();

        while (size > 0) {
            GeneratorOutput<A> current = elementSupplier.getNext(state);
            state = current.getNextState();
            if (current.isFailure()) {
                return GeneratorOutput.failure(state, current.getValue().projectA().orElseThrow(AssertionError::new));
            }
            builder = aggregator.add(builder, current.getValue().orThrow(AssertionError::new));
            size--;
        }

        Out output = aggregator.build(builder);
        return GeneratorOutput.success(state, output);
    }

    @Override
    public SupplyTree getSupplyTree() {
        return collection(elementSupplier.getSupplyTree());
    }
}
