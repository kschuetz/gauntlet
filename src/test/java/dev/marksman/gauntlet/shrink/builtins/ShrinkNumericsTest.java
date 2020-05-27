package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.gauntlet.GauntletApiBase;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.IntRange;
import org.junit.jupiter.api.Disabled;
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
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateIntRange;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateOrderedPair;
import static dev.marksman.kraftwerk.Generators.generateShort;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static testsupport.shrink.ShrinkStrategyTestCase.allElementsWithinDomain;
import static testsupport.shrink.ShrinkStrategyTestCase.constrainedShrinkTestCase;
import static testsupport.shrink.ShrinkStrategyTestCase.neverRepeatsAnElement;
import static testsupport.shrink.ShrinkStrategyTestCase.shrinkOutputEmptyWhenInputOutsideOfDomain;
import static testsupport.shrink.ShrinkStrategyTestCase.shrinkTestCases;

final class ShrinkNumericsTest extends GauntletApiBase {
    private static final Generator<Tuple2<Integer, Integer>> generateIntSpan =
            generateOrderedPair(generateInt());

    // For the shrink input, we want most values to be in the domain, but occasionally exercise it outside of the domain
    private static Generator<Integer> generateMostlyInDomain(IntRange range) {
        return frequencyMap(generateInt().weighted(1))
                .add(generateInt(range).weighted(3))
                .toGenerator();
    }

    @Nested
    @DisplayName("ints")
    class Ints {
        @Test
        void unclamped() {
            assertThat(all(shrinkTestCases(generateInt(), shrinkInt()))
                    .satisfy(neverRepeatsAnElement()));
        }

        @Test
        @Disabled
        void clamped() {
            // TODO:
            // java.lang.AssertionError: Failed property 'never repeats an element ∧ all elements within domain ∧ when input is outside of shrink domain, shrink output is empty' with value 'ShrinkTestCase(input=-2147483648, output=dev.marksman.gauntlet.shrink.LazyCons$1@30272916, min=-2147483648, max=1413962530)'. reasons: FailureReasons(items=Vector(Conjuncts failed.))

            assertThat(all(constrainedShrinkTestCase(generateIntRange(),
                    ShrinkNumericsTest::generateMostlyInDomain,
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
            assertThat(all(shrinkTestCases(generateLong(), shrinkLong()))
                    .satisfy(neverRepeatsAnElement()));
        }
    }

    @Nested
    @DisplayName("shorts")
    class Shorts {
        @Test
        void unclamped() {
            assertThat(all(shrinkTestCases(generateShort(), shrinkShort()))
                    .satisfy(neverRepeatsAnElement()));
        }
    }

    @Nested
    @DisplayName("bytes")
    class Bytes {
        @Test
        void unclamped() {
            assertThat(all(shrinkTestCases(generateByte(), shrinkByte()))
                    .satisfy(neverRepeatsAnElement()));
        }
    }
}
