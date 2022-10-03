package software.kes.gauntlet;

import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.kraftwerk.Seed;

public interface TestParameterCollection<A> {
    ImmutableNonEmptyVector<A> getValues();

    Seed getOutputSeed();
}
