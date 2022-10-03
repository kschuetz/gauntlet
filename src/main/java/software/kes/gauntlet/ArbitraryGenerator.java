package software.kes.gauntlet;

import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Generators;

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
        return ARBITRARY_GENERATOR.labeled("arbitrary");
    }

}
