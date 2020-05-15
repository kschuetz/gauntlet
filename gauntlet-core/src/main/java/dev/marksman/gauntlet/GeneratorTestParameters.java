package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

final class GeneratorTestParameters<A> {
    private final Arbitrary<A> arbitrary;
    private final int sampleCount;
    private final int maximumShrinkCount;
    private final Duration timeout;
    private final Maybe<Executor> executorOverride;
    private final GeneratorParameters generatorParameters;

    private GeneratorTestParameters(Arbitrary<A> arbitrary,
                                    int sampleCount,
                                    int maximumShrinkCount,
                                    Duration timeout,
                                    Maybe<Executor> executorOverride,
                                    GeneratorParameters generatorParameters) {
        this.arbitrary = arbitrary;
        this.sampleCount = sampleCount;
        this.maximumShrinkCount = maximumShrinkCount;
        this.timeout = timeout;
        this.executorOverride = executorOverride;
        this.generatorParameters = generatorParameters;
    }

    public static <A> GeneratorTestParameters<A> generatorTestParameters(Arbitrary<A> arbitrary,
                                                                         int sampleCount,
                                                                         int maximumShrinkCount,
                                                                         Duration timeout,
                                                                         Maybe<Executor> executorOverride,
                                                                         GeneratorParameters generatorParameters) {
        return new GeneratorTestParameters<>(arbitrary, maximumShrinkCount, sampleCount, timeout,
                executorOverride, generatorParameters);
    }

    public Arbitrary<A> getArbitrary() {
        return this.arbitrary;
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

    public Maybe<Executor> getExecutorOverride() {
        return this.executorOverride;
    }

    public GeneratorParameters getGeneratorParameters() {
        return this.generatorParameters;
    }

    public GeneratorTestParameters<A> withArbitrary(Arbitrary<A> arbitrary) {
        return this.arbitrary == arbitrary
                ? this
                : new GeneratorTestParameters<>(arbitrary, this.sampleCount, this.maximumShrinkCount, this.timeout, this.executorOverride, this.generatorParameters);
    }

    public GeneratorTestParameters<A> withSampleCount(int sampleCount) {
        return this.sampleCount == sampleCount
                ? this
                : new GeneratorTestParameters<>(this.arbitrary, sampleCount, this.maximumShrinkCount, this.timeout, this.executorOverride, this.generatorParameters);
    }

    public GeneratorTestParameters<A> withMaximumShrinkCount(int maximumShrinkCount) {
        return this.maximumShrinkCount == maximumShrinkCount
                ? this
                : new GeneratorTestParameters<>(this.arbitrary, this.sampleCount, maximumShrinkCount, this.timeout, this.executorOverride, this.generatorParameters);
    }

    public GeneratorTestParameters<A> withTimeout(Duration timeout) {
        return this.timeout == timeout
                ? this
                : new GeneratorTestParameters<>(this.arbitrary, this.sampleCount, this.maximumShrinkCount, timeout, this.executorOverride, this.generatorParameters);
    }

    public GeneratorTestParameters<A> withExecutorOverride(Maybe<Executor> executorOverride) {
        return this.executorOverride == executorOverride
                ? this
                : new GeneratorTestParameters<>(this.arbitrary, this.sampleCount, this.maximumShrinkCount, this.timeout, executorOverride, this.generatorParameters);
    }

    public GeneratorTestParameters<A> withGeneratorParameters(GeneratorParameters generatorParameters) {
        return this.generatorParameters == generatorParameters
                ? this
                : new GeneratorTestParameters<>(this.arbitrary, this.sampleCount, this.maximumShrinkCount, this.timeout, this.executorOverride, generatorParameters);
    }
}
