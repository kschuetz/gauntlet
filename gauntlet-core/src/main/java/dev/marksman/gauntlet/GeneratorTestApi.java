package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.GeneratorTest.generatorTest;
import static dev.marksman.gauntlet.GeneratorTestSettingsAdjustments.generatorTestSettingsAdjustments;
import static dev.marksman.gauntlet.SettingAdjustment.absolute;

public final class GeneratorTestApi<A> {
    private final Arbitrary<A> arbitrary;
    private final GeneratorTestSettingsAdjustments config;

    private GeneratorTestApi(Arbitrary<A> arbitrary, GeneratorTestSettingsAdjustments config) {
        this.arbitrary = arbitrary;
        this.config = config;
    }

    static <A> GeneratorTestApi<A> generatorTestApi(Arbitrary<A> arbitrary) {
        return new GeneratorTestApi<>(arbitrary, generatorTestSettingsAdjustments());
    }

    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new GeneratorTestApi<>(arbitrary, config.adjustSampleCount(absolute(sampleCount)));
    }

    public GeneratorTestApi<A> withMaximumShrinkCount(int maximumShrinkCount) {
        return new GeneratorTestApi<>(arbitrary, config.adjustMaximumShrinkCount(absolute(maximumShrinkCount)));
    }

    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new GeneratorTestApi<>(arbitrary, config.adjustTimeout(absolute(timeout)));
    }

    public GeneratorTestApi<A> withExecutor(Executor executor) {
        return new GeneratorTestApi<>(arbitrary, config.adjustExecutor(absolute(executor)));
    }

    public GeneratorTestApi<A> withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new GeneratorTestApi<>(arbitrary, config.adjustGeneratorParameters(absolute(generatorParameters)));
    }

    public GeneratorTest<A> mustSatisfy(Prop<A> property) {
        return generatorTest(arbitrary, property, config);
    }

}
