package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.gauntlet.GauntletApiBase;
import dev.marksman.kraftwerk.Generator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testsupport.shrink.ShrinkTestCase;

import static dev.marksman.gauntlet.prop.Props.allOf;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.*;
import static dev.marksman.kraftwerk.Generators.*;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static testsupport.shrink.ShrinkTestCase.*;

class ShrinkNumericsTest extends GauntletApiBase {

    private static Generator<Tuple2<Integer, Integer>> generateIntSpan =
            generateOrderedPair(generateInt());

    // For the shrink input, we want most values to be in the domain, but occasionally exercise it outside of the domain
    private static Generator<Integer> generateMostlyInDomain(int min, int max) {
        return frequencyMap(1, generateInt())
                .add(3, generateInt(min, max))
                .toGenerator();
    }

    @Nested
    @DisplayName("ints")
    class Ints {

        @Test
        void unclamped() {
            all(shrinkTestCases(generateInt(), shrinkInt()))
                    .mustSatisfy(neverRepeatsAnElement());
        }

        @Test
        void clamped() {
            all(clampedShrinkTestCases(generateIntSpan, ShrinkNumericsTest::generateMostlyInDomain, ShrinkNumerics::shrinkInt))
                    .mustSatisfy(allOf(
                            ShrinkTestCase.<Integer>neverRepeatsAnElement(),
                            allElementsWithinDomain(),
                            shrinkOutputEmptyWhenInputOutsideOfDomain()
                    ));
        }

    }

    @Nested
    @DisplayName("longs")
    class Longs {

        @Test
        void unclamped() {
            all(shrinkTestCases(generateLong(), shrinkLong()))
                    .mustSatisfy(neverRepeatsAnElement());
        }

    }

    @Nested
    @DisplayName("shorts")
    class Shorts {

        @Test
        void unclamped() {
            all(shrinkTestCases(generateShort(), shrinkShort()))
                    .mustSatisfy(neverRepeatsAnElement());
        }

    }

    @Nested
    @DisplayName("bytes")
    class Bytes {

        @Test
        void unclamped() {
            all(shrinkTestCases(generateByte(), shrinkByte()))
                    .mustSatisfy(neverRepeatsAnElement());
        }

    }

}