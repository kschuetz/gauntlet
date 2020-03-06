package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableVector;


public interface DataSet<A> {
    ImmutableVector<A> getValues();

    Maybe<SupplyFailure> getSupplyFailure();
}
