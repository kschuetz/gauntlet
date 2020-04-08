package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.ConcreteGeneratorTestApi.concreteGeneratorTestApi;
import static dev.marksman.gauntlet.GeneratorTestExecutionParameters.generatorTestExecutionParameters;
import static dev.marksman.gauntlet.GeneratorTestParameters.generatorTestParameters;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.Quantifier.UNIVERSAL;
import static dev.marksman.gauntlet.ReportData.reportData;

class DefaultGauntlet implements GauntletApi {
    private final Executor executor;
    private final GeneratorTestRunner generatorTestRunner;
    private final Reporter reporter;
    private final GeneratorParameters generatorParameters;
    private final int defaultSampleCount;
    private final Duration defaultTimeout;

    public DefaultGauntlet(Executor executor, GeneratorTestRunner generatorTestRunner, Reporter reporter, GeneratorParameters generatorParameters, int defaultSampleCount, Duration defaultTimeout) {
        this.executor = executor;
        this.generatorTestRunner = generatorTestRunner;
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
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, sampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner) {
        return new DefaultGauntlet(executor, testRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, defaultTimeout);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, timeout);
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
    public <A> DiscreteDomainTestApi<A> all(Iterable<A> domain) {
        return createDiscreteDomainTestApi(UNIVERSAL, Vector.copyFrom(domain));
    }

    @Override
    public <A, B> DiscreteDomainTestApi<Tuple2<A, B>> all(Iterable<A> domainA, Iterable<B> domainB) {
        return createDiscreteDomainTestApi(UNIVERSAL, combineDomains(Vector.copyFrom(domainA), Vector.copyFrom(domainB)));
    }

    @Override
    public <A, B, C> DiscreteDomainTestApi<Tuple3<A, B, C>> all(Iterable<A> domainA, Iterable<B> domainB, Iterable<C> domainC) {
        return createDiscreteDomainTestApi(UNIVERSAL, combineDomains(Vector.copyFrom(domainA), Vector.copyFrom(domainB),
                Vector.copyFrom(domainC)));
    }

    @Override
    public <A, B, C, D> DiscreteDomainTestApi<Tuple4<A, B, C, D>> all(Iterable<A> domainA, Iterable<B> domainB, Iterable<C> domainC, Iterable<D> domainD) {
        return createDiscreteDomainTestApi(UNIVERSAL, combineDomains(Vector.copyFrom(domainA), Vector.copyFrom(domainB),
                Vector.copyFrom(domainC), Vector.copyFrom(domainD)));
    }

    @Override
    public <A> DiscreteDomainTestApi<A> some(Iterable<A> domain) {
        return createDiscreteDomainTestApi(EXISTENTIAL, Vector.copyFrom(domain));
    }

    @Override
    public <A, B> DiscreteDomainTestApi<Tuple2<A, B>> some(Iterable<A> domainA, Iterable<B> domainB) {
        return createDiscreteDomainTestApi(EXISTENTIAL, combineDomains(Vector.copyFrom(domainA), Vector.copyFrom(domainB)));
    }

    @Override
    public <A, B, C> DiscreteDomainTestApi<Tuple3<A, B, C>> some(Iterable<A> domainA, Iterable<B> domainB, Iterable<C> domainC) {
        return createDiscreteDomainTestApi(EXISTENTIAL, combineDomains(Vector.copyFrom(domainA), Vector.copyFrom(domainB),
                Vector.copyFrom(domainC)));
    }

    @Override
    public <A, B, C, D> DiscreteDomainTestApi<Tuple4<A, B, C, D>> some(Iterable<A> domainA, Iterable<B> domainB, Iterable<C> domainC, Iterable<D> domainD) {
        return createDiscreteDomainTestApi(EXISTENTIAL, combineDomains(Vector.copyFrom(domainA), Vector.copyFrom(domainB),
                Vector.copyFrom(domainC), Vector.copyFrom(domainD)));
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

    private <A> void runDiscreteDomainTest(DiscreteDomainTest<A> discreteDomainTest) {
        throw new UnsupportedOperationException("TODO");
    }

    private <A> DiscreteDomainTestApi<A> createDiscreteDomainTestApi(Quantifier quantifier, ImmutableVector<A> domain) {
        return ConcreteDiscreteDomainTestApi.concreteGeneratorTestApi(this::runDiscreteDomainTest,
                DiscreteDomainTestParameters.discreteDomainTestParameters(domain, quantifier, Vector.empty(), nothing()));
    }

    private static <A, B> ImmutableVector<Tuple2<A, B>> combineDomains(ImmutableVector<A> domainA,
                                                                       ImmutableVector<B> domainB) {
        return domainA.cross(domainB);
    }

    private static <A, B, C> ImmutableVector<Tuple3<A, B, C>> combineDomains(ImmutableVector<A> domainA,
                                                                             ImmutableVector<B> domainB,
                                                                             ImmutableVector<C> domainC) {
        return domainA.cross(domainB.cross(domainC))
                .fmap(t -> Tuple3.tuple(
                        t._1(),
                        t._2()._1(),
                        t._2()._2()));
    }

    private static <A, B, C, D> ImmutableVector<Tuple4<A, B, C, D>> combineDomains(ImmutableVector<A> domainA,
                                                                                   ImmutableVector<B> domainB,
                                                                                   ImmutableVector<C> domainC,
                                                                                   ImmutableVector<D> domainD) {
        return domainA.cross(domainB.cross(domainC.cross(domainD)))
                .fmap(t -> Tuple4.tuple(
                        t._1(),
                        t._2()._1(),
                        t._2()._2()._1(),
                        t._2()._2()._2()));
    }

}
