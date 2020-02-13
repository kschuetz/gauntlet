package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Seed;

final class UnfilteredValueSupplier<A> implements ValueSupplier<A> {
    private final Generate<A> generateFn;

    UnfilteredValueSupplier(Generate<A> generateFn) {
        this.generateFn = generateFn;
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        return GeneratorOutput.success(generateFn.apply(input));
    }
}
