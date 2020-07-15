package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Seed;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.SupplyFailure.supplyFailure;
import static dev.marksman.gauntlet.SupplyTree.composite;
import static dev.marksman.gauntlet.SupplyTree.exhausted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testsupport.SequentialSeed.initialSequentialSeed;
import static testsupport.SequentialSupply.sequentialSupply;

class CompositeSupply6Test {
    @Test
    void threadsSeedCorrectly() {
        CompositeSupply6<String, String, String, String, String, String, String> supply = new CompositeSupply6<>(
                sequentialSupply(n -> "a:" + n),
                sequentialSupply(n -> "b:" + n),
                sequentialSupply(n -> "c:" + n),
                sequentialSupply(n -> "d:" + n),
                sequentialSupply(n -> "e:" + n),
                sequentialSupply(n -> "f:" + n),
                (s1, s2, s3, s4, s5, s6) -> s1 + ", " + s2 + ", " + s3 + ", " + s4 + ", " + s5 + ", " + s6);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("a:0, b:1, c:2, d:3, e:4, f:5"), r1.getValue());
        assertEquals(right("a:6, b:7, c:8, d:9, e:10, f:11"), r2.getValue());
        assertEquals(right("a:12, b:13, c:14, d:15, e:16, f:17"), r3.getValue());
        assertEquals(right("a:18, b:19, c:20, d:21, e:22, f:23"), r4.getValue());
    }

    @Test
    void propagatesSupplyFailures() {
        Supply<Long> unfiltered = sequentialSupply(id());
        Supply<Long> filtered = new FilteredSupply<>(unfiltered, n -> n < 18, 1);

        CompositeSupply6<Long, Long, Long, Long, Long, Long, String> supply = new CompositeSupply6<>(
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                filtered,
                (n1, n2, n3, n4, n5, n6) -> n1 + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0, 1, 2, 3, 4, 5"), r1.getValue());
        assertEquals(right("6, 7, 8, 9, 10, 11"), r2.getValue());
        assertEquals(right("12, 13, 14, 15, 16, 17"), r3.getValue());
        assertEquals(left(supplyFailure(1, composite(unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                exhausted(unfiltered.getSupplyTree(), 1)))), r4.getValue());
    }
}
