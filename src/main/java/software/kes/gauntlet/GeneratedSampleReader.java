package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import software.kes.collectionviews.Vector;
import software.kes.collectionviews.VectorBuilder;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static software.kes.gauntlet.SampleBlock.sampleBlock;

final class GeneratedSampleReader<A> implements SampleReader<A> {
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
    public SampleBlock<A> readBlock(int size) {
        if (size <= 0 || samplesRemaining <= 0) {
            return sampleBlock(Vector.empty(), nothing());
        }
        int blockSize = Math.min(size, samplesRemaining);
        VectorBuilder<A> builder = Vector.builder(blockSize);
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
            builder = builder.add(element);
            i += 1;
            samplesRemaining -= 1;
        }
        return sampleBlock(builder.build(), supplyFailure);
    }
}
