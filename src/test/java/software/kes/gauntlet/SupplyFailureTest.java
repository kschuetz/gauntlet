package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import org.junit.jupiter.api.Test;
import software.kes.collectionviews.NonEmptyVector;
import testsupport.TestSupportProps;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static software.kes.gauntlet.Arbitraries.doubles;
import static software.kes.gauntlet.Arbitraries.ints;
import static software.kes.gauntlet.Arbitraries.longs;
import static software.kes.gauntlet.Arbitraries.seeds;
import static software.kes.gauntlet.Arbitraries.shorts;
import static software.kes.gauntlet.Arbitraries.tuplesOf;
import static software.kes.gauntlet.SupplyFailure.supplyFailure;
import static software.kes.gauntlet.SupplyTree.collection;
import static software.kes.gauntlet.SupplyTree.composite;
import static software.kes.gauntlet.SupplyTree.exhausted;
import static software.kes.gauntlet.SupplyTree.filter;
import static software.kes.gauntlet.SupplyTree.leaf;
import static software.kes.gauntlet.SupplyTree.mapping;
import static software.kes.kraftwerk.Generators.generateInt;

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

        checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void composite1() {
        Supply<Tuple3<Integer, Integer, Integer>> supply = tuplesOf(unproductive, ints(), ints()).createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS)));

        checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void composite2() {
        Supply<Tuple3<Integer, Integer, Integer>> supply = tuplesOf(ints(), unproductive, ints()).createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(leaf("int"), exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS)));

        checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void composite3() {
        Supply<Tuple3<Integer, Integer, Integer>> supply = tuplesOf(ints(), ints(), unproductive).createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, composite(leaf("int"), leaf("int"), exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS)));

        checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeedMatching(supply, expected)));
    }

    @Test
    void simpleCollection() {
        Supply<NonEmptyVector<Integer>> supply = unproductive.nonEmptyVector().createSupply(getGeneratorParameters());

        SupplyFailure expected = supplyFailure(MAX_DISCARDS, mapping(collection(exhausted(leaf(UNPRODUCTIVE), MAX_DISCARDS))));

        checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeedMatching(supply, expected)));
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

        checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeedMatching(supply, expected)));
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

        checkThat(all(seeds()).satisfy(TestSupportProps.supplyFailureForSeedMatching(supply, expected)));
    }
}
