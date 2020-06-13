package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Seed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.Arbitraries.ints;
import static dev.marksman.gauntlet.Arbitraries.longs;

class ArbitraryTest extends GauntletApiBase {

    private static final int SAMPLES_COUNT_FOR_COMPARING_SUPPLIES = 10;

    @Nested
    @DisplayName("convertWithPrism")
    class ConvertWithPrism {
        @Test
        void filtersValuesCorrectly() {
            Arbitrary<Integer> evens = ints().convertWithPrism(n -> n % 2 == 0 ? just(n) : nothing(), id());
            assertThat(all(evens).satisfy(Prop.predicate(n -> n % 2 == 0)));
        }

        @Test
        void equivalentToFilteringWithSuchThat() {
            Arbitrary<Integer> evens1 = ints().suchThat(n -> n % 2 == 0);
            Arbitrary<Integer> evens2 = ints().convertWithPrism(n -> n % 2 == 0 ? just(n) : nothing(), id());

            Supply<Integer> supply1 = evens1.createSupply(getGeneratorParameters());
            Supply<Integer> supply2 = evens2.createSupply(getGeneratorParameters());

            assertThat(all(seeds()).satisfy(equivalentSuppliesForSeed(supply1, supply2)));
        }

        @Test
        void failsWithSupplyFailureWhenMaxDiscardsRunOut() {
            Arbitrary<Integer> unproductive = ints().convertWithPrism(constantly(nothing()), id())
                    .withMaxDiscards(5);
            Supply<Integer> supply = unproductive.createSupply(getGeneratorParameters());
            assertThat(all(seeds()).satisfy(supplyFailureForSeed(supply)));
        }
    }

    private static Arbitrary<Seed> seeds() {
        return longs().withNoShrinkStrategy().convert(Seed::create, Seed::getSeedValue);
    }

    private static <A> Prop<Seed> equivalentSuppliesForSeed(Supply<A> supply1, Supply<A> supply2) {
        return Prop.prop("equivalent supplies", (Seed initialSeed) -> {
            Seed current = initialSeed;
            for (int i = 1; i <= SAMPLES_COUNT_FOR_COMPARING_SUPPLIES; i++) {
                GeneratorOutput<A> value1 = supply1.getNext(current);
                GeneratorOutput<A> value2 = supply2.getNext(current);
                if (!value1.equals(value2)) {
                    return SimpleResult.fail("Different output on sample " + i);
                }
                current = value1.getNextState();
            }
            return SimpleResult.pass();
        });
    }

    private static <A> Prop<Seed> supplyFailureForSeed(Supply<A> supply) {
        return Prop.predicate("results in SupplyFailure", (Seed seed) -> supply.getNext(seed).isFailure());
    }
}