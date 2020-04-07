package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.concurrent.Executor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GeneratorTestExecutionParameters {
    private final Executor executor;
    private final GeneratorParameters generatorParameters;
    private final Duration defaultTimeout;

    public static GeneratorTestExecutionParameters generatorTestExecutionParameters(Executor executor,
                                                                                    GeneratorParameters generatorParameters,
                                                                                    Duration defaultTimeout) {
        return new GeneratorTestExecutionParameters(executor, generatorParameters, defaultTimeout);
    }
}
