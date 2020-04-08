package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.GeneratorTest.generatorTest;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class ConcreteGeneratorTestApi<A> implements GeneratorTestApi<A> {
    private final Consumer<GeneratorTest<A>> runner;
    private final GeneratorTestParameters<A> parameters;

    @Override
    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new ConcreteGeneratorTestApi<>(runner, parameters.withSampleCount(sampleCount));
    }

    @Override
    public GeneratorTestApi<A> withInitialSeed(long initialSeed) {
        return new ConcreteGeneratorTestApi<>(runner, parameters.withInitialSeed(just(initialSeed)));
    }

    @Override
    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new ConcreteGeneratorTestApi<>(runner, parameters.withTimeout(just(timeout)));
    }

    @Override
    public GeneratorTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new ConcreteGeneratorTestApi<>(runner, parameters.addClassifier(classifier));
    }

    @Override
    public void mustSatisfy(Prop<A> property) {
        runner.accept(generatorTest(parameters, property));
    }

    static <A> ConcreteGeneratorTestApi<A> concreteGeneratorTestApi(Consumer<GeneratorTest<A>> runner, GeneratorTestParameters<A> parameters) {
        return new ConcreteGeneratorTestApi<>(runner, parameters);
    }

}
