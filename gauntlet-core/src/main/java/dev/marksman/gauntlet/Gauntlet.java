package dev.marksman.gauntlet;

import java.time.Duration;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.DefaultReportRenderer.defaultReportRenderer;
import static dev.marksman.gauntlet.DefaultReporter.defaultReporter;
import static dev.marksman.gauntlet.GeneratorTestRunner.generatorTestRunner;
import static dev.marksman.gauntlet.RefinementTestRunner.refinementTestRunner;
import static dev.marksman.gauntlet.ReportSettings.defaultReportSettings;
import static dev.marksman.gauntlet.TestRunner.testRunner;
import static dev.marksman.kraftwerk.StandardGeneratorParameters.defaultGeneratorParameters;
import static dev.marksman.kraftwerk.bias.DefaultPropertyTestingBiasSettings.defaultPropertyTestBiasSettings;

public final class Gauntlet {
    public static final int DEFAULT_SAMPLE_COUNT = 100;
    public static final int DEFAULT_MAX_DISCARDS = 100;
    public static final int DEFAULT_MAXIMUM_SHRINK_COUNT = 1000;
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);  // TODO: default timeout

    private static GauntletApi INSTANCE;

    private Gauntlet() {

    }

    public static GauntletApi gauntlet() {
        if (INSTANCE == null) {
            synchronized (Gauntlet.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Core(generatorTestRunner(),
                            testRunner(),
                            refinementTestRunner(),
                            defaultReporter(),
                            defaultReportSettings(),
                            defaultReportRenderer(),
                            defaultGeneratorParameters()
                                    .withBiasSettings(defaultPropertyTestBiasSettings()),
                            DEFAULT_SAMPLE_COUNT,
                            DEFAULT_MAXIMUM_SHRINK_COUNT,
                            DEFAULT_TIMEOUT,
                            nothing());
                }
            }
        }
        return INSTANCE;
    }
}
