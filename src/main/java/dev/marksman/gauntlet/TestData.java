package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
class TestData<A> {
    @Getter
    private final Prop<A> property;

    @Getter
    private final ImmutableFiniteIterable<A> values;

    @Getter
    private final Maybe<Shrink<A>> shrinkStrategy;

    @Getter
    private final Maybe<Long> initialSeedValue;

}
