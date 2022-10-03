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

class CompositeSupply8Test {
    @Test
    void threadsSeedCorrectly() {
        CompositeSupply8<String, String, String, String, String, String, String, String, String> supply = new CompositeSupply8<>(
                sequentialSupply(n -> "a:" + n),
                sequentialSupply(n -> "b:" + n),
                sequentialSupply(n -> "c:" + n),
                sequentialSupply(n -> "d:" + n),
                sequentialSupply(n -> "e:" + n),
                sequentialSupply(n -> "f:" + n),
                sequentialSupply(n -> "g:" + n),
                sequentialSupply(n -> "h:" + n),
                (s1, s2, s3, s4, s5, s6, s7, s8) -> s1 + ", " + s2 + ", " + s3 + ", " + s4 + ", " + s5 + ", " + s6 + ", " + s7 + ", " + s8);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("a:0, b:1, c:2, d:3, e:4, f:5, g:6, h:7"), r1.getValue());
        assertEquals(right("a:8, b:9, c:10, d:11, e:12, f:13, g:14, h:15"), r2.getValue());
        assertEquals(right("a:16, b:17, c:18, d:19, e:20, f:21, g:22, h:23"), r3.getValue());
        assertEquals(right("a:24, b:25, c:26, d:27, e:28, f:29, g:30, h:31"), r4.getValue());
    }

    @Test
    void propagatesSupplyFailures() {
        Supply<Long> unfiltered = sequentialSupply(id());
        Supply<Long> filtered = new FilteredSupply<>(unfiltered, n -> n < 24, 1);

        CompositeSupply8<Long, Long, Long, Long, Long, Long, Long, Long, String> supply = new CompositeSupply8<>(
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                filtered,
                (n1, n2, n3, n4, n5, n6, n7, n8) -> n1 + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0, 1, 2, 3, 4, 5, 6, 7"), r1.getValue());
        assertEquals(right("8, 9, 10, 11, 12, 13, 14, 15"), r2.getValue());
        assertEquals(right("16, 17, 18, 19, 20, 21, 22, 23"), r3.getValue());
        assertEquals(left(supplyFailure(1, composite(unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                exhausted(unfiltered.getSupplyTree(), 1)))), r4.getValue());
    }
}
