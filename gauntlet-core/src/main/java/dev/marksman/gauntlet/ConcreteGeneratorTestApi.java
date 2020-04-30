package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.GeneratorTest.generatorTest;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class ConcreteGeneratorTestApi<A> implements GeneratorTestApi<A> {
    private final Fn0<Executor> getParentExecutor;
    private final Consumer<GeneratorTest<A>> runner;
    private final GeneratorTestParameters<A> parameters;

    @Override
    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new ConcreteGeneratorTestApi<>(getParentExecutor, runner, parameters.withSampleCount(sampleCount));
    }

    @Override
    public GeneratorTestApi<A> withInitialSeed(long initialSeed) {
        return new ConcreteGeneratorTestApi<>(getParentExecutor, runner, parameters.withInitialSeed(just(initialSeed)));
    }

    @Override
    public GeneratorTestApi<A> withMaximumShrinkCount(int maximumShrinkCount) {
        return new ConcreteGeneratorTestApi<>(getParentExecutor, runner, parameters.withMaximumShrinkCount(maximumShrinkCount));
    }

    @Override
    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new ConcreteGeneratorTestApi<>(getParentExecutor, runner, parameters.withTimeout(timeout));
    }

    @Override
    public GeneratorTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new ConcreteGeneratorTestApi<>(getParentExecutor, runner, parameters.addClassifier(classifier));
    }

    @Override
    public GeneratorTestApi<A> withExecutor(Executor executor) {
        return new ConcreteGeneratorTestApi<>(getParentExecutor, runner, parameters.withExecutorOverride(just(executor)));
    }

    @Override
    public void mustSatisfy(Prop<A> property) {
        GeneratorTest<A> generatorTest = generatorTest(parameters.getArbitrary(), property, parameters.getInitialSeed(), parameters.getSampleCount(),
                parameters.getMaximumShrinkCount(), parameters.getClassifiers(), parameters.getTimeout(),
                parameters.getExecutorOverride().orElseGet(getParentExecutor), parameters.getGeneratorParameters());
        runner.accept(generatorTest);
    }

    static <A> ConcreteGeneratorTestApi<A> concreteGeneratorTestApi(Fn0<Executor> getParentExecutor,
                                                                    Consumer<GeneratorTest<A>> runner,
                                                                    GeneratorTestParameters<A> parameters) {
        return new ConcreteGeneratorTestApi<>(getParentExecutor, runner, parameters);
    }

}
