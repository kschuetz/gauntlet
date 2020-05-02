package dev.marksman.gauntlet;

import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import java.time.Duration;
import java.util.concurrent.Executor;

final class RefinementTest<A> {
    static final int DEFAULT_BLOCK_SIZE = 16;

    private final ShrinkStrategy<A> shrinkStrategy;
    private final Prop<A> property;
    private final A sample;
    private final int maximumShrinkCount;
    private final Duration timeout;
    private final Executor executor;
    private final int blockSize;

    private RefinementTest(ShrinkStrategy<A> shrinkStrategy, Prop<A> property, A sample, int maximumShrinkCount,
                           Duration timeout, Executor executor, int blockSize) {
        this.shrinkStrategy = shrinkStrategy;
        this.property = property;
        this.sample = sample;
        this.maximumShrinkCount = maximumShrinkCount;
        this.timeout = timeout;
        this.executor = executor;
        this.blockSize = blockSize;
    }

    static <A> RefinementTest<A> refinementTest(ShrinkStrategy<A> shrinkStrategy,
                                                Prop<A> property,
                                                A sample,
                                                int maximumShrinkCount,
                                                Duration timeout,
                                                Executor executor,
                                                int blockSize) {
        return new RefinementTest<>(shrinkStrategy, property, sample, maximumShrinkCount, timeout, executor, blockSize);
    }

    static <A> RefinementTest<A> refinementTest(ShrinkStrategy<A> shrinkStrategy,
                                                Prop<A> property,
                                                A sample,
                                                int maximumShrinkCount,
                                                Duration timeout,
                                                Executor executor) {
        return refinementTest(shrinkStrategy, property, sample, maximumShrinkCount, timeout, executor, DEFAULT_BLOCK_SIZE);
    }

    public ShrinkStrategy<A> getShrinkStrategy() {
        return this.shrinkStrategy;
    }

    public Prop<A> getProperty() {
        return this.property;
    }

    public A getSample() {
        return this.sample;
    }

    public int getMaximumShrinkCount() {
        return this.maximumShrinkCount;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public int getBlockSize() {
        return this.blockSize;
    }
}
