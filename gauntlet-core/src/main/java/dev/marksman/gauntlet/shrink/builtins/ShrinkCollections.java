package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.shrink.ShrinkResult;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import java.util.ArrayList;
import java.util.HashSet;

public class ShrinkCollections {

    public static <A> ShrinkStrategy<ImmutableVector<A>> shrinkVector(ShrinkStrategy<A> element) {
        return shrinkVector(0, element);
    }

    public static <A> ShrinkStrategy<ImmutableNonEmptyVector<A>> shrinkNonEmptyVector(ShrinkStrategy<A> element) {
        return shrinkVector(1, element)
                .convert(ImmutableVector::toNonEmptyOrThrow,
                        vector -> vector);
    }

    public static <A> ShrinkStrategy<ArrayList<A>> shrinkArrayList(ShrinkStrategy<A> element) {
        return shrinkVector(element)
                .convert(vector -> vector.toCollection(ArrayList::new),
                        Vector::copyFrom);
    }

    public static <A> ShrinkStrategy<ArrayList<A>> shrinkNonEmptyArrayList(ShrinkStrategy<A> element) {
        return shrinkNonEmptyVector(element)
                .convert(vector -> vector.toCollection(ArrayList::new),
                        arrayList -> Vector.copyFrom(arrayList).toNonEmptyOrThrow());
    }

    public static <A> ShrinkStrategy<HashSet<A>> shrinkHashSet(ShrinkStrategy<A> element) {
        return shrinkVector(element)
                .convert(vector -> vector.toCollection(HashSet::new),
                        Vector::copyFrom);
    }

    public static <A> ShrinkStrategy<HashSet<A>> shrinkNonEmptyHashSet(ShrinkStrategy<A> element) {
        return shrinkNonEmptyVector(element)
                .convert(vector -> vector.toCollection(HashSet::new),
                        hashSet -> Vector.copyFrom(hashSet).toNonEmptyOrThrow());
    }

    public static <A> ShrinkStrategy<ImmutableVector<A>> shrinkVector(int minimumSize, ShrinkStrategy<A> element) {
        return input -> {
            int size = input.size();
            if (size <= minimumSize) {
                return ShrinkResult.empty();
            }
            // TODO: implement ShrinkVector.  This is a start.
            return ShrinkResultBuilder.<ImmutableVector<A>>shrinkResultBuilder()
                    .when(minimumSize == 0, b -> b.append(Vector.empty()))
                    .lazyAppend(() -> evenElements(input))
                    .lazyAppend(() -> oddElements(input))
                    .build();
        };
    }

    private static <A> ImmutableVector<A> evenElements(ImmutableVector<A> vector) {
        int newSize = (1 + vector.size()) / 2;
        return Vector.lazyFill(newSize, idx -> vector.unsafeGet(idx * 2));
    }

    private static <A> ImmutableVector<A> oddElements(ImmutableVector<A> vector) {
        int newSize = vector.size() / 2;
        return Vector.lazyFill(newSize, idx -> vector.unsafeGet(1 + idx * 2));
    }

}
