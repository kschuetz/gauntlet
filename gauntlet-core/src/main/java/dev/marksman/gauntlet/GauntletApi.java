package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

public interface GauntletApi {

    <A> GeneratorTestApi<A> all(Arbitrary<A> generator);

    <A, B> GeneratorTestApi<Tuple2<A, B>> all(Arbitrary<A> generatorA,
                                              Arbitrary<B> generatorB);

    <A, B, C> GeneratorTestApi<Tuple3<A, B, C>> all(Arbitrary<A> generatorA,
                                                    Arbitrary<B> generatorB,
                                                    Arbitrary<C> generatorC);

    <A> DiscreteDomainTestApi<A> all(Iterable<A> domain);

    <A, B> DiscreteDomainTestApi<Tuple2<A, B>> all(Iterable<A> domainA,
                                                   Iterable<B> domainB);

    <A, B, C> DiscreteDomainTestApi<Tuple3<A, B, C>> all(Iterable<A> domainA,
                                                         Iterable<B> domainB,
                                                         Iterable<C> domainC);

    <A, B, C, D> DiscreteDomainTestApi<Tuple4<A, B, C, D>> all(Iterable<A> domainA,
                                                               Iterable<B> domainB,
                                                               Iterable<C> domainC,
                                                               Iterable<D> domainD);

    <A> DiscreteDomainTestApi<A> some(Iterable<A> domain);

    <A, B> DiscreteDomainTestApi<Tuple2<A, B>> some(Iterable<A> domainA,
                                                    Iterable<B> domainB);

    <A, B, C> DiscreteDomainTestApi<Tuple3<A, B, C>> some(Iterable<A> domainA,
                                                          Iterable<B> domainB,
                                                          Iterable<C> domainC);

    <A, B, C, D> DiscreteDomainTestApi<Tuple4<A, B, C, D>> some(Iterable<A> domainA,
                                                                Iterable<B> domainB,
                                                                Iterable<C> domainC,
                                                                Iterable<D> domainD);

    Executor getExecutor();

    GeneratorTestRunner getGeneratorTestRunner();

    Reporter getReporter();

    GeneratorParameters getGeneratorParameters();

    Duration getDefaultTimeout();

    int getDefaultSampleCount();

    GauntletApi withExecutor(Executor executor);

    GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner);

    GauntletApi withReporter(Reporter reporter);

    GauntletApi withDefaultSampleCount(int sampleCount);

    GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters);

    GauntletApi withDefaultTimeout(Duration timeout);
}
