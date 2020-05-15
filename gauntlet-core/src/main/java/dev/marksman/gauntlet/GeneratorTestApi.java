package dev.marksman.gauntlet;

import java.time.Duration;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.GeneratorTest.generatorTest;

public final class GeneratorTestApi<A> {
    private final GeneratorTestParameters<A> parameters;

    private GeneratorTestApi(GeneratorTestParameters<A> parameters) {
        this.parameters = parameters;
    }

    static <A> GeneratorTestApi<A> generatorTestApi(GeneratorTestParameters<A> parameters) {
        return new GeneratorTestApi<>(parameters);
    }

    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new GeneratorTestApi<>(parameters.withSampleCount(sampleCount));
    }

    public GeneratorTestApi<A> withInitialSeed(long initialSeed) {
        return new GeneratorTestApi<>(parameters.withInitialSeed(just(initialSeed)));
    }

    public GeneratorTestApi<A> withMaximumShrinkCount(int maximumShrinkCount) {
        return new GeneratorTestApi<>(parameters.withMaximumShrinkCount(maximumShrinkCount));
    }

    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new GeneratorTestApi<>(parameters.withTimeout(timeout));
    }

    public GeneratorTestApi<A> withExecutor(Executor executor) {
        return new GeneratorTestApi<>(parameters.withExecutorOverride(just(executor)));
    }

    public GeneratorTest<A> mustSatisfy(Prop<A> property) {
        return generatorTest(parameters.getArbitrary(), property, parameters.getInitialSeed(), parameters.getSampleCount(),
                parameters.getMaximumShrinkCount(), parameters.getTimeout(), parameters.getExecutorOverride(),
                parameters.getGeneratorParameters());
    }

}
