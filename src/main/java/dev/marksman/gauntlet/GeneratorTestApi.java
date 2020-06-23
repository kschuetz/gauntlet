package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.GeneratorTest.generatorTest;
import static dev.marksman.gauntlet.GeneratorTestSettingsAdjustments.generatorTestSettingsAdjustments;
import static dev.marksman.gauntlet.SettingAdjustment.absolute;
import static dev.marksman.gauntlet.SettingAdjustment.inherit;
import static dev.marksman.gauntlet.SettingAdjustment.modify;

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

    public GeneratorTest<A> satisfy(Prop<A> property) {
        return generatorTest(arbitrary, property, settings);
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

    public GeneratorTestApi<A> modifySampleCount(Fn1<Integer, Integer> f) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustSampleCount(modify(f)));
    }

    public GeneratorTestApi<A> modifyMaximumShrinkCount(Fn1<Integer, Integer> f) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustMaximumShrinkCount(modify(f)));
    }

    public GeneratorTestApi<A> modifyTimeout(Fn1<Duration, Duration> f) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustTimeout(modify(f)));
    }

    public GeneratorTestApi<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> f) {
        return new GeneratorTestApi<>(arbitrary, settings.adjustGeneratorParameters(modify(f)));
    }

    public GeneratorTestApi<A> withDefaultSampleCount() {
        return new GeneratorTestApi<>(arbitrary, settings.adjustSampleCount(inherit()));
    }

    public GeneratorTestApi<A> withDefaultMaximumShrinkCount() {
        return new GeneratorTestApi<>(arbitrary, settings.adjustMaximumShrinkCount(inherit()));
    }

    public GeneratorTestApi<A> withDefaultTimeout() {
        return new GeneratorTestApi<>(arbitrary, settings.adjustTimeout(inherit()));
    }

    public GeneratorTestApi<A> withDefaultGeneratorParameters() {
        return new GeneratorTestApi<>(arbitrary, settings.adjustGeneratorParameters(inherit()));
    }
}
