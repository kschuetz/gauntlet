package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
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

    @Override
    public <A> GeneratorTestApi<A> all(Arbitrary<A> generator) {
        return getApi().all(generator);
    }

    @Override
    public <A, B> GeneratorTestApi<Tuple2<A, B>> all(Arbitrary<A> generatorA, Arbitrary<B> generatorB) {
        return getApi().all(generatorA, generatorB);
    }

    @Override
    public <A, B, C> GeneratorTestApi<Tuple3<A, B, C>> all(Arbitrary<A> generatorA, Arbitrary<B> generatorB, Arbitrary<C> generatorC) {
        return getApi().all(generatorA, generatorB, generatorC);
    }

    @Override
    public <A> DiscreteDomainTestApi<A> all(Iterable<A> domain) {
        return getApi().all(domain);
    }

    @Override
    public <A, B> DiscreteDomainTestApi<Tuple2<A, B>> all(Iterable<A> domainA,
                                                          Iterable<B> domainB) {
        return getApi().all(domainA, domainB);
    }

    @Override
    public <A, B, C> DiscreteDomainTestApi<Tuple3<A, B, C>> all(Iterable<A> domainA,
                                                                Iterable<B> domainB,
                                                                Iterable<C> domainC) {
        return getApi().all(domainA, domainB, domainC);
    }

    @Override
    public <A, B, C, D> DiscreteDomainTestApi<Tuple4<A, B, C, D>> all(Iterable<A> domainA,
                                                                      Iterable<B> domainB,
                                                                      Iterable<C> domainC,
                                                                      Iterable<D> domainD) {
        return getApi().all(domainA, domainB, domainC, domainD);
    }

    @Override
    public <A> DiscreteDomainTestApi<A> some(Iterable<A> domain) {
        return getApi().some(domain);
    }

    @Override
    public <A, B> DiscreteDomainTestApi<Tuple2<A, B>> some(Iterable<A> domainA,
                                                           Iterable<B> domainB) {
        return getApi().some(domainA, domainB);
    }

    @Override
    public <A, B, C> DiscreteDomainTestApi<Tuple3<A, B, C>> some(Iterable<A> domainA,
                                                                 Iterable<B> domainB,
                                                                 Iterable<C> domainC) {
        return getApi().some(domainA, domainB, domainC);
    }

    @Override
    public <A, B, C, D> DiscreteDomainTestApi<Tuple4<A, B, C, D>> some(Iterable<A> domainA,
                                                                       Iterable<B> domainB,
                                                                       Iterable<C> domainC,
                                                                       Iterable<D> domainD) {
        return getApi().some(domainA, domainB, domainC, domainD);
    }

    @Override
    public Executor getExecutor() {
        return getApi().getExecutor();
    }

    @Override
    public GeneratorTestRunner getGeneratorTestRunner() {
        return getApi().getGeneratorTestRunner();
    }

    @Override
    public Reporter getReporter() {
        return getApi().getReporter();
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
    public GauntletApi withExecutor(Executor executor) {
        return getApi().withExecutor(executor);
    }

    @Override
    public GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner) {
        return getApi().withGeneratorTestRunner(testRunner);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return getApi().withReporter(reporter);
    }

    @Override
    public GauntletApi withDefaultSampleCount(int sampleCount) {
        return getApi().withDefaultSampleCount(sampleCount);
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
