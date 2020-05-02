package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;

final class GeneratorTest<A> {
    private final Arbitrary<A> arbitrary;
    private final Prop<A> property;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;
    private final int maximumShrinkCount;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Duration timeout;
    private final Executor executor;
    private final GeneratorParameters generatorParameters;

    private GeneratorTest(Arbitrary<A> arbitrary, Prop<A> property, Maybe<Long> initialSeed, int sampleCount,
                          int maximumShrinkCount, ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                          Duration timeout, Executor executor, GeneratorParameters generatorParameters) {
        this.arbitrary = arbitrary;
        this.property = property;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
        this.maximumShrinkCount = maximumShrinkCount;
        this.classifiers = classifiers;
        this.timeout = timeout;
        this.executor = executor;
        this.generatorParameters = generatorParameters;
    }

    static <A> GeneratorTest<A> generatorTest(Arbitrary<A> arbitrary,
                                              Prop<A> property,
                                              Maybe<Long> initialSeed,
                                              int sampleCount,
                                              int maximumShrinkCount,
                                              ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                              Duration timeout,
                                              Executor executor,
                                              GeneratorParameters generatorParameters) {
        return new GeneratorTest<>(arbitrary, property, initialSeed, sampleCount, maximumShrinkCount, classifiers,
                timeout, executor, generatorParameters);
    }

    public Arbitrary<A> getArbitrary() {
        return this.arbitrary;
    }

    public Prop<A> getProperty() {
        return this.property;
    }

    public Maybe<Long> getInitialSeed() {
        return this.initialSeed;
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    public int getMaximumShrinkCount() {
        return this.maximumShrinkCount;
    }

    public ImmutableFiniteIterable<Fn1<A, Set<String>>> getClassifiers() {
        return this.classifiers;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public GeneratorParameters getGeneratorParameters() {
        return this.generatorParameters;
    }
}
