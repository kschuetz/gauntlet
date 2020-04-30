package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.kraftwerk.GeneratorParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;

@With
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class GeneratorTestParameters<A> {
    private final Arbitrary<A> arbitrary;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;
    private final int maximumShrinkCount;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Duration timeout;
    private final Maybe<Executor> executorOverride;
    private final GeneratorParameters generatorParameters;

    public static <A> GeneratorTestParameters<A> generatorTestParameters(Arbitrary<A> arbitrary,
                                                                         Maybe<Long> initialSeed,
                                                                         int sampleCount,
                                                                         int maximumShrinkCount,
                                                                         ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                                                         Duration timeout,
                                                                         Maybe<Executor> executorOverride,
                                                                         GeneratorParameters generatorParameters) {
        return new GeneratorTestParameters<>(arbitrary, initialSeed, maximumShrinkCount, sampleCount, classifiers,
                timeout, executorOverride, generatorParameters);
    }

    public GeneratorTestParameters<A> addClassifier(Fn1<A, Set<String>> classifier) {
        return new GeneratorTestParameters<>(arbitrary, initialSeed, sampleCount, maximumShrinkCount,
                classifiers.prepend(classifier), timeout, executorOverride, generatorParameters);
    }
}
