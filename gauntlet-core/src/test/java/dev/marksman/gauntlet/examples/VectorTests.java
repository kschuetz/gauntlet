package dev.marksman.gauntlet.examples;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.gauntlet.Arbitrary;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.Arbitraries.arbitraryInt;
import static dev.marksman.gauntlet.Gauntlet.GAUNTLET;
import static dev.marksman.gauntlet.Prop.prop;

public class VectorTests {

    private static final Arbitrary<ImmutableVector<Integer>> vectors = arbitraryInt().vector();

    @Test
    void reverseTwiceIsOriginal() {
        GAUNTLET.all(vectors).mustSatisfy(prop("reverse twice is original",
                xs -> xs.reverse().reverse().equals(xs)));
    }

}
