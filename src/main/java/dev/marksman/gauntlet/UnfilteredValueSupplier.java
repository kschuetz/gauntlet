package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Seed;

final class UnfilteredValueSupplier<A> implements ValueSupplier<A> {
    private final Generate<A> generateFn;
    private final Fn0<String> labelSupplier;

    UnfilteredValueSupplier(Generate<A> generateFn, Fn0<String> labelSupplier) {
        this.generateFn = generateFn;
        this.labelSupplier = labelSupplier;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.leaf(labelSupplier.apply());
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        return GeneratorOutput.success(generateFn.apply(input));
    }
}
