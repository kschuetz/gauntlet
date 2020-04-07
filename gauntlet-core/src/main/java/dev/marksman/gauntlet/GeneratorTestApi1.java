package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.GeneratorTestParameters.generatorTestParameters;
import static dev.marksman.gauntlet.ReportData.reportData;

final class GeneratorTestApi1<A> implements GeneratorTestApi<A> {
    private final Fn1<GeneratorTest<A>, GeneratorTestResult<A>> testRunner;
    private final Consumer<ReportData<A>> reportSink;
    private final GeneratorTestParameters<A> parameters;

    GeneratorTestApi1(Fn1<GeneratorTest<A>, GeneratorTestResult<A>> testRunner,
                      Consumer<ReportData<A>> reportSink,
                      GeneratorTestParameters<A> parameters) {
        this.testRunner = testRunner;
        this.reportSink = reportSink;
        this.parameters = parameters;
    }

    @Override
    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new GeneratorTestApi1<>(testRunner, reportSink, parameters.withSampleCount(sampleCount));
    }

    @Override
    public GeneratorTestApi<A> withInitialSeed(long initialSeed) {
        return new GeneratorTestApi1<>(testRunner, reportSink, parameters.withInitialSeed(just(initialSeed)));
    }

    @Override
    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new GeneratorTestApi1<>(testRunner, reportSink, parameters.withTimeout(just(timeout)));
    }

    @Override
    public GeneratorTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new GeneratorTestApi1<>(testRunner, reportSink, parameters.addClassifier(classifier));
    }

    @Override
    public void mustSatisfy(Prop<A> prop) {
        GeneratorTest<A> testData = buildGeneratorTest(prop);
        GeneratorTestResult<A> testResult = this.testRunner.apply(testData);
        ReportData<A> reportData = buildReportData(testData, testResult);
        reportSink.accept(reportData);
    }

    private GeneratorTest<A> buildGeneratorTest(Prop<A> prop) {
        return new GeneratorTest<>(parameters.getArbitrary(),
                parameters.getInitialSeed(), parameters.getSampleCount(), parameters.getClassifiers(), parameters.getTimeout(), prop
        );
    }

    private ReportData<A> buildReportData(GeneratorTest<A> testData,
                                          GeneratorTestResult<A> testResult) {
        return reportData(testData.getProperty(), testResult.getResult(), testData.getArbitrary().getPrettyPrinter(),
                just(testResult.getInitialSeedValue()));
    }

    static <A> GeneratorTestApi<A> generatorTestBuilder1(Fn1<GeneratorTest<A>, GeneratorTestResult<A>> testRunner,
                                                         Consumer<ReportData<A>> reportSink,
                                                         Arbitrary<A> generator,
                                                         int sampleCount) {
        return new GeneratorTestApi1<>(testRunner, reportSink, generatorTestParameters(generator, nothing(), sampleCount,
                Vector.empty(), nothing()));
    }

    static <A> GeneratorTestApi<A> generatorTestBuilder1(Fn1<GeneratorTest<A>, GeneratorTestResult<A>> testRunner,
                                                         Consumer<ReportData<A>> reportSink,
                                                         GeneratorTestParameters<A> parameters) {
        return new GeneratorTestApi1<>(testRunner, reportSink, parameters);
    }

}
