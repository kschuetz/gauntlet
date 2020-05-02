package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.DomainTestApi.domainTestApi;
import static dev.marksman.gauntlet.DomainTestParameters.domainTestParameters;
import static dev.marksman.gauntlet.GeneratorTestApi.generatorTestApi;
import static dev.marksman.gauntlet.GeneratorTestParameters.generatorTestParameters;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.Quantifier.UNIVERSAL;
import static dev.marksman.gauntlet.RefinementTest.refinementTest;
import static dev.marksman.gauntlet.RefinementTestExecutionParameters.refinementTestExecutionParameters;
import static dev.marksman.gauntlet.ReportData.reportData;

final class Core implements GauntletApi {
    private final Maybe<Executor> executorOverride;
    private final GeneratorTestRunner generatorTestRunner;
    private final DomainTestRunner domainTestRunner;
    private final RefinementTestRunner refinementTestRunner;
    private final Reporter reporter;
    private final ReportSettings reportSettings;
    private final ReportRenderer reportRenderer;
    private final GeneratorParameters generatorParameters;
    private final int defaultSampleCount;
    private final int defaultMaximumShrinkCount;
    private final Duration defaultTimeout;

    public Core(GeneratorTestRunner generatorTestRunner,
                DomainTestRunner domainTestRunner,
                RefinementTestRunner refinementTestRunner,
                Reporter reporter,
                ReportSettings reportSettings,
                ReportRenderer reportRenderer,
                GeneratorParameters generatorParameters,
                int defaultSampleCount,
                int defaultMaximumShrinkCount,
                Duration defaultTimeout,
                Maybe<Executor> executorOverride) {
        this.executorOverride = executorOverride;
        this.generatorTestRunner = generatorTestRunner;
        this.domainTestRunner = domainTestRunner;
        this.refinementTestRunner = refinementTestRunner;
        this.reporter = reporter;
        this.reportSettings = reportSettings;
        this.reportRenderer = reportRenderer;
        this.generatorParameters = generatorParameters;
        this.defaultSampleCount = defaultSampleCount;
        this.defaultMaximumShrinkCount = defaultMaximumShrinkCount;
        this.defaultTimeout = defaultTimeout;
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
    public RefinementTestRunner getRefinementTestRunner() {
        return refinementTestRunner;
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
    public ReportSettings getReportSettings() {
        return reportSettings;
    }

    @Override
    public ReportRenderer getReportRenderer() {
        return reportRenderer;
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
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, sampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withDefaultMaximumShrinkCount(int maximumShrinkCount) {
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, maximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, just(executor));
    }

    @Override
    public GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner) {
        return new Core(testRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withDomainTestRunner(DomainTestRunner testRunner) {
        return new Core(generatorTestRunner, testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withRefinementTestRunner(RefinementTestRunner testRunner) {
        return new Core(generatorTestRunner, domainTestRunner, testRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReportSettings(ReportSettings reportSettings) {
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReportRenderer(ReportRenderer reportRenderer) {
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return new Core(generatorTestRunner, domainTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, timeout, executorOverride);
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
        return generatorTestApi(this::getExecutor, this::runGeneratorTest,
                generatorTestParameters(generator, nothing(), defaultSampleCount, defaultMaximumShrinkCount,
                        Vector.empty(), defaultTimeout, nothing(), getGeneratorParameters()));
    }

    private <A> void runGeneratorTest(GeneratorTest<A> generatorTest) {

        GeneratorTestResult<A> result = generatorTestRunner.run(generatorTest)
                .flatMap(res -> refineResult(generatorTest, res))
                .unsafePerformIO();
        ReportData<A> reportData = reportData(generatorTest.getProperty(), result.getResult(), generatorTest.getArbitrary().getPrettyPrinter(),
                just(result.getInitialSeedValue()));
        reporter.report(reportSettings, reportRenderer, reportData);
    }

    private <A> void runDomainTest(DomainTest<A> domainTest) {
        DomainTestResult<A> result = domainTestRunner.run(domainTest)
                .unsafePerformIO();
        ReportData<A> reportData = reportData(domainTest.getProperty(),
                result.getResult(),
                domainTest.getDomain().getPrettyPrinter(),
                nothing());
        reporter.report(reportSettings, reportRenderer, reportData);
    }

    private <A> DomainTestApi<A> createDomainTestApi(Quantifier quantifier, Domain<A> domain) {
        return domainTestApi(this::getExecutor, this::runDomainTest,
                domainTestParameters(domain, quantifier, Vector.empty(), defaultTimeout, nothing()));
    }

    private <A> IO<GeneratorTestResult<A>> refineResult(GeneratorTest<A> testData,
                                                        GeneratorTestResult<A> initialResult) {
        if (!(initialResult.getResult() instanceof TestResult.Falsified<?>)) {
            return io(initialResult);
        }
        TestResult.Falsified<A> falsified = (TestResult.Falsified<A>) initialResult.getResult();
        ShrinkStrategy<A> shrinkStrategy = testData.getArbitrary().getShrinkStrategy().orElse(null);
        if (shrinkStrategy == null) {
            return io(initialResult);
        }
        return refinementTestRunner
                .run(refinementTestExecutionParameters(getExecutor()),
                        refinementTest(shrinkStrategy, testData.getProperty(), falsified.getCounterexample().getSample(),
                                testData.getMaximumShrinkCount(), testData.getTimeout()))
                .fmap(maybeRefinedResult -> maybeRefinedResult
                        .match(__ -> initialResult,
                                refined -> initialResult.withResult(falsified.withRefinedCounterexample(refined))));
    }

    private Executor getExecutor() {
        return executorOverride.orElseGet(DefaultGauntletExecutor::defaultGauntletExecutor);
    }

}
