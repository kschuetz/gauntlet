package dev.marksman.gauntlet;

import java.util.concurrent.Executor;

public interface GauntletEnvironment {
    Executor getExecutor();

    GeneratorTestRunner getGeneratorTestRunner();

    Reporter getReporter();

    GauntletEnvironment withExecutor(Executor executor);

    GauntletEnvironment withGeneratorTestRunner(GeneratorTestRunner testRunner);

    GauntletEnvironment withReporter(Reporter reporter);
}
