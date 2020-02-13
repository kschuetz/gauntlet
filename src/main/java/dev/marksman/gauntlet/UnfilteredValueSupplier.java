package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Seed;

final class UnfilteredValueSupplier<A> implements ValueSupplier<A> {
    private final Generate<A> generateFn;
    private final String generatorLabel;

    UnfilteredValueSupplier(Generate<A> generateFn, String generatorLabel) {
        this.generateFn = generateFn;
        this.generatorLabel = generatorLabel;
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        return GeneratorOutput.success(generateFn.apply(input));
    }
}
