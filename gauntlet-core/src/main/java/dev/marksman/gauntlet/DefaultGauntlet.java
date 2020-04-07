package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;

import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.GeneratorTestBuilder1.generatorTestBuilder1;

class DefaultGauntlet implements GauntletApi {
    private static final int INITIAL_DEFAULT_SAMPLE_COUNT = 100;
    private static final int INITIAL_DEFAULT_MAX_DISCARDS = 100;

    private final Executor executor;
    private final GeneratorTestRunner generatorTestRunner;
    private final Reporter reporter;
    private final int defaultSampleCount;
    private final int defaultMaxDiscards;

    public DefaultGauntlet(Executor executor, GeneratorTestRunner generatorTestRunner, Reporter reporter, int defaultSampleCount, int defaultMaxDiscards) {
        this.executor = executor;
        this.generatorTestRunner = generatorTestRunner;
        this.reporter = reporter;
        this.defaultSampleCount = defaultSampleCount;
        this.defaultMaxDiscards = defaultMaxDiscards;
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
    public Reporter getReporter() {
        return reporter;
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
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, sampleCount, defaultMaxDiscards);
    }

    @Override
    public GauntletApi withDefaultMaxDiscards(int maxDiscards) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, defaultSampleCount, maxDiscards);
    }

    @Override
    public GauntletApi withExecutor(Executor executor) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, defaultSampleCount, defaultMaxDiscards);
    }

    @Override
    public GauntletApi withGeneratorTestRunner(GeneratorTestRunner testRunner) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, defaultSampleCount, defaultMaxDiscards);
    }

    @Override
    public GauntletApi withReporter(Reporter reporter) {
        return new DefaultGauntlet(executor, generatorTestRunner, reporter, defaultSampleCount, defaultMaxDiscards);
    }

    @Override
    public <A> GeneratorTestBuilder<A> all(Arbitrary<A> generator) {
        return generatorTestBuilder1(this, generator, defaultSampleCount);
    }

    @Override
    public <A, B> GeneratorTestBuilder<Tuple2<A, B>> all(Arbitrary<A> generatorA, Arbitrary<B> generatorB) {
        return generatorTestBuilder1(this, CompositeArbitraries.combine(generatorA, generatorB),
                defaultSampleCount);
    }

    @Override
    public <A, B, C> GeneratorTestBuilder<Tuple3<A, B, C>> all(Arbitrary<A> generatorA, Arbitrary<B> generatorB, Arbitrary<C> generatorC) {
        return generatorTestBuilder1(this, CompositeArbitraries.combine(generatorA, generatorB, generatorC),
                defaultSampleCount);
    }

}
