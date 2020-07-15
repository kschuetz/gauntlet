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

class CompositeSupply2Test {
    @Test
    void threadsSeedCorrectly() {
        CompositeSupply2<String, String, String> supply = new CompositeSupply2<>(
                sequentialSupply(n -> "a:" + n),
                sequentialSupply(n -> "b:" + n),
                (s1, s2) -> s1 + ", " + s2);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("a:0, b:1"), r1.getValue());
        assertEquals(right("a:2, b:3"), r2.getValue());
        assertEquals(right("a:4, b:5"), r3.getValue());
        assertEquals(right("a:6, b:7"), r4.getValue());
    }

    @Test
    void propagatesSupplyFailures() {
        Supply<Long> unfiltered = sequentialSupply(id());
        Supply<Long> filtered = new FilteredSupply<>(unfiltered, n -> n < 6, 1);

        CompositeSupply2<Long, Long, String> supply = new CompositeSupply2<>(
                unfiltered,
                filtered,
                (n1, n2) -> n1 + ", " + n2);

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0, 1"), r1.getValue());
        assertEquals(right("2, 3"), r2.getValue());
        assertEquals(right("4, 5"), r3.getValue());
        assertEquals(left(supplyFailure(1, composite(unfiltered.getSupplyTree(),
                exhausted(unfiltered.getSupplyTree(), 1)))), r4.getValue());
    }
}
