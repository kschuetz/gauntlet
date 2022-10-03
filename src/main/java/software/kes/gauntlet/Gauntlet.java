package software.kes.gauntlet;

import java.time.Clock;
import java.time.Duration;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static software.kes.gauntlet.DefaultReporter.defaultReporter;
import static software.kes.gauntlet.ExistentialTestRunner.existentialTestRunner;
import static software.kes.gauntlet.RefinementTestRunner.refinementTestRunner;
import static software.kes.gauntlet.ReportSettings.defaultReportSettings;
import static software.kes.gauntlet.UniversalTestRunner.universalTestRunner;
import static software.kes.kraftwerk.GeneratorParameters.generatorParameters;
import static software.kes.kraftwerk.bias.DefaultPropertyTestingBiasSettings.defaultPropertyTestBiasSettings;

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
                    Clock clock = Clock.systemUTC();
                    INSTANCE = new Core(universalTestRunner(clock),
                            existentialTestRunner(clock),
                            refinementTestRunner(clock),
                            defaultReporter(),
                            defaultReportSettings(),
                            DefaultReportRenderer.defaultReportRenderer(),
                            generatorParameters()
                                    .withBiasSettings(defaultPropertyTestBiasSettings()),
                            DEFAULT_SAMPLE_COUNT,
                            DEFAULT_MAXIMUM_SHRINK_COUNT,
                            DEFAULT_TIMEOUT, nothing());
                }
            }
        }
        return INSTANCE;
    }
}
