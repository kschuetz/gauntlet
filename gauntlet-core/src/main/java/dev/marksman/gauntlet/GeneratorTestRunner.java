package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.collectionviews.ImmutableVector;
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

final class GeneratorTestRunner {
    private static final Random seedGenerator = new Random();
    private static final GeneratorTestRunner INSTANCE = new GeneratorTestRunner();

    static GeneratorTestRunner generatorTestRunner() {
        return INSTANCE;
    }

    public <A> IO<GeneratorTestResult<A>> run(GeneratorTestSettings settings,
                                              Arbitrary<A> arbitrary,
                                              Prop<A> property) {
        return generateDataSet(settings, arbitrary)
                .flatMap(dataSet -> runTest(settings, property, dataSet));
    }

    private IO<Long> getInitialSeedValue(Maybe<Long> suppliedSeedValue) {
        return suppliedSeedValue.match(__ -> io(seedGenerator::nextLong), IO::io);
    }

    // generate all inputs
    // if generator fails early:
    //    - test all anyway.  if falsified, fail as normal.
    //    - if cannot falsify, fail with SupplyFailure
    // if all inputs can be generated, submit test tasks to executor along with sample index

    private <A> IO<GeneratorTestResult<A>> runTest(GeneratorTestSettings settings,
                                                   Prop<A> property,
                                                   GeneratedDataSet<A> dataSet) {
        return io(() -> {
            Executor executor = settings.getExecutor();
            ImmutableVector<A> samples = dataSet.getSamples();
            int actualSampleCount = samples.size();
            ResultCollector<A> collector = universalResultCollector(dataSet.getSamples());
            for (int sampleIndex = 0; sampleIndex < actualSampleCount; sampleIndex++) {
                EvaluateSampleTask<A> task = evaluateSampleTask(collector, property, sampleIndex, samples.unsafeGet(sampleIndex));
                executor.execute(task);
            }
            TestResult<A> initialResult = collector.getResultBlocking(settings.getTimeout());
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

    private <A> IO<GeneratedDataSet<A>> generateDataSet(GeneratorTestSettings settings,
                                                        Arbitrary<A> arbitrary) {
        return getInitialSeedValue(nothing())
                .flatMap(isv -> io(() ->
                        buildDataSetFromSupply(isv, arbitrary.supplyStrategy(settings.getGeneratorParameters()),
                                settings.getSampleCount())));
    }

    private <A> GeneratedDataSet<A> buildDataSetFromSupply(long initialSeedValue,
                                                           SupplyStrategy<A> supplyStrategy,
                                                           int sampleCount) {
        Maybe<SupplyFailure> supplyFailure = nothing();
        ArrayList<A> values = new ArrayList<>(sampleCount);
        Seed state = Seed.create(initialSeedValue);
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
        return generatedDataSet(initialSeedValue, values, supplyFailure, state);
    }
}
