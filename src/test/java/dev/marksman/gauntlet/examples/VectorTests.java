package dev.marksman.gauntlet.examples;

import dev.marksman.gauntlet.GauntletApiBase;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.Arbitraries.vectors;
import static dev.marksman.gauntlet.Prop.predicate;

public final class VectorTests extends GauntletApiBase {

    @Test
    void reverseTwiceIsOriginal() {
        checkThat(all(vectors())
                .satisfy(predicate("reverse twice is original",
                        xs -> xs.reverse().reverse().equals(xs))));
    }
}
