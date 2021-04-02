package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.gauntlet.GauntletApiBase;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testsupport.shrink.ShrinkStrategyTestCase;

import static dev.marksman.gauntlet.Prop.allOf;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkByte;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkInt;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkLong;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkShort;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateByteRange;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateIntRange;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateLongRange;
import static dev.marksman.kraftwerk.Generators.generateShort;
import static dev.marksman.kraftwerk.Generators.generateShortRange;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static testsupport.shrink.ShrinkStrategyTestCase.allElementsWithinDomain;
import static testsupport.shrink.ShrinkStrategyTestCase.constrainedShrinkTestCase;
import static testsupport.shrink.ShrinkStrategyTestCase.neverRepeatsAnElement;
import static testsupport.shrink.ShrinkStrategyTestCase.shrinkOutputEmptyWhenInputOutsideOfDomain;
import static testsupport.shrink.ShrinkStrategyTestCase.shrinkTestCases;

final class ShrinkNumericsTest extends GauntletApiBase {
    @Nested
    @DisplayName("ints")
    class Ints {
        @Test
        void unclamped() {
            checkThat(all(shrinkTestCases(generateInt(), shrinkInt()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            // TODO:
            // java.lang.AssertionError: Failed property 'never repeats an element ∧ all elements within domain ∧ when input is outside of shrink domain, shrink output is empty' with value 'ShrinkTestCase(input=-2147483648, output=dev.marksman.gauntlet.shrink.LazyCons$1@30272916, min=-2147483648, max=1413962530)'. reasons: FailureReasons(items=Vector(Conjuncts failed.))

            checkThat(all(constrainedShrinkTestCase(generateIntRange(),
                    ShrinkNumericsTest::generateIntMostlyInDomain,
                    ShrinkNumerics::shrinkInt))
                    .satisfy(allOf(
                            ShrinkStrategyTestCase.<Integer>neverRepeatsAnElement(),
                            allElementsWithinDomain(),
                            shrinkOutputEmptyWhenInputOutsideOfDomain()
                    )));
        }
    }

    @Nested
    @DisplayName("longs")
    class Longs {
        @Test
        void unclamped() {
            checkThat(all(shrinkTestCases(generateLong(), shrinkLong()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            checkThat(all(constrainedShrinkTestCase(generateLongRange(),
                    ShrinkNumericsTest::generateLongMostlyInDomain,
                    ShrinkNumerics::shrinkLong))
                    .satisfy(allOf(
                            ShrinkStrategyTestCase.<Long>neverRepeatsAnElement(),
                            allElementsWithinDomain(),
                            shrinkOutputEmptyWhenInputOutsideOfDomain()
                    )));
        }
    }

    @Nested
    @DisplayName("shorts")
    class Shorts {
        @Test
        void unclamped() {
            checkThat(all(shrinkTestCases(generateShort(), shrinkShort()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            checkThat(all(constrainedShrinkTestCase(generateShortRange(),
                    ShrinkNumericsTest::generateShortMostlyInDomain,
                    ShrinkNumerics::shrinkShort))
                    .satisfy(allOf(
                            ShrinkStrategyTestCase.<Short>neverRepeatsAnElement(),
                            allElementsWithinDomain(),
                            shrinkOutputEmptyWhenInputOutsideOfDomain()
                    )));
        }
    }

    @Nested
    @DisplayName("bytes")
    class Bytes {
        @Test
        void unclamped() {
            checkThat(all(shrinkTestCases(generateByte(), shrinkByte()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            checkThat(all(constrainedShrinkTestCase(generateByteRange(),
                    ShrinkNumericsTest::generateByteMostlyInDomain,
                    ShrinkNumerics::shrinkByte))
                    .satisfy(allOf(
                            ShrinkStrategyTestCase.<Byte>neverRepeatsAnElement(),
                            allElementsWithinDomain(),
                            shrinkOutputEmptyWhenInputOutsideOfDomain()
                    )));
        }
    }


    // For the shrink input, we want most values to be in the domain, but occasionally exercise it outside of the domain
    private static Generator<Integer> generateIntMostlyInDomain(IntRange range) {
        return frequencyMap(generateInt().weighted(1))
                .add(generateInt(range).weighted(3))
                .toGenerator();
    }

    private static Generator<Long> generateLongMostlyInDomain(LongRange range) {
        return frequencyMap(generateLong().weighted(1))
                .add(generateLong(range).weighted(3))
                .toGenerator();
    }

    private static Generator<Short> generateShortMostlyInDomain(ShortRange range) {
        return frequencyMap(generateShort().weighted(1))
                .add(generateShort(range).weighted(3))
                .toGenerator();
    }

    private static Generator<Byte> generateByteMostlyInDomain(ByteRange range) {
        return frequencyMap(generateByte().weighted(1))
                .add(generateByte(range).weighted(3))
                .toGenerator();
    }
}
