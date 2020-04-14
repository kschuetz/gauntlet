package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import java.time.Duration;
import java.util.Set;

@Wither
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeneratorTestParameters<A> {
    private final Arbitrary<A> arbitrary;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;
    private final int maximumShrinkCount;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Duration timeout;

    public GeneratorTestParameters<A> addClassifier(Fn1<A, Set<String>> classifier) {
        return new GeneratorTestParameters<>(arbitrary, initialSeed, sampleCount, maximumShrinkCount,
                classifiers.prepend(classifier), timeout);
    }

    public static <A> GeneratorTestParameters<A> generatorTestParameters(Arbitrary<A> arbitrary,
                                                                         Maybe<Long> initialSeed,
                                                                         int sampleCount,
                                                                         int maximumShrinkCount,
                                                                         ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                                                         Duration timeout) {
        return new GeneratorTestParameters<>(arbitrary, initialSeed, maximumShrinkCount, sampleCount, classifiers, timeout);
    }
}
