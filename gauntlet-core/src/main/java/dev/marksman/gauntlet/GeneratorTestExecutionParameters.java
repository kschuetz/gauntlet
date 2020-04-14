package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Executor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GeneratorTestExecutionParameters {
    private final Executor executor;
    private final GeneratorParameters generatorParameters;

    public static GeneratorTestExecutionParameters generatorTestExecutionParameters(Executor executor,
                                                                                    GeneratorParameters generatorParameters) {
        return new GeneratorTestExecutionParameters(executor, generatorParameters);
    }
}
