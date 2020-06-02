package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

public interface SampleTypeMetadata<A> {
    Maybe<ShrinkStrategy<A>> getShrinkStrategy();

    PrettyPrinter<A> getPrettyPrinter();
}
