package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.SupplyTree.leaf;

final class UnfilteredSupplyStrategy<A> implements SupplyStrategy<A>, StatefulSupply<A> {
    private final Generate<A> generateFn;
    private final Fn0<String> labelSupplier;

    UnfilteredSupplyStrategy(Generate<A> generateFn, Fn0<String> labelSupplier) {
        this.generateFn = generateFn;
        this.labelSupplier = labelSupplier;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return leaf(labelSupplier.apply());
    }

    @Override
    public StatefulSupply<A> createSupply() {
        return this;
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        return GeneratorOutput.success(generateFn.apply(input));
    }
}
