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

class CompositeSupply3Test {
    @Test
    void threadsSeedCorrectly() {
        CompositeSupply3<String, String, String, String> supply = new CompositeSupply3<>(
                sequentialSupply(n -> "a:" + n),
                sequentialSupply(n -> "b:" + n),
                sequentialSupply(n -> "c:" + n),
                (s1, s2, s3) -> s1 + ", " + s2 + ", " + s3);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("a:0, b:1, c:2"), r1.getValue());
        assertEquals(right("a:3, b:4, c:5"), r2.getValue());
        assertEquals(right("a:6, b:7, c:8"), r3.getValue());
        assertEquals(right("a:9, b:10, c:11"), r4.getValue());
    }

    @Test
    void propagatesSupplyFailures() {
        Supply<Long> unfiltered = sequentialSupply(id());
        Supply<Long> filtered = new FilteredSupply<>(unfiltered, n -> n < 9, 1);

        CompositeSupply3<Long, Long, Long, String> supply = new CompositeSupply3<>(
                unfiltered,
                unfiltered,
                filtered,
                (n1, n2, n3) -> n1 + ", " + n2 + ", " + n3);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0, 1, 2"), r1.getValue());
        assertEquals(right("3, 4, 5"), r2.getValue());
        assertEquals(right("6, 7, 8"), r3.getValue());
        assertEquals(left(supplyFailure(1, composite(unfiltered.getSupplyTree(),
                unfiltered.getSupplyTree(),
                exhausted(unfiltered.getSupplyTree(), 1)))), r4.getValue());
    }
}
