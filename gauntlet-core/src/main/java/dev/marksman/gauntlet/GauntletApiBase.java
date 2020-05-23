package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * Extend a test from this class for convenience to bring all GauntletApi methods into scope.
 * <p>
 * You can override `initializeGauntletApi` (which is called at most once) to set up the API with
 * custom settings.
 * <p>
 * All `withX` methods from the API are also available.
 */
public abstract class GauntletApiBase implements GauntletApi {
    private GauntletApi apiInstance;

    protected GauntletApi initializeGauntletApi() {
        return Gauntlet.gauntlet();
    }

    private GauntletApi getApi() {
        if (apiInstance == null) {
            synchronized (this) {
                if (apiInstance == null) {
                    apiInstance = initializeGauntletApi();
                }
            }
        }
        return apiInstance;
    }

    public <A> GeneratorTestApi<A> all(Arbitrary<A> arbitrary) {
        return GauntletApi.all(arbitrary);
    }

    public <A, B> GeneratorTestApi<Tuple2<A, B>> all(Arbitrary<A> arbitraryA,
                                                     Arbitrary<B> arbitraryB) {
        return GauntletApi.all(arbitraryA, arbitraryB);
    }

    public <A, B, C> GeneratorTestApi<Tuple3<A, B, C>> all(Arbitrary<A> arbitraryA,
                                                           Arbitrary<B> arbitraryB,
                                                           Arbitrary<C> arbitraryC) {
        return GauntletApi.all(arbitraryA, arbitraryB, arbitraryC);
    }

    public <A> DomainTestApi<A> all(Domain<A> domain) {
        return GauntletApi.all(domain);
    }

    public <A, B> DomainTestApi<Tuple2<A, B>> all(Domain<A> domainA,
                                                  Domain<B> domainB) {
        return GauntletApi.all(domainA, domainB);
    }

    public <A, B, C> DomainTestApi<Tuple3<A, B, C>> all(Domain<A> domainA,
                                                        Domain<B> domainB,
                                                        Domain<C> domainC) {
        return GauntletApi.all(domainA, domainB, domainC);
    }

    public <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> all(Domain<A> domainA,
                                                              Domain<B> domainB,
                                                              Domain<C> domainC,
                                                              Domain<D> domainD) {
        return GauntletApi.all(domainA, domainB, domainC, domainD);
    }

    public <A> DomainTestApi<A> some(Domain<A> domain) {
        return GauntletApi.some(domain);
    }

    public <A, B> DomainTestApi<Tuple2<A, B>> some(Domain<A> domainA,
                                                   Domain<B> domainB) {
        return GauntletApi.some(domainA, domainB);
    }

    public <A, B, C> DomainTestApi<Tuple3<A, B, C>> some(Domain<A> domainA,
                                                         Domain<B> domainB,
                                                         Domain<C> domainC) {
        return GauntletApi.some(domainA, domainB, domainC);
    }

    public <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> some(Domain<A> domainA,
                                                               Domain<B> domainB,
                                                               Domain<C> domainC,
                                                               Domain<D> domainD) {
        return GauntletApi.some(domainA, domainB, domainC, domainD);
    }

    @Override
    public <A> void assertThat(GeneratorTest<A> generatorTest) {
        getApi().assertThat(generatorTest);
    }

    @Override
    public <A> void assertThatWithSeed(long initialSeedValue, GeneratorTest<A> generatorTest) {
        getApi().assertThatWithSeed(initialSeedValue, generatorTest);
    }

    @Override
    public <A, P> void assertForEach(TestParametersSource<P> parametersSource, Fn1<P, GeneratorTest<A>> createTest) {
        getApi().assertForEach(parametersSource, createTest);
    }

    @Override
    public <A, P> void assertForEachWithSeed(long initialSeedValue, TestParametersSource<P> parametersSource, Fn1<P, GeneratorTest<A>> createTest) {
        getApi().assertForEachWithSeed(initialSeedValue, parametersSource, createTest);
    }

    @Override
    public <A> void assertThat(DomainTest<A> domainTest) {
        getApi().assertThat(domainTest);
    }

    @Override
    public Reporter getReporter() {
        return getApi().getReporter();
    }

    @Override
    public ReportSettings getReportSettings() {
        return getApi().getReportSettings();
    }

    @Override
    public ReportRenderer getReportRenderer() {
        return getApi().getReportRenderer();
    }

    @Override
    public GeneratorParameters getGeneratorParameters() {
        return getApi().getGeneratorParameters();
    }

    @Override
    public Duration getDefaultTimeout() {
        return getApi().getDefaultTimeout();
    }

    @Override
    public int getDefaultSampleCount() {
        return getApi().getDefaultSampleCount();
    }

    @Override
    public int getDefaultMaximumShrinkCount() {
        return getApi().getDefaultMaximumShrinkCount();
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return getApi().withExecutor(executor);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return getApi().withReporter(reporter);
    }

    @Override
    public GauntletApi withReportSettings(ReportSettings reportSettings) {
        return getApi().withReportSettings(reportSettings);
    }

    @Override
    public GauntletApi withReportRenderer(ReportRenderer reportRenderer) {
        return getApi().withReportRenderer(reportRenderer);
    }

    @Override
    public GauntletApi withDefaultSampleCount(int sampleCount) {
        return getApi().withDefaultSampleCount(sampleCount);
    }

    @Override
    public GauntletApi withDefaultMaximumShrinkCount(int maximumShrinkCount) {
        return getApi().withDefaultMaximumShrinkCount(maximumShrinkCount);
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return getApi().withGeneratorParameters(generatorParameters);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return getApi().withDefaultTimeout(timeout);
    }
}
