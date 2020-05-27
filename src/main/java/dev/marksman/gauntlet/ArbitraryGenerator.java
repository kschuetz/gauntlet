package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;

public final class ArbitraryGenerator {

    private static final Generator<Arbitrary<?>> ARBITRARY_GENERATOR = Generators.chooseOneOfValues(Arbitraries.ints(),
            Arbitraries.strings(),
            Arbitraries.doubles(),
            Arbitraries.floats(),
            Arbitraries.shorts(),
            Arbitraries.bytes(),
            Arbitraries.booleans(),
            Arbitraries.bigDecimals(),
            Arbitraries.bigIntegers(),
            Arbitraries.localDates(),
            Arbitraries.localTimes(),
            Arbitraries.localDateTimes());

    private ArbitraryGenerator() {

    }

    public static Generator<Arbitrary<?>> generateArbitrary() {
        return ARBITRARY_GENERATOR;
    }

}
