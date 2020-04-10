package dev.marksman.gauntlet;

import java.time.Duration;

import static dev.marksman.gauntlet.DefaultDomainTestRunner.defaultDomainTestRunner;
import static dev.marksman.gauntlet.DefaultGeneratorTestRunner.defaultGeneratorTestRunner;
import static dev.marksman.gauntlet.DefaultReporter.defaultReporter;
import static dev.marksman.kraftwerk.StandardGeneratorParameters.defaultGeneratorParameters;
import static dev.marksman.kraftwerk.bias.DefaultPropertyTestingBiasSettings.defaultPropertyTestBiasSettings;
import static java.util.concurrent.Executors.newFixedThreadPool;

public final class Gauntlet {

    public static final int DEFAULT_SAMPLE_COUNT = 100;
    public static final int DEFAULT_MAX_DISCARDS = 100;
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);  // TODO: default timeout

    private Gauntlet() {

    }

    private static GauntletApi INSTANCE;

    public static GauntletApi gauntlet() {
        if (INSTANCE == null) {
            synchronized (Gauntlet.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DefaultGauntlet(newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                            defaultGeneratorTestRunner(),
                            defaultDomainTestRunner(),
                            defaultReporter(),
                            defaultGeneratorParameters()
                                    .withBiasSettings(defaultPropertyTestBiasSettings()),
                            DEFAULT_SAMPLE_COUNT,
                            DEFAULT_TIMEOUT);
                }
            }
        }
        return INSTANCE;
    }

}
