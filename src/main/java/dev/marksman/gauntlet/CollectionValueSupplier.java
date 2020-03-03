package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.aggregator.Aggregator;

final class CollectionValueSupplier<A, Builder, Out> implements ValueSupplier<Out> {
    private final ValueSupplier<A> elementSupplier;
    private final Generate<Integer> sizeGenerator;
    private final Aggregator<A, Builder, Out> aggregator;

    CollectionValueSupplier(ValueSupplier<A> elementSupplier, Generate<Integer> sizeGenerator, Aggregator<A, Builder, Out> aggregator) {
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
                return GeneratorOutput.failure(state, SupplyFailure.supplyFailure(getSupplyTree()));
            }
            builder = aggregator.add(builder, current.getValue().orThrow(AssertionError::new));
            size--;
        }

        Out output = aggregator.build(builder);
        return GeneratorOutput.success(state, output);
    }

    @Override
    public SupplyTree getSupplyTree() {
        return null;
    }
}
