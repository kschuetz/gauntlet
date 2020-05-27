package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.shrink.ShrinkResult;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

final class ShrinkCollection {
    private ShrinkCollection() {
    }

    static <A> ShrinkStrategy<ImmutableVector<A>> shrinkCollection(int minimumSize, ShrinkStrategy<A> element) {
        if (minimumSize < 0) {
            throw new IllegalArgumentException("minimumSize must be >= 0");
        }
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
