package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

final class TestParametersSourceUnit implements TestParametersSource<Unit> {
    private static final TestParametersSourceUnit INSTANCE = new TestParametersSourceUnit();

    private TestParametersSourceUnit() {
    }

    public static TestParametersSourceUnit testParametersSourceUnit() {
        return INSTANCE;
    }

    @Override
    public TestParameterCollection<Unit> getTestParameterCollection(GeneratorParameters generatorParameters, Seed inputSeed) {
        return new TestParameterCollection<Unit>() {
            @Override
            public ImmutableNonEmptyVector<Unit> getValues() {
                return Vector.of(UNIT);
            }

            @Override
            public Seed getOutputSeed() {
                return inputSeed;
            }
        };
    }
}
