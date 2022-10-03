package software.kes.gauntlet.shrink.builtins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.gauntlet.GauntletApiBase;
import software.kes.gauntlet.Prop;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.constraints.BigIntegerRange;
import software.kes.kraftwerk.constraints.ByteRange;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.constraints.LongRange;
import software.kes.kraftwerk.constraints.ShortRange;
import testsupport.shrink.ShrinkStrategyTestCase;

import java.math.BigInteger;

import static software.kes.kraftwerk.Generators.generateBigInteger;
import static software.kes.kraftwerk.Generators.generateBigIntegerRange;
import static software.kes.kraftwerk.Generators.generateByte;
import static software.kes.kraftwerk.Generators.generateByteRange;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateIntRange;
import static software.kes.kraftwerk.Generators.generateLong;
import static software.kes.kraftwerk.Generators.generateLongRange;
import static software.kes.kraftwerk.Generators.generateShort;
import static software.kes.kraftwerk.Generators.generateShortRange;
import static software.kes.kraftwerk.frequency.FrequencyMap.frequencyMap;
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
            checkThat(all(shrinkTestCases(generateInt(), ShrinkNumerics.shrinkInt()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            // TODO:
            // java.lang.AssertionError: Failed property 'never repeats an element ∧ all elements within domain ∧ when input is outside of shrink domain, shrink output is empty' with value 'ShrinkTestCase(input=-2147483648, output=dev.marksman.gauntlet.shrink.LazyCons$1@30272916, min=-2147483648, max=1413962530)'. reasons: FailureReasons(items=Vector(Conjuncts failed.))

            checkThat(all(constrainedShrinkTestCase(generateIntRange(),
                    ShrinkNumericsTest::generateIntMostlyInDomain,
                    ShrinkNumerics::shrinkInt))
                    .satisfy(Prop.allOf(
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
            checkThat(all(shrinkTestCases(generateLong(), ShrinkNumerics.shrinkLong()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            checkThat(all(constrainedShrinkTestCase(generateLongRange(),
                    ShrinkNumericsTest::generateLongMostlyInDomain,
                    ShrinkNumerics::shrinkLong))
                    .satisfy(Prop.allOf(
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
            checkThat(all(shrinkTestCases(generateShort(), ShrinkNumerics.shrinkShort()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            checkThat(all(constrainedShrinkTestCase(generateShortRange(),
                    ShrinkNumericsTest::generateShortMostlyInDomain,
                    ShrinkNumerics::shrinkShort))
                    .satisfy(Prop.allOf(
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
            checkThat(all(shrinkTestCases(generateByte(), ShrinkNumerics.shrinkByte()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            checkThat(all(constrainedShrinkTestCase(generateByteRange(),
                    ShrinkNumericsTest::generateByteMostlyInDomain,
                    ShrinkNumerics::shrinkByte))
                    .satisfy(Prop.allOf(
                            ShrinkStrategyTestCase.<Byte>neverRepeatsAnElement(),
                            allElementsWithinDomain(),
                            shrinkOutputEmptyWhenInputOutsideOfDomain()
                    )));
        }
    }

    @Nested
    @DisplayName("BigIntegers")
    class BigIntegers {
        @Test
        void unclamped() {
            checkThat(all(shrinkTestCases(generateBigInteger(), ShrinkStrategies.shrinkBigInteger()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        void clamped() {
            checkThat(all(constrainedShrinkTestCase(generateBigIntegerRange(),
                    ShrinkNumericsTest::generateBigIntegerMostlyInDomain,
                    ShrinkNumerics::shrinkBigInteger))
                    .satisfy(Prop.allOf(
                            ShrinkStrategyTestCase.<BigInteger>neverRepeatsAnElement(),
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

    private static Generator<BigInteger> generateBigIntegerMostlyInDomain(BigIntegerRange range) {
        return frequencyMap(generateBigInteger().weighted(1))
                .add(generateBigInteger(range).weighted(3))
                .toGenerator();
    }
}
