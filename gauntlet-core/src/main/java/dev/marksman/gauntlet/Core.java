package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Seed;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.GeneratedSampleReader.generatedSampleReader;
import static dev.marksman.gauntlet.GeneratorTestSettings.generatorTestSettings;
import static dev.marksman.gauntlet.IteratorSampleReader.iteratorSampleReader;
import static dev.marksman.gauntlet.Quantifier.EXISTENTIAL;
import static dev.marksman.gauntlet.ReportData.reportData;
import static dev.marksman.gauntlet.TestRunnerSettings.testRunnerSettings;

final class Core implements GauntletApi {
    private static final Random seedGenerator = new Random();
    private static final int REFINEMENT_BLOCK_SIZE = RefinementTest.DEFAULT_BLOCK_SIZE;

    private final UniversalTestRunner universalTestRunner;
    private final ExistentialTestRunner existentialTestRunner;
    private final RefinementTestRunner refinementTestRunner;
    private final Reporter reporter;
    private final ReportSettings reportSettings;
    private final ReportRenderer reportRenderer;
    private final GeneratorParameters generatorParameters;
    private final int defaultSampleCount;
    private final int defaultMaximumShrinkCount;
    private final Duration defaultTimeout;
    private final Maybe<Executor> executorOverride;

    public Core(UniversalTestRunner universalTestRunner,
                ExistentialTestRunner existentialTestRunner,
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
        this.universalTestRunner = universalTestRunner;
        this.existentialTestRunner = existentialTestRunner;
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
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, sampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withDefaultMaximumShrinkCount(int maximumShrinkCount) {
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, maximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, just(executor));
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReportSettings(ReportSettings reportSettings) {
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReportRenderer(ReportRenderer reportRenderer) {
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return new Core(universalTestRunner, existentialTestRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, timeout, executorOverride);
    }

    @Override
    public <A> void assertThat(GeneratorTest<A> generatorTest) {
        assertWithSeed(seedGenerator.nextLong(), generatorTest);
    }

    @Override
    public <A> void assertWithSeed(long initialSeedValue, GeneratorTest<A> generatorTest) {
        Seed inputSeed = Seed.create(initialSeedValue);
        runGeneratorTest(initialSeedValue, inputSeed, generatorTest);
    }

    @Override
    public <A, P> void assertForEach(TestParametersSource<P> parametersSource, Fn1<P, GeneratorTest<A>> createTest) {
        assertForEachWithSeed(seedGenerator.nextLong(), parametersSource, createTest);
    }

    @Override
    public <A, P> void assertForEachWithSeed(long initialSeedValue, TestParametersSource<P> parametersSource, Fn1<P, GeneratorTest<A>> createTest) {
        Seed inputSeed = Seed.create(initialSeedValue);
        TestParameterCollection<P> testParameterCollection = parametersSource.getTestParameterCollection(generatorParameters, inputSeed);
        Seed currentSeed = testParameterCollection.getOutputSeed();
        // TODO
    }

    @Override
    public <A> void assertThat(DomainTest<A> domainTest) {
        TestRunnerSettings settings = createDomainSettings(domainTest.getSettingsAdjustments());
        TestResult<A> result;
        IteratorSampleReader<A> sampleReader = iteratorSampleReader(domainTest.getDomain().getElements().iterator());
        if (domainTest.getQuantifier() == EXISTENTIAL) {
            Either<Abnormal<A>, ExistentialTestResult<A>> etr = existentialTestRunner.run(settings, domainTest.getProperty(), sampleReader);
            result = etr.match(TestResult::testResult, TestResult::testResult);
        } else {
            Either<Abnormal<A>, UniversalTestResult<A>> etr = universalTestRunner.run(settings, domainTest.getProperty(), sampleReader);
            result = etr.match(TestResult::testResult, TestResult::testResult);
        }
        ReportData<A> reportData = reportData(domainTest.getProperty(),
                result,
                domainTest.getDomain().getPrettyPrinter(),
                nothing(),
                nothing(), 1, 1);
        reporter.report(reportSettings, reportRenderer, reportData);
    }

    private GeneratorTestSettings createGeneratorSettings(GeneratorTestSettingsAdjustments adjustments) {
        return generatorTestSettings(adjustments.getSampleCount().apply(this::getDefaultSampleCount),
                adjustments.getMaximumShrinkCount().apply(this::getDefaultMaximumShrinkCount),
                adjustments.getTimeout().apply(this::getDefaultTimeout),
                adjustments.getExecutor().apply(this::getExecutor),
                adjustments.getGeneratorParameters().apply(this::getGeneratorParameters));
    }

    private TestRunnerSettings createDomainSettings(DomainTestSettingsAdjustments adjustments) {
        return testRunnerSettings(adjustments.getTimeout().apply(this::getDefaultTimeout),
                adjustments.getExecutor().apply(this::getExecutor));
    }

    private <A> IO<TestResult<A>> refineResult(GeneratorTest<A> testData,
                                               TestResult<A> initialResult) {
        return io(initialResult);
//        if (!(initialResult.getResult() instanceof TestResult.Falsified<?>)) {
//            return io(initialResult);
//        }
//        TestResult.Falsified<A> falsified = (TestResult.Falsified<A>) initialResult.getResult();
//        ShrinkStrategy<A> shrinkStrategy = testData.getArbitrary().getShrinkStrategy().orElse(null);
//        if (shrinkStrategy == null) {
//            return io(initialResult);
//        }
//        return refinementTestRunner
//                .run(refinementTest(shrinkStrategy, testData.getProperty(), falsified.getCounterexample().getSample(),
//                        testData.getMaximumShrinkCount(), testData.getTimeout(), getExecutor(), REFINEMENT_BLOCK_SIZE))
//                .fmap(maybeRefinedResult -> maybeRefinedResult
//                        .match(__ -> initialResult,
//                                refined -> initialResult.withResult(falsified.withRefinedCounterexample(refined))));
    }

    private Executor getExecutor() {
        return executorOverride.orElseGet(DefaultGauntletExecutor::defaultGauntletExecutor);
    }


    // generate all inputs
    // if generator fails early:
    //    - test all anyway.  if falsified, fail as normal.
    //    - if cannot falsify, fail with SupplyFailure
    // if all inputs can be generated, submit test tasks to executor along with sample index
    private <A> void runGeneratorTest(long initialSeedValue,
                                      Seed inputSeed,
                                      GeneratorTest<A> generatorTest) {
        GeneratorTestSettings settings = createGeneratorSettings(generatorTest.getSettingsAdjustments());
        GeneratedSampleReader<A> sampleReader = generatedSampleReader(settings.getSampleCount(), generatorTest.getArbitrary().supplyStrategy(settings.getGeneratorParameters()), inputSeed);
        TestRunnerSettings testRunnerSettings = TestRunnerSettings.testRunnerSettings(settings.getTimeout(), settings.getExecutor());
        Either<Abnormal<A>, UniversalTestResult<A>> utr = universalTestRunner.run(testRunnerSettings, generatorTest.getProperty(), sampleReader);
        TestResult<A> result = utr.match(TestResult::testResult, TestResult::testResult);
        // TODO: refine result
        ReportData<A> reportData = reportData(generatorTest.getProperty(), result, generatorTest.getArbitrary().getPrettyPrinter(),
                just(initialSeedValue), nothing(), 1, 1);
        reporter.report(reportSettings, reportRenderer, reportData);
    }
}
