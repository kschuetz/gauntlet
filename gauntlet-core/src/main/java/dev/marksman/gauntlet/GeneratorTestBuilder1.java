package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.time.Duration;
import java.util.Set;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.gauntlet.ReportData.reportData;

final class GeneratorTestBuilder1<A> implements GeneratorTestBuilder<A> {
    private final Maybe<GeneratorTestRunner> runner;
    private final Arbitrary<A> gen;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Maybe<Duration> timeout;

    GeneratorTestBuilder1(Maybe<GeneratorTestRunner> runner,
                          Arbitrary<A> gen,
                          Maybe<Long> initialSeed,
                          int sampleCount,
                          ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers, Maybe<Duration> timeout) {
        this.runner = runner;
        this.gen = gen;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
        this.classifiers = classifiers;
        this.timeout = timeout;
    }

    @Override
    public GeneratorTestBuilder<A> withSampleCount(int sampleCount) {
        return new GeneratorTestBuilder1<>(runner, gen, initialSeed, sampleCount, classifiers, timeout);
    }

    @Override
    public GeneratorTestBuilder<A> withInitialSeed(long initialSeed) {
        return new GeneratorTestBuilder1<>(runner, gen, just(initialSeed), sampleCount, classifiers, timeout);
    }

    @Override
    public GeneratorTestBuilder<A> withTimeout(Duration timeout) {
        return new GeneratorTestBuilder1<>(runner, gen, initialSeed, sampleCount, classifiers, just(timeout));
    }

    @Override
    public GeneratorTestBuilder<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new GeneratorTestBuilder1<>(runner, gen, initialSeed, sampleCount, classifiers.prepend(classifier), timeout);
    }

    public GeneratorTestBuilder<A> withRunner(GeneratorTestRunner runner) {
        return new GeneratorTestBuilder1<>(just(runner), gen, initialSeed, sampleCount, classifiers, timeout);
    }

    @Override
    public Outcome<A> executeFor(Prop<A> prop) {
        return runImpl(buildGeneratorTest(prop));
    }

    @Override
    public void mustSatisfy(Prop<A> prop) {
        GeneratorTest<A> testData = buildGeneratorTest(prop);
        Outcome<A> outcome = runImpl(testData);
        ReportData<A> reportData = buildReportData(testData, outcome);
        DefaultReporter.defaultReporter().reportOutcome(reportData);
    }

    private Outcome<A> runImpl(GeneratorTest<A> testData) {
        return runner.orElseGet(Gauntlet::defaultGeneratorTestRunner).run(testData);
    }

    private GeneratorTest<A> buildGeneratorTest(Prop<A> prop) {
        return new GeneratorTest<>(gen, prop, initialSeed, sampleCount, classifiers, timeout);
    }

    private ReportData<A> buildReportData(GeneratorTest<A> testData,
                                          Outcome<A> outcome) {
        return reportData(testData.getProperty(), outcome, testData.getArbitrary().getPrettyPrinter());
    }

    static <A> GeneratorTestBuilder<A> generatorTestBuilder1(Arbitrary<A> generator,
                                                             int sampleCount) {
        return new GeneratorTestBuilder1<>(nothing(), generator, nothing(),
                sampleCount, emptyImmutableFiniteIterable(), nothing());
    }

}