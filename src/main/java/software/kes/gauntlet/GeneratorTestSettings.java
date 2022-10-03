package software.kes.gauntlet;

import software.kes.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

final class GeneratorTestSettings {
    private final int sampleCount;
    private final int maximumShrinkCount;
    private final Duration timeout;
    private final Executor executor;
    private final GeneratorParameters generatorParameters;

    private GeneratorTestSettings(int sampleCount,
                                  int maximumShrinkCount,
                                  Duration timeout,
                                  Executor executor,
                                  GeneratorParameters generatorParameters) {
        this.sampleCount = sampleCount;
        this.maximumShrinkCount = maximumShrinkCount;
        this.timeout = timeout;
        this.executor = executor;
        this.generatorParameters = generatorParameters;
    }

    public static GeneratorTestSettings generatorTestSettings(int sampleCount, int maximumShrinkCount, Duration timeout,
                                                              Executor executor, GeneratorParameters generatorParameters) {
        return new GeneratorTestSettings(sampleCount, maximumShrinkCount, timeout, executor, generatorParameters);
    }

    public int getMaximumShrinkCount() {
        return maximumShrinkCount;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public Executor getExecutor() {
        return executor;
    }

    public GeneratorParameters getGeneratorParameters() {
        return generatorParameters;
    }

    public int getSampleCount() {
        return sampleCount;
    }
}
