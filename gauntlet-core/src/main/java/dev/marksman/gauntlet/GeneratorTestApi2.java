package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.GeneratorTest.generatorTest;

final class GeneratorTestApi2<A> implements GeneratorTestApi<A> {
    private final Consumer<GeneratorTest<A>> runner;
    private final GeneratorTestParameters<A> parameters;

    GeneratorTestApi2(Consumer<GeneratorTest<A>> runner, GeneratorTestParameters<A> parameters) {
        this.runner = runner;
        this.parameters = parameters;
    }

    @Override
    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new GeneratorTestApi2<>(runner, parameters.withSampleCount(sampleCount));
    }

    @Override
    public GeneratorTestApi<A> withInitialSeed(long initialSeed) {
        return new GeneratorTestApi2<>(runner, parameters.withInitialSeed(just(initialSeed)));
    }

    @Override
    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new GeneratorTestApi2<>(runner, parameters.withTimeout(just(timeout)));
    }

    @Override
    public GeneratorTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new GeneratorTestApi2<>(runner, parameters.addClassifier(classifier));
    }

    @Override
    public void mustSatisfy(Prop<A> property) {
        runner.accept(generatorTest(parameters, property));
    }

    static <A> GeneratorTestApi2<A> generatorTestApi2(Consumer<GeneratorTest<A>> runner, GeneratorTestParameters<A> parameters) {
        return new GeneratorTestApi2<>(runner, parameters);
    }

}
