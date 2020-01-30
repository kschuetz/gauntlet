package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.ValueSupply;

import static dev.marksman.gauntlet.GeneratorOutput.generatorOutput;

final class UnfilteredValueSupply {

    static <A> Iterable<GeneratorOutput<A>> unfilteredValueSupply(ValueSupply<A> valueSupply) {
        return valueSupply.fmap(item -> generatorOutput(item, 0));
    }
}
