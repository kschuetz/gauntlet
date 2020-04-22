package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;

import java.util.ArrayList;
import java.util.HashSet;

public class ShrinkCollections {

    public static <A> Shrink<ImmutableVector<A>> shrinkVector(Shrink<A> element) {
        return shrinkVector(0, element);
    }

    public static <A> Shrink<ImmutableNonEmptyVector<A>> shrinkNonEmptyVector(Shrink<A> element) {
        return shrinkVector(1, element)
                .convert(ImmutableVector::toNonEmptyOrThrow,
                        vector -> vector);
    }

    public static <A> Shrink<ArrayList<A>> shrinkArrayList(Shrink<A> element) {
        return shrinkVector(element)
                .convert(vector -> vector.toCollection(ArrayList::new),
                        Vector::copyFrom);
    }

    public static <A> Shrink<ArrayList<A>> shrinkNonEmptyArrayList(Shrink<A> element) {
        return shrinkNonEmptyVector(element)
                .convert(vector -> vector.toCollection(ArrayList::new),
                        arrayList -> Vector.copyFrom(arrayList).toNonEmptyOrThrow());
    }

    public static <A> Shrink<HashSet<A>> shrinkHashSet(Shrink<A> element) {
        return shrinkVector(element)
                .convert(vector -> vector.toCollection(HashSet::new),
                        Vector::copyFrom);
    }

    public static <A> Shrink<HashSet<A>> shrinkNonEmptyHashSet(Shrink<A> element) {
        return shrinkNonEmptyVector(element)
                .convert(vector -> vector.toCollection(HashSet::new),
                        hashSet -> Vector.copyFrom(hashSet).toNonEmptyOrThrow());
    }

    public static <A> Shrink<ImmutableVector<A>> shrinkVector(int minimumSize, Shrink<A> element) {
        return input -> {
            int size = input.size();
            if (size <= minimumSize) {
                return ShrinkResult.empty();
            }
            // TODO: implement ShrinkVector.  This is a start.
            return ShrinkResult.<ImmutableVector<A>>maybeCons(minimumSize == 0, Vector::empty,
                    () -> ShrinkResult.cons(evenElements(input),
                            () -> ShrinkResult.singleton(oddElements(input)))).apply();
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
