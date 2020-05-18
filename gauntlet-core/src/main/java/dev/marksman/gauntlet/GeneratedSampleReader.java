package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.SampleBlock.sampleBlock;

final class GeneratedSampleReader<A> implements SampleReader<A> {
    private final StatefulSupply<A> supply;
    private int samplesRemaining;
    private Seed currentSeed;

    private GeneratedSampleReader(int samplesRequested, StatefulSupply<A> supply, Seed inputSeed) {
        this.samplesRemaining = Math.max(0, samplesRequested);
        this.supply = supply;
        this.currentSeed = inputSeed;
    }

    static <A> GeneratedSampleReader<A> generatedSampleReader(int samplesRequested, SupplyStrategy<A> supplyStrategy, Seed inputSeed) {
        return new GeneratedSampleReader<>(samplesRequested, supplyStrategy.createSupply(), inputSeed);
    }

    public Seed getOutputSeed() {
        return currentSeed;
    }

    @Override
    public SampleBlock<A> readBlock(int size) {
        if (size <= 0 || samplesRemaining <= 0) {
            return sampleBlock(Vector.empty(), nothing());
        }
        int blockSize = Math.min(size, samplesRemaining);
        VectorBuilder<A> builder = Vector.builder(blockSize);
        Maybe<SupplyFailure> supplyFailure = nothing();
        int i = 0;
        while (i < blockSize) {
            GeneratorOutput<A> next = supply.getNext(currentSeed);
            currentSeed = next.getNextState();
            Either<SupplyFailure, A> value = next.getValue();
            supplyFailure = value.projectA();
            if (!supplyFailure.equals(nothing())) {
                break;
            }
            builder = builder.add(value.projectB().orElseThrow(AssertionError::new));
            i += 1;
            samplesRemaining -= 1;
        }
        return sampleBlock(builder.build(), supplyFailure);
    }
}
