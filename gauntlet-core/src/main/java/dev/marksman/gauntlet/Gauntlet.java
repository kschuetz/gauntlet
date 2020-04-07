package dev.marksman.gauntlet;

import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.GeneratorParameters.defaultGeneratorParameters;
import static java.util.concurrent.Executors.newFixedThreadPool;

public final class Gauntlet {

    private static Executor executor = newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static int DEFAULT_SAMPLE_COUNT = 100;
    public static int DEFAULT_MAX_DISCARDS = 100;

    private Gauntlet() {

    }

    public static GauntletApi GAUNTLET = new DefaultGauntlet(executor,
            DefaultGeneratorTestRunner.defaultGeneratorTestRunner(defaultGeneratorParameters()),
            DefaultReporter.defaultReporter(),
            DEFAULT_SAMPLE_COUNT,
            DEFAULT_MAX_DISCARDS);


}
