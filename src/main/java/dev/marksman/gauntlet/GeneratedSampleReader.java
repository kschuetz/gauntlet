package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.GeneratedSample.generatedSample;
import static dev.marksman.gauntlet.SampleBlock.sampleBlock;

final class GeneratedSampleReader<A> implements SampleReader<GeneratedSample<A>> {
    private final Supply<A> supply;
    private int samplesRemaining;
    private Seed currentSeed;

    private GeneratedSampleReader(int samplesRequested, Supply<A> supply, Seed inputSeed) {
        this.samplesRemaining = Math.max(0, samplesRequested);
        this.supply = supply;
        this.currentSeed = inputSeed;
    }

    static <A> GeneratedSampleReader<A> generatedSampleReader(int samplesRequested, Supply<A> supply, Seed inputSeed) {
        return new GeneratedSampleReader<>(samplesRequested, supply, inputSeed);
    }

    public Seed getOutputSeed() {
        return currentSeed;
    }

    @Override
    public SampleBlock<GeneratedSample<A>> readBlock(int size) {
        if (size <= 0 || samplesRemaining <= 0) {
            return sampleBlock(Vector.empty(), nothing());
        }
        int blockSize = Math.min(size, samplesRemaining);
        VectorBuilder<GeneratedSample<A>> builder = Vector.builder(blockSize);
        Maybe<SupplyFailure> supplyFailure = nothing();
        int i = 0;
        while (i < blockSize) {
            Seed inputSeed = currentSeed;
            GeneratorOutput<A> next = supply.getNext(inputSeed);
            currentSeed = next.getNextState();
            Either<SupplyFailure, A> value = next.getValue();
            supplyFailure = value.projectA();
            if (!supplyFailure.equals(nothing())) {
                break;
            }
            A element = value.projectB().orElseThrow(AssertionError::new);
            builder = builder.add(generatedSample(inputSeed, element));
            i += 1;
            samplesRemaining -= 1;
        }
        return sampleBlock(builder.build(), supplyFailure);
    }
}
