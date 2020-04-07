package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.GeneratorTestApi1.generatorTestBuilder1;
import static dev.marksman.gauntlet.GeneratorTestApi2.generatorTestApi2;
import static dev.marksman.gauntlet.GeneratorTestExecutionParameters.generatorTestExecutionParameters;
import static dev.marksman.gauntlet.GeneratorTestParameters.generatorTestParameters;
import static dev.marksman.gauntlet.ReportData.reportData;

class DefaultGauntlet implements GauntletApi {
    private final Executor executor;
    private final GeneratorTestRunner generatorTestRunner;
    private final Reporter reporter;
    private final GeneratorParameters generatorParameters;
    private final int defaultSampleCount;
    private final int defaultMaxDiscards;
    private final Duration defaultTimeout;

    public DefaultGauntlet(Executor executor, GeneratorTestRunner generatorTestRunner, Reporter reporter, GeneratorParameters generatorParameters, int defaultSampleCount, int defaultMaxDiscards, Duration defaultTimeout) {
        this.executor = executor;
        this.generatorTestRunner = generatorTestRunner;
        this.reporter = reporter;
        this.generatorParameters = generatorParameters;
        this.defaultSampleCount = defaultSampleCount;
        this.defaultMaxDiscards = defaultMaxDiscards;
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
    public int getDefaultMaxDiscards() {
        return defaultMaxDiscards;
    }

    @Override
    public GauntletApi withDefaultSampleCount(int sampleCount) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, sampleCount, defaultMaxDiscards, defaultTimeout);
    }

    @Override
    public GauntletApi withDefaultMaxDiscards(int maxDiscards) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, maxDiscards, defaultTimeout);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaxDiscards, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner) {
        return new DefaultGauntlet(executor, testRunner, reporter, generatorParameters, defaultSampleCount, defaultMaxDiscards, defaultTimeout);
    }

    @Override
    public GauntletApi withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaxDiscards, defaultTimeout);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaxDiscards, defaultTimeout);
    }

    @Override
    public GauntletApi withDefaultTimeout(Duration timeout) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, generatorParameters, defaultSampleCount, defaultMaxDiscards, timeout);
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

    private <A> GeneratorTestApi<A> createGeneratorTestApi1(Arbitrary<A> generator) {
        GeneratorTestExecutionParameters gtep = generatorTestExecutionParameters(getExecutor(),
                getGeneratorParameters(), defaultTimeout);

        return generatorTestBuilder1(testData -> getGeneratorTestRunner().run(gtep, testData),
                reporter::report,
                generatorTestParameters(generator, nothing(), defaultSampleCount, Vector.empty(), nothing()));
    }

    private <A> GeneratorTestApi<A> createGeneratorTestApi(Arbitrary<A> generator) {
        return generatorTestApi2(this::runGeneratorTest,
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

}
