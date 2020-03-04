package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.Getter;

import java.util.Set;

public final class GeneratorTest<A> {
    @Getter
    private final Arbitrary<A> arbitrary;
    @Getter
    private final Prop<A> property;
    @Getter
    private final Maybe<Long> initialSeed;
    @Getter
    private final int sampleCount;
    @Getter
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;

    GeneratorTest(Arbitrary<A> arbitrary, Prop<A> property, Maybe<Long> initialSeed, int sampleCount, ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers) {
        this.arbitrary = arbitrary;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
        this.classifiers = classifiers;
        this.property = property;
    }
}
