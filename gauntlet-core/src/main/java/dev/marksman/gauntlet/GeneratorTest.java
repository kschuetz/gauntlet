package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.kraftwerk.GeneratorParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class GeneratorTest<A> {
    @Getter
    private final Arbitrary<A> arbitrary;
    @Getter
    private final Prop<A> property;
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
    private final Executor executor;
    @Getter
    private final GeneratorParameters generatorParameters;

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

}
