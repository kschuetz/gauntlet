package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.util.Set;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

final class GeneratorTestBuilder1<A> implements GeneratorTestBuilder<A> {
    private final Maybe<GeneratorTestRunner> runner;
    private final Arbitrary<A> gen;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;

    GeneratorTestBuilder1(Maybe<GeneratorTestRunner> runner,
                          Arbitrary<A> gen,
                          Maybe<Long> initialSeed,
                          int sampleCount,
                          ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers) {
        this.runner = runner;
        this.gen = gen;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
        this.classifiers = classifiers;
    }

    @Override
    public GeneratorTestBuilder<A> withSampleCount(int sampleCount) {
        return new GeneratorTestBuilder1<>(runner, gen, initialSeed, sampleCount, classifiers);
    }

    @Override
    public GeneratorTestBuilder<A> withInitialSeed(long initialSeed) {
        return new GeneratorTestBuilder1<>(runner, gen, just(initialSeed), sampleCount, classifiers);
    }

    @Override
    public GeneratorTestBuilder<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new GeneratorTestBuilder1<>(runner, gen, initialSeed, sampleCount, classifiers.prepend(classifier));
    }

    public GeneratorTestBuilder<A> withRunner(GeneratorTestRunner runner) {
        return new GeneratorTestBuilder1<>(just(runner), gen, initialSeed, sampleCount, classifiers);
    }

    @Override
    public Outcome<A> executeFor(Prop<A> prop) {
        GeneratorTest<A> testData = new GeneratorTest<>(gen, prop, initialSeed, sampleCount, classifiers);
        return runner.orElseGet(Gauntlet::defaultGeneratorTestRunner).run(testData);
    }

    static <A> GeneratorTestBuilder<A> generatorTestBuilder1(Arbitrary<A> generator,
                                                             int sampleCount) {
        return new GeneratorTestBuilder1<>(nothing(), generator, nothing(),
                sampleCount, emptyImmutableFiniteIterable());
    }

}
