package dev.marksman.gauntlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testsupport.TestSupportProps;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.Arbitraries.ints;
import static dev.marksman.gauntlet.Arbitraries.seeds;

class ArbitraryTest extends GauntletApiBase {

    @Nested
    @DisplayName("convertWithPrism")
    class ConvertWithPrism {
        @Test
        void filtersValuesCorrectly() {
            Arbitrary<Integer> evens = ints().convertWithPrism(n -> n % 2 == 0 ? just(n) : nothing(), id());
            checkThat(all(evens).satisfy(Prop.predicate(n -> n % 2 == 0)));
        }

        @Test
        void equivalentToFilteringWithSuchThat() {
            Arbitrary<Integer> evens1 = ints().suchThat(n -> n % 2 == 0);
            Arbitrary<Integer> evens2 = ints().convertWithPrism(n -> n % 2 == 0 ? just(n) : nothing(), id());

            Supply<Integer> supply1 = evens1.createSupply(getGeneratorParameters());
            Supply<Integer> supply2 = evens2.createSupply(getGeneratorParameters());

            checkThat(all(seeds()).satisfy(TestSupportProps.equivalentSuppliesForSeed(supply1, supply2)));
        }

        @Test
        void failsWithSupplyFailureWhenMaxDiscardsRunOut() {
            Arbitrary<Integer> unproductive = ints().convertWithPrism(constantly(nothing()), id())
                    .withMaxDiscards(5);
            Supply<Integer> supply = unproductive.createSupply(getGeneratorParameters());
            checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeed(supply)));
        }
    }

}