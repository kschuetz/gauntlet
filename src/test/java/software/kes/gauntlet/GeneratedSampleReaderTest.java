package software.kes.gauntlet;

import org.junit.jupiter.api.Test;
import software.kes.collectionviews.Vector;
import software.kes.kraftwerk.Seed;
import software.kes.kraftwerk.constraints.IntRange;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.kes.gauntlet.Arbitraries.ints;
import static software.kes.gauntlet.GeneratedSampleReader.generatedSampleReader;
import static testsupport.matchers.SatisfiesPredicate.satisfiesPredicate;

class GeneratedSampleReaderTest extends GauntletApiBase {
    private static final int MAX_DISCARDS = 5;

    @Test
    void totalSampleCountZero() {
        Seed inputSeed = Seed.random();
        GeneratedSampleReader<Integer> reader = generatedSampleReader(0, never(), inputSeed);

        SampleBlock<Integer> block = reader.readBlock(1000);

        assertEquals(Vector.empty(), block.getSamples());
        assertEquals(nothing(), block.getSupplyFailure());
        assertEquals(inputSeed, reader.getOutputSeed());
    }

    @Test
    void requestBlockOfSizeZero() {
        Seed inputSeed = Seed.random();
        GeneratedSampleReader<Integer> reader = generatedSampleReader(1000, never(), inputSeed);

        SampleBlock<Integer> block = reader.readBlock(0);

        assertEquals(Vector.empty(), block.getSamples());
        assertEquals(nothing(), block.getSupplyFailure());
        assertEquals(inputSeed, reader.getOutputSeed());
    }

    @Test
    void attemptsUpToMaxDiscards() {
        Seed inputSeed = Seed.random();
        GeneratedSampleReader<Integer> reader = generatedSampleReader(1000, never(), inputSeed);

        SampleBlock<Integer> block = reader.readBlock(1);

        assertEquals(Vector.empty(), block.getSamples());
        assertThat(block.getSupplyFailure().orElseThrow(AssertionError::new),
                satisfiesPredicate(sf -> sf.getDiscardCount() == MAX_DISCARDS));
    }

    @Test
    void returnsAsManyAsRequestedIfAvailable() {
        Seed inputSeed = Seed.random();
        checkThat(all(ints(IntRange.inclusive(0, 10)))
                .satisfy(Prop.prop("returns requested count",
                        requestedCount -> {
                            GeneratedSampleReader<Integer> reader = generatedSampleReader(10, always(), inputSeed);
                            int actual = reader.readBlock(requestedCount).getSamples().size();
                            return actual == requestedCount
                                    ? SimpleResult.pass()
                                    : SimpleResult.fail("size returned is " + actual);
                        })));
    }

    @Test
    void returnsOnlyTheAmountAvailable() {
        checkThat(all(ints(IntRange.exclusive(0, 5)), ints(IntRange.inclusive(5, 10)))
                .satisfy(Prop.prop("returns available count",
                        into((availableCount, requestedCount) -> {
                            GeneratedSampleReader<Integer> reader = generatedSampleReader(availableCount, always(), Seed.random());
                            int actual = reader.readBlock(requestedCount).getSamples().size();
                            return actual == availableCount
                                    ? SimpleResult.pass()
                                    : SimpleResult.fail("size returned is " + actual);
                        }))));
    }

    @Test
    void multipleRequestsReturnExpectedSizes() {
        checkThat(all(ints(IntRange.inclusive(1, 3)).triple())
                .satisfy(Prop.prop(into3((request1, request2, request3) -> {
                    int availableCount = request1 + request2 + request3;
                    GeneratedSampleReader<Integer> reader = generatedSampleReader(availableCount, always(), Seed.random());
                    int blockSize1 = reader.readBlock(request1).getSamples().size();
                    int blockSize2 = reader.readBlock(request2).getSamples().size();
                    int blockSize3 = reader.readBlock(request3).getSamples().size();
                    int blockSize4 = reader.readBlock(1).getSamples().size();
                    return SimpleResult.test(blockSize1 == request1, "incorrect size of block 1: " + blockSize1)
                            .and(SimpleResult.test(blockSize2 == request2, "incorrect size of block 2: " + blockSize2))
                            .and(SimpleResult.test(blockSize3 == request3, "incorrect size of block 3: " + blockSize3))
                            .and(SimpleResult.test(blockSize4 == 0, "incorrect size of block 4: " + blockSize4));
                }))));
    }

    private Supply<Integer> always() {
        return ints().createSupply(getGeneratorParameters());
    }

    private Supply<Integer> never() {
        return ints().suchThat(constantly(false)).withMaxDiscards(MAX_DISCARDS).createSupply(getGeneratorParameters());
    }
}
