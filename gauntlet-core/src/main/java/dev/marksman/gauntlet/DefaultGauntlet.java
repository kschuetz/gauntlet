package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.ConcreteDomainTestApi.concreteGeneratorTestApi;
import static dev.marksman.gauntlet.ConcreteGeneratorTestApi.concreteGeneratorTestApi;
import static dev.marksman.gauntlet.Domain.combineDomains;
import static dev.marksman.gauntlet.DomainTestParameters.domainTestParameters;
import static dev.marksman.gauntlet.GeneratorTestExecutionParameters.generatorTestExecutionParameters;
import static dev.marksman.gauntlet.GeneratorTestParameters.generatorTestParameters;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.Quantifier.UNIVERSAL;
import static dev.marksman.gauntlet.ReportData.reportData;

class DefaultGauntlet implements GauntletApi {
    private final Executor executor;
    private final GeneratorTestRunner generatorTestRunner;
    private final DomainTestRunner domainTestRunner;
    private final Reporter reporter;
    private final GeneratorParameters generatorParameters;
    private final int defaultSampleCount;
    private final Duration defaultTimeout;

    public DefaultGauntlet(Executor executor, GeneratorTestRunner generatorTestRunner, DomainTestRunner domainTestRunner, Reporter reporter, GeneratorParameters generatorParameters, int defaultSampleCount, Duration defaultTimeout) {
        this.executor = executor;
        this.generatorTestRunner = generatorTestRunner;
        this.domainTestRunner = domainTestRunner;
        this.reporter = reporter;
        this.generatorParameters = generatorParameters;
        this.defaultSampleCount = defaultSampleCount;
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public GeneratorTestRunner getGeneratorTestRunner() {
        return generatorTestRunner;
    }

    @Override
    public DomainTestRunner getDomainTestRunner() {
        return domainTestRunner;
    }

    @Override
    public GeneratorParameters getGeneratorParameters() {
        return generatorParameters;
    }

    @Override
    public Reporter getReporter() {
        return reporter;
    }

    @Override
    public Duration getDefaultTimeout() {
        return defaultTimeout;
    }

    @Override
    public int getDefaultSampleCount() {
        return defaultSampleCount;
    }

    @Override
    public GauntletApi withDefaultSampleCount(int sampleCount) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, reporter, generatorParameters, sampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner) {
        return new DefaultGauntlet(executor, testRunner, domainTestRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withDomainTestRunner(DomainTestRunner testRunner) {
        return new DefaultGauntlet(executor, generatorTestRunner, testRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, reporter, generatorParameters, defaultSampleCount, timeout);
    }

    @Override
    public <A> GeneratorTestApi<A> all(Arbitrary<A> generator) {
        return createGeneratorTestApi(generator);
    }

    @Override
    public <A, B> GeneratorTestApi<Tuple2<A, B>> all(Arbitrary<A> generatorA, Arbitrary<B> generatorB) {
        return createGeneratorTestApi(CompositeArbitraries.combine(generatorA, generatorB));
    }

    @Override
    public <A, B, C> GeneratorTestApi<Tuple3<A, B, C>> all(Arbitrary<A> generatorA, Arbitrary<B> generatorB, Arbitrary<C> generatorC) {
        return createGeneratorTestApi(CompositeArbitraries.combine(generatorA, generatorB, generatorC));
    }

    @Override
    public <A> DomainTestApi<A> all(Domain<A> domain) {
        return createDomainTestApi(UNIVERSAL, domain);
    }

    @Override
    public <A, B> DomainTestApi<Tuple2<A, B>> all(Domain<A> domainA, Domain<B> domainB) {
        return createDomainTestApi(UNIVERSAL, combineDomains(domainA, domainB));
    }

    @Override
    public <A, B, C> DomainTestApi<Tuple3<A, B, C>> all(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC) {
        return createDomainTestApi(UNIVERSAL, combineDomains(domainA, domainB, domainC));
    }

    @Override
    public <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> all(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC, Domain<D> domainD) {
        return createDomainTestApi(UNIVERSAL, combineDomains(domainA, domainB, domainC, domainD));
    }

    @Override
    public <A> DomainTestApi<A> some(Domain<A> domain) {
        return createDomainTestApi(EXISTENTIAL, domain);
    }

    @Override
    public <A, B> DomainTestApi<Tuple2<A, B>> some(Domain<A> domainA, Domain<B> domainB) {
        return createDomainTestApi(EXISTENTIAL, combineDomains(domainA, domainB));
    }

    @Override
    public <A, B, C> DomainTestApi<Tuple3<A, B, C>> some(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC) {
        return createDomainTestApi(EXISTENTIAL, combineDomains(domainA, domainB, domainC));
    }

    @Override
    public <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> some(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC, Domain<D> domainD) {
        return createDomainTestApi(EXISTENTIAL, combineDomains(domainA, domainB, domainC, domainD));
    }

    private <A> GeneratorTestApi<A> createGeneratorTestApi(Arbitrary<A> generator) {
        return concreteGeneratorTestApi(this::runGeneratorTest,
                generatorTestParameters(generator, nothing(), defaultSampleCount, Vector.empty(), nothing()));
    }

    private <A> void runGeneratorTest(GeneratorTest<A> generatorTest) {
        GeneratorTestResult<A> result = generatorTestRunner.run(
                generatorTestExecutionParameters(getExecutor(), getGeneratorParameters(), defaultTimeout),
                generatorTest);
        ReportData<A> reportData = reportData(generatorTest.getProperty(), result.getResult(), generatorTest.getArbitrary().getPrettyPrinter(),
                just(result.getInitialSeedValue()));
        reporter.report(reportData);
    }

    private <A> void runDomainTest(DomainTest<A> domainTest) {
        throw new UnsupportedOperationException("TODO");
    }

    private <A> DomainTestApi<A> createDomainTestApi(Quantifier quantifier, Domain<A> domain) {
        return concreteGeneratorTestApi(this::runDomainTest,
                domainTestParameters(domain, quantifier, Vector.empty(), nothing()));
    }

}
