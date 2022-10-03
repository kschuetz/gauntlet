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

class CompositeSupply4Test {
    @Test
    void threadsSeedCorrectly() {
        CompositeSupply4<String, String, String, String, String> supply = new CompositeSupply4<>(
                sequentialSupply(n -> "a:" + n),
                sequentialSupply(n -> "b:" + n),
                sequentialSupply(n -> "c:" + n),
                sequentialSupply(n -> "d:" + n),
                (s1, s2, s3, s4) -> s1 + ", " + s2 + ", " + s3 + ", " + s4);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("a:0, b:1, c:2, d:3"), r1.getValue());
        assertEquals(right("a:4, b:5, c:6, d:7"), r2.getValue());
        assertEquals(right("a:8, b:9, c:10, d:11"), r3.getValue());
        assertEquals(right("a:12, b:13, c:14, d:15"), r4.getValue());
    }

    @Test
    void propagatesSupplyFailures() {
        Supply<Long> unfiltered = sequentialSupply(id());
        Supply<Long> filtered = new FilteredSupply<>(unfiltered, n -> n < 12, 1);

        CompositeSupply4<Long, Long, Long, Long, String> supply = new CompositeSupply4<>(
                unfiltered,
                unfiltered,
                unfiltered,
                filtered,
                (n1, n2, n3, n4) -> n1 + ", " + n2 + ", " + n3 + ", " + n4);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0, 1, 2, 3"), r1.getValue());
        assertEquals(right("4, 5, 6, 7"), r2.getValue());
        assertEquals(right("8, 9, 10, 11"), r3.getValue());
        assertEquals(left(supplyFailure(1, composite(unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                exhausted(unfiltered.getSupplyTree(), 1)))), r4.getValue());
    }
}
