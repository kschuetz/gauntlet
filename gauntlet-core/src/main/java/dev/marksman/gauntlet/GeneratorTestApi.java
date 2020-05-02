package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.GeneratorTest.generatorTest;

public final class GeneratorTestApi<A> {
    private final Fn0<Executor> getParentExecutor;
    private final Consumer<GeneratorTest<A>> runner;
    private final GeneratorTestParameters<A> parameters;

    private GeneratorTestApi(Fn0<Executor> getParentExecutor, Consumer<GeneratorTest<A>> runner, GeneratorTestParameters<A> parameters) {
        this.getParentExecutor = getParentExecutor;
        this.runner = runner;
        this.parameters = parameters;
    }

    static <A> GeneratorTestApi<A> generatorTestApi(Fn0<Executor> getParentExecutor,
                                                    Consumer<GeneratorTest<A>> runner,
                                                    GeneratorTestParameters<A> parameters) {
        return new GeneratorTestApi<>(getParentExecutor, runner, parameters);
    }

    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new GeneratorTestApi<>(getParentExecutor, runner, parameters.withSampleCount(sampleCount));
    }

    public GeneratorTestApi<A> withInitialSeed(long initialSeed) {
        return new GeneratorTestApi<>(getParentExecutor, runner, parameters.withInitialSeed(just(initialSeed)));
    }

    public GeneratorTestApi<A> withMaximumShrinkCount(int maximumShrinkCount) {
        return new GeneratorTestApi<>(getParentExecutor, runner, parameters.withMaximumShrinkCount(maximumShrinkCount));
    }

    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new GeneratorTestApi<>(getParentExecutor, runner, parameters.withTimeout(timeout));
    }

    public GeneratorTestApi<A> withExecutor(Executor executor) {
        return new GeneratorTestApi<>(getParentExecutor, runner, parameters.withExecutorOverride(just(executor)));
    }

    public void mustSatisfy(Prop<A> property) {
        GeneratorTest<A> generatorTest = generatorTest(parameters.getArbitrary(), property, parameters.getInitialSeed(), parameters.getSampleCount(),
                parameters.getMaximumShrinkCount(), parameters.getTimeout(), parameters.getExecutorOverride().orElseGet(getParentExecutor),
                parameters.getGeneratorParameters());
        runner.accept(generatorTest);
    }

    public void mustNotSatisfy(Prop<A> prop) {
        mustSatisfy(prop.not());
    }

}
