package software.kes.gauntlet;

import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.collectionviews.Vector;
import software.kes.kraftwerk.GeneratorParameters;
import software.kes.kraftwerk.Seed;

final class EnumeratedTestParameters<A> implements TestParametersSource<A> {
    private final ImmutableNonEmptyVector<A> values;

    private EnumeratedTestParameters(ImmutableNonEmptyVector<A> values) {
        this.values = values;
    }

    public static <A> EnumeratedTestParameters<A> enumeratedTestParameters(Iterable<A> values) {
        ImmutableNonEmptyVector<A> vector = Vector.copyFrom(values).toNonEmpty().orElseThrow(() -> new IllegalArgumentException("values must be non-empty"));
        return new EnumeratedTestParameters<>(vector);
    }

    @Override
    public TestParameterCollection<A> getTestParameterCollection(GeneratorParameters generatorParameters, Seed inputSeed) {
        return new TestParameterCollection<A>() {
            @Override
            public ImmutableNonEmptyVector<A> getValues() {
                return values;
            }

            @Override
            public Seed getOutputSeed() {
                return inputSeed;
            }
        };
    }
}
