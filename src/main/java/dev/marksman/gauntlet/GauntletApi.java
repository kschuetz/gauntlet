package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
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

    static <A, B, C, D> GeneratorTestApi<Tuple4<A, B, C, D>> all(Arbitrary<A> arbitraryA,
                                                                 Arbitrary<B> arbitraryB,
                                                                 Arbitrary<C> arbitraryC,
                                                                 Arbitrary<D> arbitraryD) {
        return generatorTestApi(Arbitraries.tuplesOf(arbitraryA, arbitraryB, arbitraryC, arbitraryD));
    }

    static <A, B, C, D, E> GeneratorTestApi<Tuple5<A, B, C, D, E>> all(Arbitrary<A> arbitraryA,
                                                                       Arbitrary<B> arbitraryB,
                                                                       Arbitrary<C> arbitraryC,
                                                                       Arbitrary<D> arbitraryD,
                                                                       Arbitrary<E> arbitraryE) {
        return generatorTestApi(Arbitraries.tuplesOf(arbitraryA, arbitraryB, arbitraryC, arbitraryD, arbitraryE));
    }

    static <A, B, C, D, E, F> GeneratorTestApi<Tuple6<A, B, C, D, E, F>> all(Arbitrary<A> arbitraryA,
                                                                             Arbitrary<B> arbitraryB,
                                                                             Arbitrary<C> arbitraryC,
                                                                             Arbitrary<D> arbitraryD,
                                                                             Arbitrary<E> arbitraryE,
                                                                             Arbitrary<F> arbitraryF) {
        return generatorTestApi(Arbitraries.tuplesOf(arbitraryA, arbitraryB, arbitraryC, arbitraryD, arbitraryE, arbitraryF));
    }

    static <A, B, C, D, E, F, G> GeneratorTestApi<Tuple7<A, B, C, D, E, F, G>> all(Arbitrary<A> arbitraryA,
                                                                                   Arbitrary<B> arbitraryB,
                                                                                   Arbitrary<C> arbitraryC,
                                                                                   Arbitrary<D> arbitraryD,
                                                                                   Arbitrary<E> arbitraryE,
                                                                                   Arbitrary<F> arbitraryF,
                                                                                   Arbitrary<G> arbitraryG) {
        return generatorTestApi(Arbitraries.tuplesOf(arbitraryA, arbitraryB, arbitraryC, arbitraryD, arbitraryE, arbitraryF, arbitraryG));
    }

    static <A, B, C, D, E, F, G, H> GeneratorTestApi<Tuple8<A, B, C, D, E, F, G, H>> all(Arbitrary<A> arbitraryA,
                                                                                         Arbitrary<B> arbitraryB,
                                                                                         Arbitrary<C> arbitraryC,
                                                                                         Arbitrary<D> arbitraryD,
                                                                                         Arbitrary<E> arbitraryE,
                                                                                         Arbitrary<F> arbitraryF,
                                                                                         Arbitrary<G> arbitraryG,
                                                                                         Arbitrary<H> arbitraryH) {
        return generatorTestApi(Arbitraries.tuplesOf(arbitraryA, arbitraryB, arbitraryC, arbitraryD, arbitraryE, arbitraryF,
                arbitraryG, arbitraryH));
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

    <A> void checkThat(Test<A> test);

    <A> void checkThatWithSeed(long initialSeedValue, GeneratorTest<A> generatorTest);

    <A, P> void checkThatForEach(TestParametersSource<P> parametersSource, Fn1<P, Test<A>> createTest);

    <A, P> void checkThatForEachWithSeed(long initialSeedValue, TestParametersSource<P> parametersSource, Fn1<P, Test<A>> createTest);

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

