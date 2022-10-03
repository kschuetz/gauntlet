package software.kes.gauntlet;

import org.junit.jupiter.api.Test;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.kes.gauntlet.SupplyFailure.supplyFailure;
import static software.kes.gauntlet.SupplyTree.composite;
import static software.kes.gauntlet.SupplyTree.exhausted;
import static testsupport.SequentialSeed.initialSequentialSeed;
import static testsupport.SequentialSupply.sequentialSupply;

class CompositeSupply5Test {
    @Test
    void threadsSeedCorrectly() {
        CompositeSupply5<String, String, String, String, String, String> supply = new CompositeSupply5<>(
                sequentialSupply(n -> "a:" + n),
                sequentialSupply(n -> "b:" + n),
                sequentialSupply(n -> "c:" + n),
                sequentialSupply(n -> "d:" + n),
                sequentialSupply(n -> "e:" + n),
                (s1, s2, s3, s4, s5) -> s1 + ", " + s2 + ", " + s3 + ", " + s4 + ", " + s5);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("a:0, b:1, c:2, d:3, e:4"), r1.getValue());
        assertEquals(right("a:5, b:6, c:7, d:8, e:9"), r2.getValue());
        assertEquals(right("a:10, b:11, c:12, d:13, e:14"), r3.getValue());
        assertEquals(right("a:15, b:16, c:17, d:18, e:19"), r4.getValue());
    }

    @Test
    void propagatesSupplyFailures() {
        Supply<Long> unfiltered = sequentialSupply(id());
        Supply<Long> filtered = new FilteredSupply<>(unfiltered, n -> n < 15, 1);

        CompositeSupply5<Long, Long, Long, Long, Long, String> supply = new CompositeSupply5<>(
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                filtered,
                (n1, n2, n3, n4, n5) -> n1 + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0, 1, 2, 3, 4"), r1.getValue());
        assertEquals(right("5, 6, 7, 8, 9"), r2.getValue());
        assertEquals(right("10, 11, 12, 13, 14"), r3.getValue());
        assertEquals(left(supplyFailure(1, composite(unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                exhausted(unfiltered.getSupplyTree(), 1)))), r4.getValue());
    }
}
