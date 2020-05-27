package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.SettingAdjustment.absolute;
import static dev.marksman.gauntlet.SettingAdjustment.modify;

public final class GenerateParametersForTest<A> implements TestParametersSource<A> {
    public static int DEFAULT_SAMPLE_COUNT = 10;

    private final int sampleCount;
    private final Generator<A> generator;
    private final SettingAdjustment<GeneratorParameters> generatorParametersAdjustment;

    private GenerateParametersForTest(int sampleCount, Generator<A> generator, SettingAdjustment<GeneratorParameters> generatorParametersAdjustment) {
        this.sampleCount = sampleCount;
        this.generator = generator;
        this.generatorParametersAdjustment = generatorParametersAdjustment;
    }

    static <A> GenerateParametersForTest<A> generateParametersForTest(int sampleCount, Generator<A> generator) {
        if (sampleCount < 1) {
            throw new IllegalArgumentException("sampleCount must be > 0");
        }
        return new GenerateParametersForTest<>(sampleCount, generator, SettingAdjustment.inherit());
    }

    static <A> GenerateParametersForTest<A> generateParametersForTest(Generator<A> generator) {
        return generateParametersForTest(DEFAULT_SAMPLE_COUNT, generator);
    }

    @Override
    public TestParameterCollection<A> getTestParameterCollection(GeneratorParameters generatorParameters, Seed inputSeed) {
        GeneratorParameters effectiveGeneratorParameters = generatorParametersAdjustment.apply(() -> generatorParameters);
        Generate<A> gen = generator.prepare(effectiveGeneratorParameters);
        VectorBuilder<A> builder = VectorBuilder.builder();
        Seed current = inputSeed;
        for (int i = 0; i < sampleCount; i++) {
            Result<? extends Seed, A> result = gen.apply(current);
            builder = builder.add(result._2());
            current = result._1();
        }
        final Seed outputSeed = current;
        ImmutableNonEmptyVector<A> samples = builder.build().toNonEmptyOrThrow();
        return new TestParameterCollection<A>() {
            @Override
            public ImmutableNonEmptyVector<A> getValues() {
                return samples;
            }

            @Override
            public Seed getOutputSeed() {
                return outputSeed;
            }
        };
    }

    public GenerateParametersForTest<A> withGeneratorParameters(GeneratorParameters generatorParameters) {
        return new GenerateParametersForTest<>(sampleCount, generator, absolute(generatorParameters));
    }

    public GenerateParametersForTest<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> f) {
        return new GenerateParametersForTest<>(sampleCount, generator, generatorParametersAdjustment.add(modify(f)));
    }
}
