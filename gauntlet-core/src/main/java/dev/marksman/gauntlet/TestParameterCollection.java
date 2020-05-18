package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.kraftwerk.Seed;

public interface TestParameterCollection<A> {
    ImmutableNonEmptyVector<A> getValues();

    Seed getOutputSeed();
}
