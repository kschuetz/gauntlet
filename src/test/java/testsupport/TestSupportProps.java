package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.gauntlet.GeneratorOutput;
import software.kes.gauntlet.Prop;
import software.kes.gauntlet.SimpleResult;
import software.kes.gauntlet.Supply;
import software.kes.gauntlet.SupplyFailure;
import software.kes.kraftwerk.Seed;

public final class TestSupportProps {
    private static final int SAMPLE_COUNT_FOR_COMPARING_SUPPLIES = 50;

    public static <A> Prop<Seed> equivalentSuppliesForSeed(Supply<A> supply1, Supply<A> supply2) {
        return Prop.prop("equivalent supplies", (Seed initialSeed) -> {
            Seed current = initialSeed;
            for (int i = 1; i <= SAMPLE_COUNT_FOR_COMPARING_SUPPLIES; i++) {
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

    public static <A> Prop<Seed> supplyFailureForSeed(Supply<A> supply) {
        return Prop.predicate("results in SupplyFailure", (Seed seed) -> supply.getNext(seed).isFailure());
    }

    public static <A> Prop<Seed> supplyFailureForSeedMatching(Supply<A> supply, Fn1<SupplyFailure, Boolean> matchesSupplyFailure) {
        return Prop.prop("results in SupplyFailure with matching SupplyTree", (Seed seed) -> supply.getNext(seed)
                .getValue()
                .match(sf -> matchesSupplyFailure.apply(sf)
                                ? SimpleResult.pass()
                                : SimpleResult.fail("SupplyFailure didn't match expected: " + sf),
                        __ -> SimpleResult.fail("SupplyFailure expected but did not occur")));
    }

    public static <A> Prop<Seed> supplyFailureForSeedMatching(Supply<A> supply, SupplyFailure expected) {
        return supplyFailureForSeedMatching(supply, sf -> sf.equals(expected));
    }
}
