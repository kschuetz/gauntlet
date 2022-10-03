package software.kes.gauntlet;

import org.junit.jupiter.api.Test;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.kes.gauntlet.SupplyFailure.supplyFailure;
import static software.kes.gauntlet.SupplyTree.exhausted;
import static software.kes.gauntlet.SupplyTree.leaf;
import static testsupport.SequentialSeed.initialSequentialSeed;
import static testsupport.SequentialSupply.sequentialSupply;

final class FilteredSupplyTest {
    @Test
    void triesUpToMaxDiscards() {
        Supply<Long> source = sequentialSupply(id());
        FilteredSupply<Long> supply = new FilteredSupply<>(source, n -> n % 2 == 0, 1);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<Long> r1 = supply.getNext(s1);
        GeneratorOutput<Long> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<Long> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<Long> r4 = supply.getNext(r3.getNextState());

        assertEquals(right(0L), r1.getValue());
        assertEquals(right(2L), r2.getValue());
        assertEquals(right(4L), r3.getValue());
        assertEquals(right(6L), r4.getValue());
    }

    @Test
    void givesUpAfterMaxDiscards() {
        Supply<Long> source = sequentialSupply(id());
        FilteredSupply<Long> supply = new FilteredSupply<>(source, n -> n < 4, 100);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<Long> r1 = supply.getNext(s1);
        GeneratorOutput<Long> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<Long> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<Long> r4 = supply.getNext(r3.getNextState());
        GeneratorOutput<Long> r5 = supply.getNext(r4.getNextState());

        assertEquals(right(0L), r1.getValue());
        assertEquals(right(1L), r2.getValue());
        assertEquals(right(2L), r3.getValue());
        assertEquals(right(3L), r4.getValue());
        assertEquals(left(supplyFailure(100, exhausted(leaf("SequentialSupply"), 100))), r5.getValue());
    }
}
