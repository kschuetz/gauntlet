package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class CollectionValueSupplier<A, Builder, Out> implements ValueSupplier<Out> {
    private final ValueSupplier<A> elementSupplier;
    private final Generate<Integer> sizeGenerator;
    private final Fn0<Builder> initialBuilderSupplier;
    private final Fn2<Builder, A, Builder> addFn;
    private final Fn1<Builder, Out> buildFn;

    CollectionValueSupplier(ValueSupplier<A> elementSupplier, Generate<Integer> sizeGenerator, Fn0<Builder> initialBuilderSupplier, Fn2<Builder, A, Builder> addFn, Fn1<Builder, Out> buildFn) {
        this.elementSupplier = elementSupplier;
        this.sizeGenerator = sizeGenerator;
        this.initialBuilderSupplier = initialBuilderSupplier;
        this.addFn = addFn;
        this.buildFn = buildFn;
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        Result<? extends Seed, Integer> sizeResult = sizeGenerator.apply(input);
        Seed state = sizeResult.getNextState();
        int size = sizeResult.getValue();
        Builder builder = initialBuilderSupplier.apply();

        while (size > 0) {
            GeneratorOutput<A> current = elementSupplier.getNext(state);
            state = current.getNextState();
            if (current.isFailure()) {
                return GeneratorOutput.failure(state, SupplyFailure.supplyFailure(getSupplyTree()));
            }
            builder = addFn.apply(builder, current.getValue().orThrow(AssertionError::new));
            size--;
        }

        Out output = buildFn.apply(builder);
        return GeneratorOutput.success(state, output);
    }

    @Override
    public SupplyTree getSupplyTree() {
        return null;
    }
}
