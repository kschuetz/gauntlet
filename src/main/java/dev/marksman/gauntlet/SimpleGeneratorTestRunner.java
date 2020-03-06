package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Seed;

import java.util.Random;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static dev.marksman.gauntlet.GeneratorParameters.defaultGeneratorParameters;

public final class SimpleGeneratorTestRunner implements GeneratorTestRunner {

    private static final SimpleGeneratorTestRunner INSTANCE = new SimpleGeneratorTestRunner();

    private SimpleGeneratorTestRunner() {

    }

    @Override
    public <A> Report<A> run(GeneratorTest<A> testData) {
        Context context = new Context();
        int sampleCount = testData.getSampleCount();
        ValueSupplier<A> valueSupplier = testData.getArbitrary().prepare(defaultGeneratorParameters());
        long initialSeed = testData.getInitialSeed().orElseGet(() -> new Random().nextLong());
        Seed state = Seed.create(initialSeed);
        GeneratorTestRun<A> reportBuilder = GeneratorTestRun.mutableReportBuilder();
        reportBuilder.setInitialSeed(initialSeed);
        reportBuilder.setClassifiers(testData.getClassifiers());
        for (int i = 0; i < sampleCount; i++) {
            GeneratorOutput<A> next = valueSupplier.getNext(state);
            next.getValue()
                    .match(sf -> {
                                reportBuilder.supplyFailure(sf);
                                return UNIT;
                            },
                            value -> {
                                runCase(context, reportBuilder, testData, value);
                                return UNIT;
                            });
            state = next.getNextState();

            if (!reportBuilder.shouldContinue()) {
                break;
            }
        }

        return reportBuilder.buildReport();
    }

    private <A> void runCase(Context context, GeneratorTestRun<A> reportBuilder, GeneratorTest<A> testData, A value) {
        EvalResult evalResult;
        try {
            evalResult = testData.getProperty().test(context, value);
        } catch (Throwable e) {
            reportBuilder.failWithError(value, e);
            return;
        }

        if (evalResult.isSuccess()) {
            reportBuilder.markSuccess(value);
            return;
        }

        // attempt to shrink here
    }

    public static SimpleGeneratorTestRunner simpleGeneratorTestRunner() {
        return INSTANCE;
    }
}
