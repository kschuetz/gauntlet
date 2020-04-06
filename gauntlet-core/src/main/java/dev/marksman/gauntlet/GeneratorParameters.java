package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Parameters;

import static dev.marksman.kraftwerk.StandardParameters.defaultParameters;
import static dev.marksman.kraftwerk.bias.DefaultPropertyTestingBiasSettings.defaultPropertyTestBiasSettings;

class GeneratorParameters {

    private static final Parameters DEFAULT = defaultParameters()
            .withBiasSettings(defaultPropertyTestBiasSettings());

    static Parameters defaultGeneratorParameters() {
        return DEFAULT;
    }
}
