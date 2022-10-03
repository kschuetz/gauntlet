package software.kes.gauntlet;

import org.junit.jupiter.api.Test;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.kes.gauntlet.MappedSupply.mappedSupply;
import static testsupport.SequentialSeed.initialSequentialSeed;
import static testsupport.SequentialSupply.sequentialSupply;

final class MappedSupplyTest {
    @Test
    void mappedOnce() {
        Supply<Long> source = sequentialSupply(id());
        Supply<Long> supply = mappedSupply(n -> n * 2, source);

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
    void mappedTwice() {
        Supply<Long> source = sequentialSupply(id());
        Supply<String> supply = mappedSupply(Object::toString, mappedSupply(n -> n * 2, source));

        Seed s1 = initialSequentialSeed();
        GeneratorOutput<String> r1 = supply.getNext(s1);
        GeneratorOutput<String> r2 = supply.getNext(r1.getNextState());
        GeneratorOutput<String> r3 = supply.getNext(r2.getNextState());
        GeneratorOutput<String> r4 = supply.getNext(r3.getNextState());

        assertEquals(right("0"), r1.getValue());
        assertEquals(right("2"), r2.getValue());
        assertEquals(right("4"), r3.getValue());
        assertEquals(right("6"), r4.getValue());
    }
}
