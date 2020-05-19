package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.Domain.cartesianProduct;
import static dev.marksman.gauntlet.DomainTestApi.domainTestApi;
import static dev.marksman.gauntlet.GeneratorTestApi.generatorTestApi;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.Quantifier.UNIVERSAL;

public interface GauntletApi {

    static <A> GeneratorTestApi<A> all(Arbitrary<A> arbitrary) {
        return generatorTestApi(arbitrary);
    }

    static <A, B> GeneratorTestApi<Tuple2<A, B>> all(Arbitrary<A> arbitraryA,
                                                     Arbitrary<B> arbitraryB) {
        return generatorTestApi(Arbitraries.tuplesOf(arbitraryA, arbitraryB));
    }

    static <A, B, C> GeneratorTestApi<Tuple3<A, B, C>> all(Arbitrary<A> arbitraryA,
                                                           Arbitrary<B> arbitraryB,
                                                           Arbitrary<C> arbitraryC) {
        return generatorTestApi(Arbitraries.tuplesOf(arbitraryA, arbitraryB, arbitraryC));
    }

    static <A> DomainTestApi<A> all(Domain<A> domain) {
        return domainTestApi(UNIVERSAL, domain);
    }

    static <A, B> DomainTestApi<Tuple2<A, B>> all(Domain<A> domainA,
                                                  Domain<B> domainB) {
        return domainTestApi(UNIVERSAL, cartesianProduct(domainA, domainB));
    }

    static <A, B, C> DomainTestApi<Tuple3<A, B, C>> all(Domain<A> domainA,
                                                        Domain<B> domainB,
                                                        Domain<C> domainC) {
        return domainTestApi(UNIVERSAL, cartesianProduct(domainA, domainB, domainC));
    }

    static <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> all(Domain<A> domainA,
                                                              Domain<B> domainB,
                                                              Domain<C> domainC,
                                                              Domain<D> domainD) {
        return domainTestApi(UNIVERSAL, cartesianProduct(domainA, domainB, domainC, domainD));
    }

    static <A> DomainTestApi<A> some(Domain<A> domain) {
        return domainTestApi(EXISTENTIAL, domain);
    }

    static <A, B> DomainTestApi<Tuple2<A, B>> some(Domain<A> domainA,
                                                   Domain<B> domainB) {
        return domainTestApi(EXISTENTIAL, cartesianProduct(domainA, domainB));
    }

    static <A, B, C> DomainTestApi<Tuple3<A, B, C>> some(Domain<A> domainA,
                                                         Domain<B> domainB,
                                                         Domain<C> domainC) {
        return domainTestApi(EXISTENTIAL, cartesianProduct(domainA, domainB, domainC));
    }

    static <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> some(Domain<A> domainA,
                                                               Domain<B> domainB,
                                                               Domain<C> domainC,
                                                               Domain<D> domainD) {
        return domainTestApi(EXISTENTIAL, cartesianProduct(domainA, domainB, domainC, domainD));
    }

    <A> void assertThat(GeneratorTest<A> generatorTest);

    <A> void assertWithSeed(long initialSeedValue, GeneratorTest<A> generatorTest);

    <A, P> void assertForEach(TestParametersSource<P> parametersSource, Fn1<P, GeneratorTest<A>> createTest);

    <A, P> void assertForEachWithSeed(long initialSeedValue, TestParametersSource<P> parametersSource, Fn1<P, GeneratorTest<A>> createTest);

    <A> void assertThat(DomainTest<A> domainTest);

    Reporter getReporter();

    ReportSettings getReportSettings();

    ReportRenderer getReportRenderer();

    GeneratorParameters getGeneratorParameters();

    Duration getDefaultTimeout();

    int getDefaultSampleCount();

    int getDefaultMaximumShrinkCount();

    GauntletApi withExecutor(Executor executor);

    GauntletApi withReporter(Reporter reporter);

    GauntletApi withReportSettings(ReportSettings reportSettings);

    GauntletApi withReportRenderer(ReportRenderer renderer);

    GauntletApi withDefaultSampleCount(int sampleCount);

    GauntletApi withDefaultMaximumShrinkCount(int maximumShrinkCount);

    GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters);

    GauntletApi withDefaultTimeout(Duration timeout);
}

