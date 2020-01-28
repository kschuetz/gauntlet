package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

final class GeneratorTestBuilderImpl<A> implements GeneratorTestBuilder<A> {
    private final Generator<A> generator;
    private final ImmutableFiniteIterable<Fn1<A, Boolean>> filters;
    private final Maybe<Shrink<A>> shrinkStrategy;
    private final Maybe<Long> initialSeed;
    private final int sampleCount;

    GeneratorTestBuilderImpl(Generator<A> generator,
                             ImmutableFiniteIterable<Fn1<A, Boolean>> filters,
                             Maybe<Shrink<A>> shrinkStrategy,
                             Maybe<Long> initialSeed,
                             int sampleCount) {
        this.generator = generator;
        this.filters = filters;
        this.shrinkStrategy = shrinkStrategy;
        this.initialSeed = initialSeed;
        this.sampleCount = sampleCount;
    }

    @Override
    public GeneratorTestBuilder<A> withShrink(Shrink<A> shrink) {
        return new GeneratorTestBuilderImpl<>(generator, filters, just(shrink), initialSeed, sampleCount);
    }

    @Override
    public GeneratorTestBuilder<A> withNoShrink() {
        return new GeneratorTestBuilderImpl<>(generator, filters, nothing(), initialSeed, sampleCount);
    }

    @Override
    public GeneratorTestBuilder<A> withSampleCount(int sampleCount) {
        return new GeneratorTestBuilderImpl<>(generator, filters, shrinkStrategy, initialSeed, sampleCount);
    }

    @Override
    public GeneratorTestBuilder<A> withInitialSeed(long initialSeed) {
        return new GeneratorTestBuilderImpl<>(generator, filters, shrinkStrategy, just(initialSeed), sampleCount);
    }

    @Override
    public GeneratorTestBuilder<A> suchThat(Fn1<A, Boolean> predicate) {
        return new GeneratorTestBuilderImpl<>(generator, filters.prepend(predicate), shrinkStrategy, initialSeed, sampleCount);
    }

    @Override
    public void mustSatisfy(Prop<A> prop) {

    }

    static <A> GeneratorTestBuilder<A> generatorTestBuilder(Generator<A> generator,
                                                            int sampleCount) {
        return new GeneratorTestBuilderImpl<>(generator, emptyImmutableFiniteIterable(),
                nothing(), nothing(), sampleCount);
    }

}
