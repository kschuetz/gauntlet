package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;

import static dev.marksman.kraftwerk.bias.DefaultPropertyTestingBiasSettings.defaultPropertyTestBiasSettings;

class GParams {

    private static final GeneratorParameters DEFAULT = defaultGeneratorParameters()
            .withBiasSettings(defaultPropertyTestBiasSettings());

    static GeneratorParameters defaultGeneratorParameters() {
        return DEFAULT;
    }
}
