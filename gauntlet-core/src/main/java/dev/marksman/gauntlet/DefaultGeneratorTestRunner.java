package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.kraftwerk.Seed;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.EvaluateSampleTask.testSampleTask;
import static dev.marksman.gauntlet.GeneratedDataSet.generatedDataSet;
import static dev.marksman.gauntlet.GeneratorTestResult.generatorTestResult;

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
        Executor executor = executionParameters.getExecutor();
        Maybe<AscribedFailure<A>> result = nothing();
        Context context = new Context();
        long initialSeedValue = testData.getInitialSeed().orElseGet(seedGenerator::nextLong);
        Seed initialSeed = Seed.create(initialSeedValue);
        Arbitrary<A> arbitrary = testData.getArbitrary();
        ValueSupplier<A> valueSupplier = arbitrary.prepare(executionParameters.getGeneratorParameters());
        GeneratedDataSet<A> dataSet = generateDataSet(initialSeed, valueSupplier, testData.getSampleCount());

        ImmutableVector<A> samples = dataSet.getValues();
        int actualSampleCount = samples.size();
        ResultCollector<A> collector = new ResultCollector<>(dataSet.getValues());
        for (int sampleIndex = 0; sampleIndex < actualSampleCount; sampleIndex++) {
            EvaluateSampleTask<A> task = testSampleTask(context, collector, testData.getProperty(), sampleIndex, samples.unsafeGet(sampleIndex));
            executor.execute(task);
        }
        // TODO: handle supply failure
        // TODO: handle shrinks
        return generatorTestResult(collector.getResultBlocking(testData.getTimeout().orElse(executionParameters.getDefaultTimeout())),
                initialSeedValue);
    }

    private <A> GeneratedDataSet<A> generateDataSet(Seed initialSeed,
                                                    ValueSupplier<A> valueSupplier,
                                                    int sampleCount) {
        Maybe<SupplyFailure> supplyFailure = nothing();
        ArrayList<A> values = new ArrayList<>(sampleCount);
        Seed state = initialSeed;
        for (int i = 0; i < sampleCount; i++) {
            GeneratorOutput<A> next = valueSupplier.getNext(state);
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

    public static DefaultGeneratorTestRunner defaultGeneratorTestRunner() {
        return INSTANCE;
    }
}
