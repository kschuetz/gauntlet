package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Seed;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.GeneratedDataSet.generatedDataSet;
import static dev.marksman.gauntlet.GeneratorTestSettings.generatorTestSettings;
import static dev.marksman.gauntlet.Quantifier.UNIVERSAL;
import static dev.marksman.gauntlet.ReportData.reportData;
import static dev.marksman.gauntlet.TestRunnerSettings.testRunnerSettings;

final class Core implements GauntletApi {
    private static final Random seedGenerator = new Random();
    private static final int REFINEMENT_BLOCK_SIZE = RefinementTest.DEFAULT_BLOCK_SIZE;

    private final Maybe<Executor> executorOverride;
    private final TestRunner testRunner;
    private final RefinementTestRunner refinementTestRunner;
    private final Reporter reporter;
    private final ReportSettings reportSettings;
    private final ReportRenderer reportRenderer;
    private final GeneratorParameters generatorParameters;
    private final int defaultSampleCount;
    private final int defaultMaximumShrinkCount;
    private final Duration defaultTimeout;

    public Core(TestRunner testRunner,
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
        this.testRunner = testRunner;
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
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, sampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withDefaultMaximumShrinkCount(int maximumShrinkCount) {
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, maximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, just(executor));
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReportSettings(ReportSettings reportSettings) {
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withReportRenderer(ReportRenderer reportRenderer) {
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, defaultTimeout, executorOverride);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return new Core(testRunner, refinementTestRunner, reporter, reportSettings, reportRenderer, generatorParameters, defaultSampleCount, defaultMaximumShrinkCount, timeout, executorOverride);
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
    public <A> void assertThat(DomainTest<A> domainTest) {
        TestRunnerSettings settings = createDomainSettings(domainTest.getSettingsAdjustments());
        TestResult<A> result = testRunner.run(settings, domainTest.getQuantifier(), domainTest.getDomain().getElements(),
                domainTest.getProperty())
                .unsafePerformIO();
        ReportData<A> reportData = reportData(domainTest.getProperty(),
                result,
                domainTest.getDomain().getPrettyPrinter(),
                nothing());
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
        GeneratedDataSet<A> dataSet = generateDataSet(settings, generatorTest.getArbitrary(), inputSeed);
        TestRunnerSettings testRunnerSettings = TestRunnerSettings.testRunnerSettings(settings.getTimeout(), settings.getExecutor());
        TestResult<A> result = testRunner.run(testRunnerSettings, UNIVERSAL, dataSet.getSamples(), generatorTest.getProperty())
                .unsafePerformIO();
        // TODO: refactor this
        result = maybeApplySupplyFailure(dataSet.getSupplyFailure(), result);

        // TODO: refine result
        ReportData<A> reportData = reportData(generatorTest.getProperty(), result, generatorTest.getArbitrary().getPrettyPrinter(),
                just(initialSeedValue));
        reporter.report(reportSettings, reportRenderer, reportData);
    }

    private <A> TestResult<A> maybeApplySupplyFailure(Maybe<SupplyFailure> supplyFailureMaybe, TestResult<A> input) {
        // supply failure only matters if test has passed
        return supplyFailureMaybe
                .match(__ -> input,
                        supplyFailure -> input.projectA()
                                .match(__ -> input,
                                        passed -> TestResult.supplyFailed(supplyFailure, passed.getSuccessCount())));
    }

    private <A> GeneratedDataSet<A> generateDataSet(GeneratorTestSettings settings,
                                                    Arbitrary<A> arbitrary,
                                                    Seed inputSeed) {
        return buildDataSetFromSupply(arbitrary.supplyStrategy(settings.getGeneratorParameters()),
                settings.getSampleCount(),
                inputSeed);
    }

    private <A> GeneratedDataSet<A> buildDataSetFromSupply(SupplyStrategy<A> supplyStrategy,
                                                           int sampleCount,
                                                           Seed inputSeed) {
        Maybe<SupplyFailure> supplyFailure = nothing();
        ArrayList<A> values = new ArrayList<>(sampleCount);
        Seed state = inputSeed;
        StatefulSupply<A> supply = supplyStrategy.createSupply();
        for (int i = 0; i < sampleCount; i++) {
            GeneratorOutput<A> next = supply.getNext(state);
            supplyFailure = next.getValue()
                    .match(Maybe::just,
                            value -> {
                                values.add(value);
                                return nothing();
                            });
            state = next.getNextState();
            if (!supplyFailure.equals(nothing())) {
                break;
            }
        }
        return generatedDataSet(values, supplyFailure, state);
    }

}
