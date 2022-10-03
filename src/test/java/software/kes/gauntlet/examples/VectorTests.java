package software.kes.gauntlet.examples;

import org.junit.jupiter.api.Test;
import software.kes.gauntlet.Arbitraries;
import software.kes.gauntlet.GauntletApiBase;
import software.kes.gauntlet.Prop;

public final class VectorTests extends GauntletApiBase {

    @Test
    void reverseTwiceIsOriginal() {
        checkThat(all(Arbitraries.vectors())
                .satisfy(Prop.predicate("reverse twice is original",
                        xs -> xs.reverse().reverse().equals(xs))));
    }
}
