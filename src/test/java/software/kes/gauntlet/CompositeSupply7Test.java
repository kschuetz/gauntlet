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

class CompositeSupply7Test {
    @Test
    void threadsSeedCorrectly() {
        CompositeSupply7<String, String, String, String, String, String, String, String> supply = new CompositeSupply7<>(
                sequentialSupply(n -> "a:" + n),
                sequentialSupply(n -> "b:" + n),
                sequentialSupply(n -> "c:" + n),
                sequentialSupply(n -> "d:" + n),
                sequentialSupply(n -> "e:" + n),
                sequentialSupply(n -> "f:" + n),
                sequentialSupply(n -> "g:" + n),
                (s1, s2, s3, s4, s5, s6, s7) -> s1 + ", " + s2 + ", " + s3 + ", " + s4 + ", " + s5 + ", " + s6 + ", " + s7);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("a:0, b:1, c:2, d:3, e:4, f:5, g:6"), r1.getValue());
        assertEquals(right("a:7, b:8, c:9, d:10, e:11, f:12, g:13"), r2.getValue());
        assertEquals(right("a:14, b:15, c:16, d:17, e:18, f:19, g:20"), r3.getValue());
        assertEquals(right("a:21, b:22, c:23, d:24, e:25, f:26, g:27"), r4.getValue());
    }

    @Test
    void propagatesSupplyFailures() {
        Supply<Long> unfiltered = sequentialSupply(id());
        Supply<Long> filtered = new FilteredSupply<>(unfiltered, n -> n < 21, 1);

        CompositeSupply7<Long, Long, Long, Long, Long, Long, Long, String> supply = new CompositeSupply7<>(
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                unfiltered,
                filtered,
                (n1, n2, n3, n4, n5, n6, n7) -> n1 + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0, 1, 2, 3, 4, 5, 6"), r1.getValue());
        assertEquals(right("7, 8, 9, 10, 11, 12, 13"), r2.getValue());
        assertEquals(right("14, 15, 16, 17, 18, 19, 20"), r3.getValue());
        assertEquals(left(supplyFailure(1, composite(unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                exhausted(unfiltered.getSupplyTree(), 1)))), r4.getValue());
    }
}
