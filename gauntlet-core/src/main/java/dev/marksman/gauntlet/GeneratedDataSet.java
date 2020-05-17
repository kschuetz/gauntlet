package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Seed;

final class GeneratedDataSet<A> {
    private final ImmutableVector<A> samples;
    private final Maybe<SupplyFailure> supplyFailure;
    private final Seed outputSeed;

    private GeneratedDataSet(ImmutableVector<A> samples, Maybe<SupplyFailure> supplyFailure, Seed outputSeed) {
        this.samples = samples;
        this.supplyFailure = supplyFailure;
        this.outputSeed = outputSeed;
    }

    public static <A> GeneratedDataSet<A> generatedDataSet(Iterable<A> values,
                                                           Maybe<SupplyFailure> supplyFailure,
                                                           Seed outputSeed) {
        return new GeneratedDataSet<>(Vector.copyFrom(values), supplyFailure, outputSeed);
    }

    public ImmutableVector<A> getSamples() {
        return this.samples;
    }

    public Maybe<SupplyFailure> getSupplyFailure() {
        return this.supplyFailure;
    }

    public Seed getOutputSeed() {
        return this.outputSeed;
    }
}
