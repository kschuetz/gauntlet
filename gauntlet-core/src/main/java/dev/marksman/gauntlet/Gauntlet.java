package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;

import static dev.marksman.kraftwerk.StandardGeneratorParameters.defaultGeneratorParameters;
import static dev.marksman.kraftwerk.bias.DefaultPropertyTestingBiasSettings.defaultPropertyTestBiasSettings;
import static java.util.concurrent.Executors.newFixedThreadPool;

public final class Gauntlet {

    public static int DEFAULT_SAMPLE_COUNT = 100;
    public static int DEFAULT_MAX_DISCARDS = 100;

    private Gauntlet() {

    }

    private static GauntletApi INSTANCE;

    public static GauntletApi gauntlet() {
        if (INSTANCE == null) {
            synchronized (Gauntlet.class) {
                if (INSTANCE == null) {
                    GeneratorParameters generatorParameters = defaultGeneratorParameters()
                            .withBiasSettings(defaultPropertyTestBiasSettings());

                    INSTANCE = new DefaultGauntlet(newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                            DefaultGeneratorTestRunner.defaultGeneratorTestRunner(generatorParameters),
                            DefaultReporter.defaultReporter(),
                            generatorParameters,
                            DEFAULT_SAMPLE_COUNT,
                            DEFAULT_MAX_DISCARDS);
                }
            }
        }
        return INSTANCE;
    }

}
