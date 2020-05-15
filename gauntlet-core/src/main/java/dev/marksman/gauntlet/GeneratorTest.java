package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

final class GeneratorTest<A> {
    private final Arbitrary<A> arbitrary;
    private final Prop<A> property;
    private final int sampleCount;
    private final int maximumShrinkCount;
    private final Duration timeout;
    private final Executor executor;
    private final GeneratorParameters generatorParameters;

    private GeneratorTest(Arbitrary<A> arbitrary, Prop<A> property, int sampleCount,
                          int maximumShrinkCount,
                          Duration timeout, Executor executor, GeneratorParameters generatorParameters) {
        this.arbitrary = arbitrary;
        this.property = property;
        this.sampleCount = sampleCount;
        this.maximumShrinkCount = maximumShrinkCount;
        this.timeout = timeout;
        this.executor = executor;
        this.generatorParameters = generatorParameters;
    }

    static <A> GeneratorTest<A> generatorTest(Arbitrary<A> arbitrary,
                                              Prop<A> property,
                                              int sampleCount,
                                              int maximumShrinkCount,
                                              Duration timeout,
                                              Executor executor,
                                              GeneratorParameters generatorParameters) {
        return new GeneratorTest<>(arbitrary, property, sampleCount, maximumShrinkCount,
                timeout, executor, generatorParameters);
    }

    public Arbitrary<A> getArbitrary() {
        return this.arbitrary;
    }

    public Prop<A> getProperty() {
        return this.property;
    }

    public int getSampleCount() {
        return this.sampleCount;
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

    public GeneratorParameters getGeneratorParameters() {
        return this.generatorParameters;
    }
}
