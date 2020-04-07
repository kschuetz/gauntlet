package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.util.concurrent.Executor;

public interface GauntletApi extends GauntletEnvironment {
    int getDefaultSampleCount();

    int getDefaultMaxDiscards();

    GauntletApi withExecutor(Executor executor);

    GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner);

    GauntletApi withReporter(Reporter reporter);

    GauntletApi withDefaultSampleCount(int sampleCount);

    GauntletApi withDefaultMaxDiscards(int maxDiscards);

    GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters);

    <A> GeneratorTestBuilder<A> all(Arbitrary<A> generator);

    <A, B> GeneratorTestBuilder<Tuple2<A, B>> all(Arbitrary<A> generatorA,
                                                  Arbitrary<B> generatorB);

    <A, B, C> GeneratorTestBuilder<Tuple3<A, B, C>> all(Arbitrary<A> generatorA,
                                                        Arbitrary<B> generatorB,
                                                        Arbitrary<C> generatorC);
}
