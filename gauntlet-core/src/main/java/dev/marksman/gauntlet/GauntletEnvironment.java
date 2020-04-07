package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.Parameters;

import java.util.concurrent.Executor;

public interface GauntletEnvironment {
    Executor getExecutor();

    GeneratorTestRunner getGeneratorTestRunner();

    Reporter getReporter();

    Parameters getGeneratorParameters();
}
