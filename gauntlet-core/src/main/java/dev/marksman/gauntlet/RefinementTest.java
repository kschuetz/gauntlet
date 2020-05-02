package dev.marksman.gauntlet;

import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class RefinementTest<A> {
    @Getter
    private final ShrinkStrategy<A> shrinkStrategy;
    @Getter
    private final Prop<A> property;
    @Getter
    private final A sample;
    @Getter
    private final int maximumShrinkCount;
    @Getter
    private final Duration timeout;

    public static <A> RefinementTest<A> refinementTest(ShrinkStrategy<A> shrinkStrategy,
                                                       Prop<A> property,
                                                       A sample,
                                                       int maximumShrinkCount,
                                                       Duration timeout) {
        return new RefinementTest<>(shrinkStrategy, property, sample, maximumShrinkCount, timeout);
    }

}
