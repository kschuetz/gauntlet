package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;

public final class GeneratorTest<A> {
    @Getter
    private final Arbitrary<A> arbitrary;
    @Getter
    private final Maybe<Long> initialSeed;
    @Getter
    private final int sampleCount;
    @Getter
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    @Getter
    private final Maybe<Duration> timeout;
    @Getter
    private final Prop<A> property;

    GeneratorTest(Arbitrary<A> arbitrary, Maybe<Long> initialSeed, int sampleCount, ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers, Maybe<Duration> timeout, Prop<A> property) {
        this.arbitrary = arbitrary;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
        this.classifiers = classifiers;
        this.property = property;
        this.timeout = timeout;
    }

    public static <A> GeneratorTest<A> generatorTest(GeneratorTestParameters<A> parameters, Prop<A> property) {
        return new GeneratorTest<>(parameters.getArbitrary(),
                parameters.getInitialSeed(), parameters.getSampleCount(), parameters.getClassifiers(),
                parameters.getTimeout(), property);
    }


}
