package dev.marksman.gauntlet;

import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkInt;
import static dev.marksman.kraftwerk.Generators.generateInt;

public final class Arbitraries {

    private Arbitraries() {

    }

    public static Arbitrary<Integer> arbitraryInt() {
        return arbitrary(generateInt()).withShrink(shrinkInt());
    }

    public static Arbitrary<Integer> arbitraryInt(int min, int max) {
        return arbitrary(generateInt(min, max)).withShrink(shrinkInt());
    }


}
