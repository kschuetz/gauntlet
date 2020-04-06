package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.gauntlet.shrink.Shrink;

import java.util.ArrayList;
import java.util.HashSet;

public class ShrinkCollections {

    public static <A> Shrink<ImmutableVector<A>> shrinkVector(Shrink<A> element) {
        return Shrink.none();
    }

    public static <A> Shrink<ImmutableNonEmptyVector<A>> shrinkNonEmptyVector(Shrink<A> element) {
        return Shrink.none();
    }

    public static <A> Shrink<ArrayList<A>> shrinkArrayList(Shrink<A> element) {
        return Shrink.none();
    }

    public static <A> Shrink<ArrayList<A>> shrinkNonEmptyArrayList(Shrink<A> element) {
        return Shrink.none();
    }

    public static <A> Shrink<HashSet<A>> shrinkHashSet(Shrink<A> element) {
        return Shrink.none();
    }

    public static <A> Shrink<HashSet<A>> shrinkNonEmptyHashSet(Shrink<A> element) {
        return Shrink.none();
    }

}
