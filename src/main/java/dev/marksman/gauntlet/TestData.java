package dev.marksman.gauntlet;

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
    private final Shrink<A> shrinkStrategy;

    @Getter
    private final long initialSeedValue;
}
