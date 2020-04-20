package dev.marksman.gauntlet;

import dev.marksman.gauntlet.shrink.Shrink;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class ShrinkTest<A> {
    @Getter
    private final Shrink<A> shrink;
    @Getter
    private final Prop<A> property;
    @Getter
    private final A sample;
    @Getter
    private final int maximumShrinkCount;
    @Getter
    private final Duration timeout;

    public static <A> ShrinkTest<A> shrinkTest(Shrink<A> shrink,
                                               Prop<A> property,
                                               A sample,
                                               int maximumShrinkCount,
                                               Duration timeout) {
        return new ShrinkTest<>(shrink, property, sample, maximumShrinkCount, timeout);
    }

}
