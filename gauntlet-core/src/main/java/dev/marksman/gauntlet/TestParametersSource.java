package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static java.util.Arrays.asList;

public interface TestParametersSource<A> {
    static <A> GenerateParametersForTest<A> generateParametersForTests(int sampleCount, Generator<A> generator) {
        return GenerateParametersForTest.generateParametersForTest(sampleCount, generator);
    }

    @SafeVarargs
    static <A> TestParametersSource<A> testParametersOf(A first, A... more) {
        return EnumeratedTestParameters.enumeratedTestParameters(cons(first, asList(more)));
    }

    static <A> TestParametersSource<A> testParametersSource(Iterable<A> params) {
        return EnumeratedTestParameters.enumeratedTestParameters(params);
    }

    TestParameterCollection<A> getTestParameterCollection(GeneratorParameters generatorParameters,
                                                          Seed inputSeed);
}
