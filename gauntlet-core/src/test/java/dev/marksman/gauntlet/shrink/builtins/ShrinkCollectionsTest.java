package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.shrink.builtins.ShrinkCollections.shrinkVector;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class ShrinkCollectionsTest {

    @Test
    void shrinkVectorStructure() {
        Shrink<ImmutableVector<Integer>> shrink = shrinkVector(Shrink.none());

        ImmutableFiniteIterable<ImmutableVector<Integer>> output = shrink.apply(Vector.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertThat(output, contains(
                Vector.empty(),
                Vector.of(0, 2, 4, 6, 8),
                Vector.of(1, 3, 5, 7, 9)
        ));
    }

}