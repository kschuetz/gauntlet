package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Parameters;
import dev.marksman.kraftwerk.bias.DefaultPropertyTestingBiasSettings;

import static dev.marksman.kraftwerk.StandardParameters.defaultParameters;

class GeneratorParameters {

    private static final Parameters DEFAULT = defaultParameters()
            .withBiasSettings(DefaultPropertyTestingBiasSettings.defaultPropertyTestBiasSettings());

    static Parameters defaultGeneratorParameters() {
        return DEFAULT;
    }
}
