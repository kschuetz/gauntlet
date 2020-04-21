package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.ConcreteDomainTestApi.concreteDomainTestApi;
import static dev.marksman.gauntlet.ConcreteGeneratorTestApi.concreteGeneratorTestApi;
import static dev.marksman.gauntlet.DomainTestExecutionParameters.domainTestExecutionParameters;
import static dev.marksman.gauntlet.DomainTestParameters.domainTestParameters;
import static dev.marksman.gauntlet.GeneratorTestExecutionParameters.generatorTestExecutionParameters;
import static dev.marksman.gauntlet.GeneratorTestParameters.generatorTestParameters;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.Quantifier.UNIVERSAL;
import static dev.marksman.gauntlet.ReportData.reportData;
import static dev.marksman.gauntlet.ShrinkTest.shrinkTest;
import static dev.marksman.gauntlet.ShrinkTestExecutionParameters.shrinkTestExecutionParameters;

class DefaultGauntlet implements GauntletApi {
    private final Executor executor;
    private final GeneratorTestRunner generatorTestRunner;
    private final DomainTestRunner domainTestRunner;
    private final ShrinkTestRunner shrinkTestRunner;
    private final Reporter reporter;
    private final GeneratorParameters generatorParameters;
    private final int defaultSampleCount;
    private final int defaultMaximumShrinkCount;
    private final Duration defaultTimeout;

    public DefaultGauntlet(Executor executor, GeneratorTestRunner generatorTestRunner, DomainTestRunner domainTestRunner,
                           ShrinkTestRunner shrinkTestRunner, Reporter reporter, GeneratorParameters generatorParameters,
                           int defaultSampleCount, int defaultMaximumShrinkCount, Duration defaultTimeout) {
        this.executor = executor;
        this.generatorTestRunner = generatorTestRunner;
        this.domainTestRunner = domainTestRunner;
        this.shrinkTestRunner = shrinkTestRunner;
        this.reporter = reporter;
        this.generatorParameters = generatorParameters;
        this.defaultSampleCount = defaultSampleCount;
        this.defaultMaximumShrinkCount = defaultMaximumShrinkCount;
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
    public ShrinkTestRunner getShrinkTestRunner() {
        return shrinkTestRunner;
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
    public int getDefaultMaximumShrinkCount() {
        return defaultMaximumShrinkCount;
    }

    @Override
    public GauntletApi withDefaultSampleCount(int sampleCount) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, shrinkTestRunner, reporter, generatorParameters, sampleCount, defaultMaximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withDefaultMaximumShrinkCount(int maximumShrinkCount) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, shrinkTestRunner, reporter, generatorParameters, defaultSampleCount, maximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, shrinkTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner) {
        return new DefaultGauntlet(executor, testRunner, domainTestRunner, shrinkTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withDomainTestRunner(DomainTestRunner testRunner) {
        return new DefaultGauntlet(executor, generatorTestRunner, testRunner, shrinkTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withShrinkTestRunner(ShrinkTestRunner testRunner) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, testRunner, reporter, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, shrinkTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, shrinkTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return new DefaultGauntlet(executor, generatorTestRunner, domainTestRunner, shrinkTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, timeout);
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
        return createDomainTestApi(UNIVERSAL, Domain.cartesianProduct(domainA, domainB));
    }

    @Override
    public <A, B, C> DomainTestApi<Tuple3<A, B, C>> all(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC) {
        return createDomainTestApi(UNIVERSAL, Domain.cartesianProduct(domainA, domainB, domainC));
    }

    @Override
    public <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> all(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC, Domain<D> domainD) {
        return createDomainTestApi(UNIVERSAL, Domain.cartesianProduct(domainA, domainB, domainC, domainD));
    }

    @Override
    public <A> DomainTestApi<A> some(Domain<A> domain) {
        return createDomainTestApi(EXISTENTIAL, domain);
    }

    @Override
    public <A, B> DomainTestApi<Tuple2<A, B>> some(Domain<A> domainA, Domain<B> domainB) {
        return createDomainTestApi(EXISTENTIAL, Domain.cartesianProduct(domainA, domainB));
    }

    @Override
    public <A, B, C> DomainTestApi<Tuple3<A, B, C>> some(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC) {
        return createDomainTestApi(EXISTENTIAL, Domain.cartesianProduct(domainA, domainB, domainC));
    }

    @Override
    public <A, B, C, D> DomainTestApi<Tuple4<A, B, C, D>> some(Domain<A> domainA, Domain<B> domainB, Domain<C> domainC, Domain<D> domainD) {
        return createDomainTestApi(EXISTENTIAL, Domain.cartesianProduct(domainA, domainB, domainC, domainD));
    }

    private <A> GeneratorTestApi<A> createGeneratorTestApi(Arbitrary<A> generator) {
        return concreteGeneratorTestApi(this::runGeneratorTest,
                generatorTestParameters(generator, nothing(), defaultSampleCount, defaultMaximumShrinkCount,
                        Vector.empty(), defaultTimeout));
    }

    private <A> void runGeneratorTest(GeneratorTest<A> generatorTest) {
        GeneratorTestResult<A> result = generatorTestRunner.run(
                generatorTestExecutionParameters(getExecutor(), getGeneratorParameters()),
                generatorTest)
                .flatMap(res -> refineResult(generatorTest, res))
                .unsafePerformIO();
        ReportData<A> reportData = reportData(generatorTest.getProperty(), result.getResult(), generatorTest.getArbitrary().getPrettyPrinter(),
                just(result.getInitialSeedValue()));
        reporter.report(reportData);
    }

    private <A> void runDomainTest(DomainTest<A> domainTest) {
        DomainTestResult<A> result = domainTestRunner.run(
                domainTestExecutionParameters(getExecutor()),
                domainTest)
                .unsafePerformIO();
        ReportData<A> reportData = reportData(domainTest.getProperty(),
                result.getResult(),
                domainTest.getDomain().getPrettyPrinter(),
                nothing());
        reporter.report(reportData);
    }

    private <A> DomainTestApi<A> createDomainTestApi(Quantifier quantifier, Domain<A> domain) {
        return concreteDomainTestApi(this::runDomainTest,
                domainTestParameters(domain, quantifier, Vector.empty(), defaultTimeout));
    }

    private <A> IO<GeneratorTestResult<A>> refineResult(GeneratorTest<A> testData,
                                                        GeneratorTestResult<A> initialResult) {
        if (!(initialResult.getResult() instanceof TestResult.Falsified<?>)) {
            return io(initialResult);
        }
        TestResult.Falsified<A> falsified = (TestResult.Falsified<A>) initialResult.getResult();
        Shrink<A> shrink = testData.getArbitrary().getShrink().orElse(null);
        if (shrink == null) {
            return io(initialResult);
        }
        return shrinkTestRunner
                .run(shrinkTestExecutionParameters(executor),
                        shrinkTest(shrink, testData.getProperty(), falsified.getCounterexample().getSample(),
                                testData.getMaximumShrinkCount(), testData.getTimeout()))
                .fmap(maybeRefinedResult -> maybeRefinedResult
                        .match(__ -> initialResult,
                                refined -> initialResult.withResult(falsified.withRefinedCounterexample(refined))));
    }

}
