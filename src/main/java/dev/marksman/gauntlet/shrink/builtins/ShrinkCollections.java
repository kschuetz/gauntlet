package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.gauntlet.shrink.Shrink;

public class ShrinkCollections {

    public static <A> Shrink<ImmutableVector<A>> shrinkVector(Shrink<A> element) {
        return Shrink.none();
    }
}
