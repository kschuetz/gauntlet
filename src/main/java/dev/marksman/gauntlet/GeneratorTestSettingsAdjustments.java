package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.SettingAdjustment.inherit;

final class GeneratorTestSettingsAdjustments {
    private static final GeneratorTestSettingsAdjustments BASE = new GeneratorTestSettingsAdjustments(inherit(), inherit(), inherit(), inherit(), inherit());

    private final SettingAdjustment<Integer> sampleCount;
    private final SettingAdjustment<Integer> maximumShrinkCount;
    private final SettingAdjustment<Duration> timeout;
    private final SettingAdjustment<Executor> executor;
    private final SettingAdjustment<GeneratorParameters> generatorParameters;

    private GeneratorTestSettingsAdjustments(SettingAdjustment<Integer> sampleCount,
                                             SettingAdjustment<Integer> maximumShrinkCount,
                                             SettingAdjustment<Duration> timeout,
                                             SettingAdjustment<Executor> executor,
                                             SettingAdjustment<GeneratorParameters> generatorParameters) {
        this.sampleCount = sampleCount;
        this.maximumShrinkCount = maximumShrinkCount;
        this.timeout = timeout;
        this.executor = executor;
        this.generatorParameters = generatorParameters;
    }

    public static GeneratorTestSettingsAdjustments generatorTestSettingsAdjustments() {
        return BASE;
    }

    public SettingAdjustment<Integer> getSampleCount() {
        return sampleCount;
    }

    public SettingAdjustment<Integer> getMaximumShrinkCount() {
        return maximumShrinkCount;
    }

    public SettingAdjustment<Duration> getTimeout() {
        return timeout;
    }

    public SettingAdjustment<Executor> getExecutor() {
        return executor;
    }

    public SettingAdjustment<GeneratorParameters> getGeneratorParameters() {
        return generatorParameters;
    }

    public GeneratorTestSettingsAdjustments adjustSampleCount(SettingAdjustment<Integer> sampleCount) {
        return new GeneratorTestSettingsAdjustments(this.sampleCount.add(sampleCount), maximumShrinkCount, timeout, executor, generatorParameters);
    }

    public GeneratorTestSettingsAdjustments adjustMaximumShrinkCount(SettingAdjustment<Integer> maximumShrinkCount) {
        return new GeneratorTestSettingsAdjustments(sampleCount, this.maximumShrinkCount.add(maximumShrinkCount), timeout, executor, generatorParameters);
    }

    public GeneratorTestSettingsAdjustments adjustTimeout(SettingAdjustment<Duration> timeout) {
        return new GeneratorTestSettingsAdjustments(sampleCount, maximumShrinkCount, this.timeout.add(timeout), executor, generatorParameters);
    }

    public GeneratorTestSettingsAdjustments adjustExecutor(SettingAdjustment<Executor> executor) {
        return new GeneratorTestSettingsAdjustments(sampleCount, maximumShrinkCount, timeout, this.executor.add(executor), generatorParameters);
    }

    public GeneratorTestSettingsAdjustments adjustGeneratorParameters(SettingAdjustment<GeneratorParameters> generatorParameters) {
        return new GeneratorTestSettingsAdjustments(sampleCount, maximumShrinkCount, timeout, executor, this.generatorParameters.add(generatorParameters));
    }

}
