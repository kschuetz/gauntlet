package dev.marksman.gauntlet;

import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.GeneratorTest.generatorTest;
import static dev.marksman.gauntlet.GeneratorTestSettingsAdjustments.generatorTestSettingsAdjustments;
import static dev.marksman.gauntlet.SettingAdjustment.absolute;

public final class GeneratorTestApi<A> {
    private final Arbitrary<A> arbitrary;
    private final GeneratorTestSettingsAdjustments settings;

    private GeneratorTestApi(Arbitrary<A> arbitrary, GeneratorTestSettingsAdjustments settings) {
        this.arbitrary = arbitrary;
        this.settings = settings;
    }

    static <A> GeneratorTestApi<A> generatorTestApi(Arbitrary<A> arbitrary) {
        return new GeneratorTestApi<>(arbitrary, generatorTestSettingsAdjustments());
    }

    public GeneratorTestApi<A> withSampleCount(int sampleCount) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustSampleCount(absolute(sampleCount)));
    }

    public GeneratorTestApi<A> withMaximumShrinkCount(int maximumShrinkCount) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustMaximumShrinkCount(absolute(maximumShrinkCount)));
    }

    public GeneratorTestApi<A> withTimeout(Duration timeout) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustTimeout(absolute(timeout)));
    }

    public GeneratorTestApi<A> withExecutor(Executor executor) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustExecutor(absolute(executor)));
    }

    public GeneratorTestApi<A> withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustGeneratorParameters(absolute(generatorParameters)));
    }

    public GeneratorTest<A> satisfy(Prop<A> property) {
        return generatorTest(arbitrary, property, settings);
    }
}
