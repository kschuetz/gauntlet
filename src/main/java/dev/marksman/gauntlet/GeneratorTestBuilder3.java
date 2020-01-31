package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.util.Set;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

final class GeneratorTestBuilder3<A, B, C> implements GeneratorTestBuilder<Tuple3<A, B, C>> {
    private final GenerationStrategy<A> genA;
    private final GenerationStrategy<B> genB;
    private final GenerationStrategy<C> genC;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;
    private final ImmutableFiniteIterable<Fn1<Tuple3<A, B, C>, Set<String>>> classifiers;

    GeneratorTestBuilder3(GenerationStrategy<A> genA,
                          GenerationStrategy<B> genB,
                          GenerationStrategy<C> genC,
                          Maybe<Long> initialSeed,
                          int sampleCount,
                          ImmutableFiniteIterable<Fn1<Tuple3<A, B, C>, Set<String>>> classifiers) {
        this.genA = genA;
        this.genB = genB;
        this.genC = genC;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
        this.classifiers = classifiers;
    }

    @Override
    public GeneratorTestBuilder<Tuple3<A, B, C>> withSampleCount(int sampleCount) {
        return new GeneratorTestBuilder3<>(genA, genB, genC, initialSeed, sampleCount, classifiers);
    }

    @Override
    public GeneratorTestBuilder<Tuple3<A, B, C>> withInitialSeed(long initialSeed) {
        return new GeneratorTestBuilder3<>(genA, genB, genC, just(initialSeed), sampleCount, classifiers);
    }

    @Override
    public GeneratorTestBuilder<Tuple3<A, B, C>> classifyUsing(Fn1<Tuple3<A, B, C>, Set<String>> classifier) {
        return new GeneratorTestBuilder3<>(genA, genB, genC, initialSeed, sampleCount, classifiers.prepend(classifier));

    }

    @Override
    public Report<Tuple3<A, B, C>> executeFor(Prop<Tuple3<A, B, C>> prop) {
        return null;
    }

    static <A, B, C> GeneratorTestBuilder<Tuple3<A, B, C>> generatorTestBuilder3(GenerationStrategy<A> genA,
                                                                                 GenerationStrategy<B> genB,
                                                                                 GenerationStrategy<C> genC,
                                                                                 int sampleCount) {
        return new GeneratorTestBuilder3<>(genA, genB, genC, nothing(),
                sampleCount, emptyImmutableFiniteIterable());
    }

}
