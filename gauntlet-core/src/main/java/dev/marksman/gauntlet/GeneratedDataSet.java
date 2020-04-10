package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class GeneratedDataSet<A> {
    ImmutableVector<A> samples;
    Maybe<SupplyFailure> supplyFailure;
    Seed outputSeed;

    public static <A> GeneratedDataSet<A> generatedDataSet(Iterable<A> values,
                                                           Maybe<SupplyFailure> supplyFailure,
                                                           Seed outputSeed) {
        return new GeneratedDataSet<>(Vector.copyFrom(values), supplyFailure, outputSeed);
    }
}
