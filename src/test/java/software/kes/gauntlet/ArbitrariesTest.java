package software.kes.gauntlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.kraftwerk.constraints.Constraint;
import software.kes.kraftwerk.constraints.DoubleRange;
import software.kes.kraftwerk.constraints.FloatRange;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.constraints.LongRange;
import software.kes.kraftwerk.constraints.ShortRange;

import static software.kes.gauntlet.Arbitraries.bigDecimals;
import static software.kes.gauntlet.Arbitraries.bigIntegers;
import static software.kes.gauntlet.Arbitraries.bytes;
import static software.kes.gauntlet.Arbitraries.doubles;
import static software.kes.gauntlet.Arbitraries.floats;
import static software.kes.gauntlet.Arbitraries.ints;
import static software.kes.gauntlet.Arbitraries.localDateTimes;
import static software.kes.gauntlet.Arbitraries.longs;
import static software.kes.gauntlet.Arbitraries.nonEmptyVectors;
import static software.kes.gauntlet.Arbitraries.shorts;
import static software.kes.gauntlet.TestParametersSource.generateParametersForTests;
import static software.kes.kraftwerk.Generators.generateBigDecimalRange;
import static software.kes.kraftwerk.Generators.generateBigIntegerRange;
import static software.kes.kraftwerk.Generators.generateByteRange;
import static software.kes.kraftwerk.Generators.generateDoubleRange;
import static software.kes.kraftwerk.Generators.generateDurationRange;
import static software.kes.kraftwerk.Generators.generateFloatRange;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateIntRange;
import static software.kes.kraftwerk.Generators.generateLocalDateRange;
import static software.kes.kraftwerk.Generators.generateLocalDateTimeRange;
import static software.kes.kraftwerk.Generators.generateLocalTimeRange;
import static software.kes.kraftwerk.Generators.generateLongRange;
import static software.kes.kraftwerk.Generators.generateShortRange;
import static testsupport.TestSupportProps.equivalentSuppliesForSeed;

final class ArbitrariesTest extends GauntletApiBase {

    @Override
    protected GauntletApi initializeGauntletApi() {
        return Gauntlet.gauntlet().withDefaultSampleCount(200);
    }

    @Nested
    @DisplayName("ints")
    class Ints {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateIntRange()),
                    range -> all(ints(range)).satisfy(inRange(range)));
        }

        @Test
        void allNaturalsInRange() {
            checkThat(all(Arbitraries.intNaturals()).satisfy(inRange(IntRange.inclusive(0, Integer.MAX_VALUE))));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateIntRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.ints(), Arbitraries::ints).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.ints(), Arbitraries::ints).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("longs")
    class Longs {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateLongRange()),
                    range -> all(longs(range)).satisfy(inRange(range)));
        }

        @Test
        void allNaturalsInRange() {
            checkThat(all(Arbitraries.longNaturals()).satisfy(inRange(LongRange.inclusive(0, Long.MAX_VALUE))));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateLongRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.longs(), Arbitraries::longs).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.longs(), Arbitraries::longs).createSupply(getGeneratorParameters()))));
        }
    }


    @Nested
    @DisplayName("shorts")
    class Shorts {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateShortRange()),
                    range -> all(shorts(range)).satisfy(inRange(range)));
        }

        @Test
        void allNaturalsInRange() {
            checkThat(all(Arbitraries.shortNaturals()).satisfy(inRange(ShortRange.inclusive((short) 0, Short.MAX_VALUE))));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateShortRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.shorts(), Arbitraries::shorts).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.shorts(), Arbitraries::shorts).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("bytes")
    class Bytes {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateByteRange()),
                    range -> all(bytes(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateByteRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.bytes(), Arbitraries::bytes).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.bytes(), Arbitraries::bytes).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("doubles")
    class Doubles {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateDoubleRange()),
                    range -> all(doubles(range)).satisfy(inRange(range)));
        }

        @Test
        void fractionalsInRange() {
            checkThat(all(Arbitraries.doubleFractionals()).satisfy(inRange(DoubleRange.exclusive(1f))));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateDoubleRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.doubles(), Arbitraries::doubles).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.doubles(), Arbitraries::doubles).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("floats")
    class Floats {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateFloatRange()),
                    range -> all(floats(range)).satisfy(inRange(range)));
        }

        @Test
        void fractionalsInRange() {
            checkThat(all(Arbitraries.floatFractionals()).satisfy(inRange(FloatRange.exclusive(1f))));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateFloatRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.floats(), Arbitraries::floats).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.floats(), Arbitraries::floats).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("bigIntegers")
    class BigIntegers {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateBigIntegerRange()),
                    range -> all(bigIntegers(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateBigIntegerRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.bigIntegers(), Arbitraries::bigIntegers).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.bigIntegers(), Arbitraries::bigIntegers).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("bigDecimals")
    class BigDecimals {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateBigDecimalRange()),
                    range -> all(bigDecimals(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateBigDecimalRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.bigDecimals(), Arbitraries::bigDecimals).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.bigDecimals(), Arbitraries::bigDecimals).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("localDates")
    class LocalDates {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateLocalDateRange()),
                    range -> all(Arbitraries.localDates(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateLocalDateRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.localDates(), Arbitraries::localDates).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.localDates(), Arbitraries::localDates).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("localTimes")
    class LocalTimes {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateLocalTimeRange()),
                    range -> all(Arbitraries.localTimes(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateLocalTimeRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.localTimes(), Arbitraries::localTimes).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.localTimes(), Arbitraries::localTimes).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("localDateTimes")
    class LocalDateTimes {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateLocalDateTimeRange()),
                    range -> all(localDateTimes(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateLocalDateTimeRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.localDateTimes(), Arbitraries::localDateTimes).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.localDateTimes(), Arbitraries::localDateTimes).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("durations")
    class Durations {
        @Test
        void allInRange() {
            checkThatForEach(generateParametersForTests(generateDurationRange()),
                    range -> all(Arbitraries.durations(range)).satisfy(inRange(range)));
        }

        @Test
        void deterministicForAGivenSeed() {
            checkThatForEach(generateParametersForTests(generateDurationRange().maybe()),
                    maybeRange -> all(Arbitraries.seeds())
                            .satisfy(equivalentSuppliesForSeed(maybeRange.match(__ -> Arbitraries.durations(), Arbitraries::durations).createSupply(getGeneratorParameters()),
                                    maybeRange.match(__ -> Arbitraries.durations(), Arbitraries::durations).createSupply(getGeneratorParameters()))));
        }
    }

    @Nested
    @DisplayName("nonEmptyVectors")
    class NonEmptyVectors {
        @Test
        void haveAtLeastOneElement() {
            checkThat(all(Arbitraries.nonEmptyVectors()).satisfy(Prop.predicate("non-empty", nev -> !nev.isEmpty())));
        }

        @Test
        void haveExactSizeRequested() {
            checkThatForEach(generateParametersForTests(generateInt(IntRange.from(1).to(10))),
                    size -> all(nonEmptyVectors(size)).satisfy(Prop.predicate("has requested size", nev -> nev.size() == size)));
        }

        @Test
        void haveSizeWithinRequestedRange() {
            checkThatForEach(generateParametersForTests(generateIntRange(IntRange.from(1).to(10))),
                    sizeRange -> all(nonEmptyVectors(sizeRange)).satisfy(Prop.predicate("has requested size", nev -> sizeRange.includes(nev.size()))));
        }
    }

    private <A> Prop<A> inRange(Constraint<A> range) {
        return Prop.predicate("in range", range::includes);
    }
}