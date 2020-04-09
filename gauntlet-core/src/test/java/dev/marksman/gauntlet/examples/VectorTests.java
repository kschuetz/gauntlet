package dev.marksman.gauntlet.examples;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.gauntlet.Arbitrary;
import dev.marksman.gauntlet.GauntletApiBase;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.Arbitraries.arbitraryInt;
import static dev.marksman.gauntlet.Prop.predicate;

public class VectorTests extends GauntletApiBase {

    private static final Arbitrary<ImmutableVector<Integer>> vectors = arbitraryInt().vector();

    @Test
    void reverseTwiceIsOriginal() {
        all(vectors)
                .mustSatisfy(predicate("reverse twice is original",
                        xs -> xs.reverse().reverse().equals(xs)));
    }

}
