package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
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

    GauntletApi withDefaultTimeout(Duration timeout);

    <A> GeneratorTestApi<A> all(Arbitrary<A> generator);

    <A, B> GeneratorTestApi<Tuple2<A, B>> all(Arbitrary<A> generatorA,
                                              Arbitrary<B> generatorB);

    <A, B, C> GeneratorTestApi<Tuple3<A, B, C>> all(Arbitrary<A> generatorA,
                                                    Arbitrary<B> generatorB,
                                                    Arbitrary<C> generatorC);
}
