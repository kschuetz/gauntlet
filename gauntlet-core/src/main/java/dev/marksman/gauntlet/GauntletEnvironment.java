package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;

import java.util.concurrent.Executor;

public interface GauntletEnvironment {
    Executor getExecutor();

    GeneratorTestRunner getGeneratorTestRunner();

    Reporter getReporter();

    GeneratorParameters getGeneratorParameters();
}
