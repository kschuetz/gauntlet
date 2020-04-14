package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class GeneratorTest<A> {
    @Getter
    private final Arbitrary<A> arbitrary;
    @Getter
    private final Maybe<Long> initialSeed;
    @Getter
    private final int sampleCount;
    @Getter
    private final int maximumShrinkCount;
    @Getter
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    @Getter
    private final Duration timeout;
    @Getter
    private final Prop<A> property;


    public static <A> GeneratorTest<A> generatorTest(GeneratorTestParameters<A> parameters, Prop<A> property) {
        return new GeneratorTest<>(parameters.getArbitrary(), parameters.getInitialSeed(), parameters.getSampleCount(),
                parameters.getMaximumShrinkCount(), parameters.getClassifiers(), parameters.getTimeout(), property);
    }

}
