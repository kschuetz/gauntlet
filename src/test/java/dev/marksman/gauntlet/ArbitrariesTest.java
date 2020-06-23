package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.constraints.Constraint;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.Arbitraries.bigDecimals;
import static dev.marksman.gauntlet.Arbitraries.bigIntegers;
import static dev.marksman.gauntlet.Arbitraries.bytes;
import static dev.marksman.gauntlet.Arbitraries.doubleFractionals;
import static dev.marksman.gauntlet.Arbitraries.doubles;
import static dev.marksman.gauntlet.Arbitraries.durations;
import static dev.marksman.gauntlet.Arbitraries.floatFractionals;
import static dev.marksman.gauntlet.Arbitraries.floats;
import static dev.marksman.gauntlet.Arbitraries.intNaturals;
import static dev.marksman.gauntlet.Arbitraries.ints;
import static dev.marksman.gauntlet.Arbitraries.localDateTimes;
import static dev.marksman.gauntlet.Arbitraries.localDates;
import static dev.marksman.gauntlet.Arbitraries.localTimes;
import static dev.marksman.gauntlet.Arbitraries.longNaturals;
import static dev.marksman.gauntlet.Arbitraries.longs;
import static dev.marksman.gauntlet.Arbitraries.nonEmptyVectors;
import static dev.marksman.gauntlet.Arbitraries.seeds;
import static dev.marksman.gauntlet.Arbitraries.shortNaturals;
import static dev.marksman.gauntlet.Arbitraries.shorts;
import static dev.marksman.gauntlet.Gauntlet.gauntlet;
import static dev.marksman.gauntlet.TestParametersSource.generateParametersForTests;
import static dev.marksman.kraftwerk.Generators.generateBigDecimalRange;
import static dev.marksman.kraftwerk.Generators.generateBigIntegerRange;
import static dev.marksman.kraftwerk.Generators.generateByteRange;
import static dev.marksman.kraftwerk.Generators.generateDoubleRange;
import static dev.marksman.kraftwerk.Generators.generateDurationRange;
import static dev.marksman.kraftwerk.Generators.generateFloatRange;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateIntRange;
import static dev.marksman.kraftwerk.Generators.generateLocalDateRange;
import static dev.marksman.kraftwerk.Generators.generateLocalDateTimeRange;
import static dev.marksman.kraftwerk.Generators.generateLocalTimeRange;
import static dev.marksman.kraftwerk.Generators.generateLongRange;
import static dev.marksman.kraftwerk.Generators.generateShortRange;
import static testsupport.TestSupportProps.equivalentSuppliesForSeed;

final class ArbitrariesTest extends GauntletApiBase {

    @Override
    protected GauntletApi initializeGauntletApi() {
        return gauntlet().withDefaultSampleCount(200);
    }

    @Nested
    @DisplayName("ints")
    class Ints {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateIntRange()),
                    range -> all(ints(range)).satisfy(inRange(range)));
        }

        @Test
        void allNaturalsInRange() {
            assertThat(all(intNaturals()).satisfy(inRange(IntRange.inclusive(0, Integer.MAX_VALUE))));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateIntRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> ints(), Arbitraries::ints).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> ints(), Arbitraries::ints).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("longs")
    class Longs {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateLongRange()),
                    range -> all(longs(range)).satisfy(inRange(range)));
        }

        @Test
        void allNaturalsInRange() {
            assertThat(all(longNaturals()).satisfy(inRange(LongRange.inclusive(0, Long.MAX_VALUE))));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateLongRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> longs(), Arbitraries::longs).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> longs(), Arbitraries::longs).createSupply(getGeneratorParameters()))));
        }
    }


    @Nested
    @DisplayName("shorts")
    class Shorts {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateShortRange()),
                    range -> all(shorts(range)).satisfy(inRange(range)));
        }

        @Test
        void allNaturalsInRange() {
            assertThat(all(shortNaturals()).satisfy(inRange(ShortRange.inclusive((short) 0, Short.MAX_VALUE))));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateShortRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> shorts(), Arbitraries::shorts).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> shorts(), Arbitraries::shorts).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("bytes")
    class Bytes {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateByteRange()),
                    range -> all(bytes(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateByteRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> bytes(), Arbitraries::bytes).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> bytes(), Arbitraries::bytes).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("doubles")
    class Doubles {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateDoubleRange()),
                    range -> all(doubles(range)).satisfy(inRange(range)));
        }

        @Test
        void fractionalsInRange() {
            assertThat(all(doubleFractionals()).satisfy(inRange(DoubleRange.exclusive(1f))));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateDoubleRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> doubles(), Arbitraries::doubles).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> doubles(), Arbitraries::doubles).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("floats")
    class Floats {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateFloatRange()),
                    range -> all(floats(range)).satisfy(inRange(range)));
        }

        @Test
        void fractionalsInRange() {
            assertThat(all(floatFractionals()).satisfy(inRange(FloatRange.exclusive(1f))));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateFloatRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> floats(), Arbitraries::floats).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> floats(), Arbitraries::floats).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("bigIntegers")
    class BigIntegers {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateBigIntegerRange()),
                    range -> all(bigIntegers(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateBigIntegerRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> bigIntegers(), Arbitraries::bigIntegers).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> bigIntegers(), Arbitraries::bigIntegers).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("bigDecimals")
    class BigDecimals {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateBigDecimalRange()),
                    range -> all(bigDecimals(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateBigDecimalRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> bigDecimals(), Arbitraries::bigDecimals).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> bigDecimals(), Arbitraries::bigDecimals).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("localDates")
    class LocalDates {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateLocalDateRange()),
                    range -> all(localDates(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateLocalDateRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> localDates(), Arbitraries::localDates).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> localDates(), Arbitraries::localDates).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("localTimes")
    class LocalTimes {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateLocalTimeRange()),
                    range -> all(localTimes(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateLocalTimeRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> localTimes(), Arbitraries::localTimes).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> localTimes(), Arbitraries::localTimes).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("localDateTimes")
    class LocalDateTimes {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateLocalDateTimeRange()),
                    range -> all(localDateTimes(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateLocalDateTimeRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> localDateTimes(), Arbitraries::localDateTimes).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> localDateTimes(), Arbitraries::localDateTimes).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("durations")
    class Durations {
        @Test
        void allInRange() {
            assertForEach(generateParametersForTests(generateDurationRange()),
                    range -> all(durations(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            assertForEach(generateParametersForTests(generateDurationRange().maybe()),
                    maybeRange -> all(seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> durations(), Arbitraries::durations).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> durations(), Arbitraries::durations).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("nonEmptyVectors")
    class NonEmptyVectors {
        @Test
        void haveAtLeastOneElement() {
            assertThat(all(nonEmptyVectors()).satisfy(Prop.predicate("non-empty", nev -> !nev.isEmpty())));
        }

        @Test
        void haveExactSizeRequested() {
            assertForEach(generateParametersForTests(generateInt(IntRange.from(1).to(10))),
                    size -> all(nonEmptyVectors(size)).satisfy(Prop.predicate("has requested size", nev -> nev.size() == size)));
        }

        @Test
        void haveSizeWithinRequestedRange() {
            assertForEach(generateParametersForTests(generateIntRange(IntRange.from(1).to(10))),
                    sizeRange -> all(nonEmptyVectors(sizeRange)).satisfy(Prop.predicate("has requested size", nev -> sizeRange.includes(nev.size()))));
        }
    }

    private <A> Prop<A> inRange(Constraint<A> range) {
        return Prop.predicate("in range", range::includes);
    }
}