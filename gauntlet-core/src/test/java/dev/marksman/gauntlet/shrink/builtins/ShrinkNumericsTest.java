package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.gauntlet.GauntletApiBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkInt;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static testsupport.shrink.ShrinkTestCase.neverRepeatsAnElement;
import static testsupport.shrink.ShrinkTestCase.shrinkTestCases;

class ShrinkNumericsTest extends GauntletApiBase {

    @Nested
    @DisplayName("ints")
    class Ints {

        @Test
        @Disabled
        void neverRepeats() {
            all(shrinkTestCases(generateInt(), shrinkInt()))
                    .mustSatisfy(neverRepeatsAnElement());
        }

    }

}