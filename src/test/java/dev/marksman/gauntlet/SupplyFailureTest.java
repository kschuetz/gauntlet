package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.collectionviews.NonEmptyVector;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static dev.marksman.gauntlet.Arbitraries.doubles;
import static dev.marksman.gauntlet.Arbitraries.ints;
import static dev.marksman.gauntlet.Arbitraries.longs;
import static dev.marksman.gauntlet.Arbitraries.seeds;
import static dev.marksman.gauntlet.Arbitraries.shorts;
import static dev.marksman.gauntlet.Arbitraries.tuplesOf;
import static dev.marksman.gauntlet.SupplyFailure.supplyFailure;
import static dev.marksman.gauntlet.SupplyTree.collection;
import static dev.marksman.gauntlet.SupplyTree.composite;
import static dev.marksman.gauntlet.SupplyTree.exhausted;
import static dev.marksman.gauntlet.SupplyTree.filter;
import static dev.marksman.gauntlet.SupplyTree.leaf;
import static dev.marksman.gauntlet.SupplyTree.mapping;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static testsupport.TestSupportProps.supplyFailureForSeedMatching;

public class SupplyFailureTest extends GauntletApiBase {
    private static final int MAX_DISCARDS = 5;
    private static final String UNPRODUCTIVE = "unproductive";
    private final Arbitrary<Integer> unproductive = Arbitrary.arbitrary(generateInt().labeled(UNPRODUCTIVE))
            .suchThat(constantly(false))
            .withMaxDiscards(MAX_DISCARDS);

    @Test
    void simple() {
        Supply<Integer> supply = unproductive.createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS));

        checkThat(all(seeds()).satisfy(supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void composite1() {
        Supply<Tuple3<Integer, Integer, Integer>> supply = tuplesOf(unproductive, ints(), ints()).createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS)));

        checkThat(all(seeds()).satisfy(supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void composite2() {
        Supply<Tuple3<Integer, Integer, Integer>> supply = tuplesOf(ints(), unproductive, ints()).createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(leaf("int"), exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS)));

        checkThat(all(seeds()).satisfy(supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void composite3() {
        Supply<Tuple3<Integer, Integer, Integer>> supply = tuplesOf(ints(), ints(), unproductive).createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(leaf("int"), leaf("int"), exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS)));

        checkThat(all(seeds()).satisfy(supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void simpleCollection() {
        Supply<NonEmptyVector<Integer>> supply = unproductive.nonEmptyVector().createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, mapping(collection(exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS))));

        checkThat(all(seeds()).satisfy(supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void complex1() {
        Supply<Tuple3<Integer, Tuple3<Long, NonEmptyVector<Tuple2<Integer, Double>>, Integer>, Short>> supply = tuplesOf(ints(),
                tuplesOf(longs(),
                        tuplesOf(unproductive, doubles())
                                .nonEmptyVector(),
                        ints())
                        .suchThat(constantly(true)),
                shorts())
                .createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(leaf("int"),
                filter(composite(leaf("long"), mapping(collection(composite(exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS))))))));

        checkThat(all(seeds()).satisfy(supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void complex2() {
        Arbitrary<Tuple2<NonEmptyVector<Double>, Short>> compoundUnproductive = tuplesOf(doubles().nonEmptyVector(), shorts())
                .suchThat(constantly(false))
                .withMaxDiscards(MAX_DISCARDS);
        Supply<Tuple3<Long, Tuple2<NonEmptyVector<Double>, Short>, Integer>> supply = tuplesOf(longs(), compoundUnproductive, unproductive)
                .createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(leaf("long"),
                exhausted(composite(mapping(collection(leaf("double"))), leaf("short")), MAX_DISCARDS)));

        checkThat(all(seeds()).satisfy(supplyFailureForSeedMatching(supply, expected)));
    }
}
