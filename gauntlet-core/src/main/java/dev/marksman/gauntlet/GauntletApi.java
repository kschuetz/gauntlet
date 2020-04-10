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

    <A> DomainTestApi<A> all(Domain<A> domain);

    <A, B> DomainTestApi<Tuple2<A, B>> all(Domain<A> domainA,
                                           Domain<B> domainB);

    <A, B, C> DomainTestApi<Tuple3<A, B, C>> all(Domain<A> domainA,
                                                 Domain<B> domainB,
                                                 Domain<C> domainC);

    <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> all(Domain<A> domainA,
                                                       Domain<B> domainB,
                                                       Domain<C> domainC,
                                                       Domain<D> domainD);

    <A> DomainTestApi<A> some(Domain<A> domain);

    <A, B> DomainTestApi<Tuple2<A, B>> some(Domain<A> domainA,
                                            Domain<B> domainB);

    <A, B, C> DomainTestApi<Tuple3<A, B, C>> some(Domain<A> domainA,
                                                  Domain<B> domainB,
                                                  Domain<C> domainC);

    <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> some(Domain<A> domainA,
                                                        Domain<B> domainB,
                                                        Domain<C> domainC,
                                                        Domain<D> domainD);

    Executor getExecutor();

    GeneratorTestRunner getGeneratorTestRunner();

    DomainTestRunner getDomainTestRunner();

    Reporter getReporter();

    GeneratorParameters getGeneratorParameters();

    Duration getDefaultTimeout();

    int getDefaultSampleCount();

    GauntletApi withExecutor(Executor executor);

    GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner);

    GauntletApi withDomainTestRunner(DomainTestRunner testRunner);

    GauntletApi withReporter(Reporter reporter);

    GauntletApi withDefaultSampleCount(int sampleCount);

    GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters);

    GauntletApi withDefaultTimeout(Duration timeout);
}
