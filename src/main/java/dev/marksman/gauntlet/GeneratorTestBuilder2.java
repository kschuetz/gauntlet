package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.util.Set;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

final class GeneratorTestBuilder2<A, B> implements GeneratorTestBuilder<Tuple2<A, B>> {
    private final GenerationStrategy<A> genA;
    private final GenerationStrategy<B> genB;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;
    private final ImmutableFiniteIterable<Fn1<Tuple2<A, B>, Set<String>>> classifiers;

    GeneratorTestBuilder2(GenerationStrategy<A> genA,
                          GenerationStrategy<B> genB,
                          Maybe<Long> initialSeed,
                          int sampleCount,
                          ImmutableFiniteIterable<Fn1<Tuple2<A, B>, Set<String>>> classifiers) {
        this.genA = genA;
        this.genB = genB;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
        this.classifiers = classifiers;
    }

    @Override
    public GeneratorTestBuilder<Tuple2<A, B>> withSampleCount(int sampleCount) {
        return new GeneratorTestBuilder2<>(genA, genB, initialSeed, sampleCount, classifiers);
    }

    @Override
    public GeneratorTestBuilder<Tuple2<A, B>> withInitialSeed(long initialSeed) {
        return new GeneratorTestBuilder2<>(genA, genB, just(initialSeed), sampleCount, classifiers);
    }

    @Override
    public GeneratorTestBuilder<Tuple2<A, B>> classifyUsing(Fn1<Tuple2<A, B>, Set<String>> classifier) {
        return new GeneratorTestBuilder2<>(genA, genB, initialSeed, sampleCount, classifiers.prepend(classifier));

    }

    @Override
    public Report<Tuple2<A, B>> executeFor(Prop<Tuple2<A, B>> prop) {
        return null;
    }

    static <A, B> GeneratorTestBuilder<Tuple2<A, B>> generatorTestBuilder2(GenerationStrategy<A> genA,
                                                                           GenerationStrategy<B> genB,
                                                                           int sampleCount) {
        return new GeneratorTestBuilder2<>(genA, genB, nothing(),
                sampleCount, emptyImmutableFiniteIterable());
    }

}
