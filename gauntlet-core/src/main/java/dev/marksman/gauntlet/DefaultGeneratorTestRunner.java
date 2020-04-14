package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Seed;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.io.IO.io;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static dev.marksman.gauntlet.GeneratedDataSet.generatedDataSet;
import static dev.marksman.gauntlet.GeneratorTestResult.generatorTestResult;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;

public final class DefaultGeneratorTestRunner implements GeneratorTestRunner {
    private static final Random seedGenerator = new Random();
    private static final DefaultGeneratorTestRunner INSTANCE = new DefaultGeneratorTestRunner();

    // generate all inputs
    // if generator fails early:
    //    - test all anyway.  if falsified, fail as normal.
    //    - if cannot falsify, fail with SupplyFailure
    // if all inputs can be generated, submit test tasks to executor along with sample index
    // if failure is found, run tests on shrinks

    @Override
    public <A> GeneratorTestResult<A> run(GeneratorTestExecutionParameters executionParameters, GeneratorTest<A> testData) {
        return generateDataSet(executionParameters.getGeneratorParameters(), testData)
                .flatMap(dataSet -> runTest(executionParameters, testData, dataSet))
                .flatMap(initialResult -> refineResult(executionParameters, testData, initialResult))
                .unsafePerformIO();
    }

    private IO<Long> getInitialSeedValue(Maybe<Long> suppliedSeedValue) {
        return suppliedSeedValue.match(__ -> io(seedGenerator::nextLong), IO::io);
    }

    private <A> IO<GeneratorTestResult<A>> runTest(GeneratorTestExecutionParameters executionParameters,
                                                   GeneratorTest<A> testData,
                                                   GeneratedDataSet<A> dataSet) {
        return io(() -> {
            Executor executor = executionParameters.getExecutor();
            ImmutableVector<A> samples = dataSet.getSamples();
            int actualSampleCount = samples.size();
            ResultCollector<A> collector = universalResultCollector(dataSet.getSamples());
            for (int sampleIndex = 0; sampleIndex < actualSampleCount; sampleIndex++) {
                EvaluateSampleTask<A> task = evaluateSampleTask(collector, testData.getProperty(), sampleIndex, samples.unsafeGet(sampleIndex));
                executor.execute(task);
            }
            TestResult<A> initialResult = collector.getResultBlocking(testData.getTimeout());
            return generatorTestResult(maybeApplySupplyFailure(dataSet.getSupplyFailure(), initialResult),
                    dataSet.getInitialSeedValue());
        });
    }


    private <A> TestResult<A> maybeApplySupplyFailure(Maybe<SupplyFailure> supplyFailureMaybe, TestResult<A> input) {
        // supply failure only matters if test has passed
        return supplyFailureMaybe
                .match(__ -> input,
                        supplyFailure -> input.projectA()
                                .match(__ -> input,
                                        passed -> TestResult.supplyFailed(passed.getPassedSamples(), supplyFailure)));
    }

    private <A> IO<GeneratorTestResult<A>> refineResult(GeneratorTestExecutionParameters executionParameters,
                                                        GeneratorTest<A> testData,
                                                        GeneratorTestResult<A> initialResult) {
        return initialResult.getResult().projectC()
                .match(__ -> io(initialResult),
                        falsified -> runShrinks(executionParameters, testData, initialResult, falsified));
    }

    private <A> IO<GeneratorTestResult<A>> runShrinks(GeneratorTestExecutionParameters executionParameters,
                                                      GeneratorTest<A> testData,
                                                      GeneratorTestResult<A> initialResult,
                                                      TestResult.Falsified<A> falsified) {
        // TODO: handle shrinks
        return io(initialResult);
    }

    private <A> IO<GeneratedDataSet<A>> generateDataSet(GeneratorParameters generatorParameters,
                                                        GeneratorTest<A> testData) {
        return getInitialSeedValue(testData.getInitialSeed())
                .flatMap(isv -> io(() ->
                        buildDataSetFromSupply(isv, testData.getArbitrary().createSupply(generatorParameters),
                                testData.getSampleCount())));
    }

    private <A> GeneratedDataSet<A> buildDataSetFromSupply(long initialSeedValue,
                                                           Supply<A> supply,
                                                           int sampleCount) {
        Maybe<SupplyFailure> supplyFailure = nothing();
        ArrayList<A> values = new ArrayList<>(sampleCount);
        Seed state = Seed.create(initialSeedValue);
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
        return generatedDataSet(initialSeedValue, values, supplyFailure, state);
    }

    public static DefaultGeneratorTestRunner defaultGeneratorTestRunner() {
        return INSTANCE;
    }

//    private

}
