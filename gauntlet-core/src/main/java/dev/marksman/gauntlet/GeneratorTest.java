package dev.marksman.gauntlet;

public final class GeneratorTest<A> {
    private final Arbitrary<A> arbitrary;
    private final Prop<A> property;
    private final GeneratorTestSettingsAdjustments settings;

    private GeneratorTest(Arbitrary<A> arbitrary, Prop<A> property, GeneratorTestSettingsAdjustments settingsAdjustments) {
        this.arbitrary = arbitrary;
        this.property = property;
        this.settings = settingsAdjustments;
    }

    static <A> GeneratorTest<A> generatorTest(Arbitrary<A> arbitrary, Prop<A> property, GeneratorTestSettingsAdjustments settingsAdjustments) {
        return new GeneratorTest<>(arbitrary, property, settingsAdjustments);
    }

    public Arbitrary<A> getArbitrary() {
        return this.arbitrary;
    }

    public Prop<A> getProperty() {
        return this.property;
    }

    public GeneratorTestSettingsAdjustments getSettingsAdjustments() {
        return settings;
    }
}
