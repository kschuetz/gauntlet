package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Seed;
import lombok.AllArgsConstructor;

import java.util.Random;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.GeneratorParameters.defaultGeneratorParameters;

@AllArgsConstructor
public class GeneratorPropTest<A> implements PropTest<A> {
    private final Generator<A> generator;
    private final Prop<A> property;
    private final Maybe<Shrink<A>> shrinkStrategy;
    private final int sampleCount;

    @Override
    public TestResult<A> run() {
        return runWithSeed(new Random().nextLong());
    }

    @Override
    public TestResult<A> runWithSeed(long seedValue) {
        ImmutableFiniteIterable<A> values = generator.run(defaultGeneratorParameters(),
                Seed.create(seedValue))
                .take(sampleCount);
        return TestRunner.runTest(new TestData<>(property, values, shrinkStrategy, just(seedValue)));
    }
}
