package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

final class ConcreteArbitrary<A> implements Arbitrary<A> {
    private final Fn1<GeneratorParameters, Supply<A>> generator;
    private final ImmutableFiniteIterable<Fn1<GeneratorParameters, GeneratorParameters>> parameterTransforms;
    private final Filter<A> filter;
    private final Maybe<ShrinkStrategy<A>> shrinkStrategy;
    private final Fn1<? super A, String> prettyPrinter;
    private final int maxDiscards;

    private ConcreteArbitrary(Fn1<GeneratorParameters, Supply<A>> generator,
                              ImmutableFiniteIterable<Fn1<GeneratorParameters, GeneratorParameters>> parameterTransforms,
                              Filter<A> filter, Maybe<ShrinkStrategy<A>> shrinkStrategy,
                              Fn1<? super A, String> prettyPrinter,
                              int maxDiscards) {
        this.generator = generator;
        this.parameterTransforms = parameterTransforms;
        this.filter = filter;
        this.shrinkStrategy = shrinkStrategy;
        this.prettyPrinter = prettyPrinter;
        this.maxDiscards = maxDiscards;
    }

    static <A> ConcreteArbitrary<A> concreteArbitrary(Fn1<GeneratorParameters, Supply<A>> generator,
                                                      Maybe<ShrinkStrategy<A>> shrinkStrategy,
                                                      Fn1<? super A, String> prettyPrinter) {
        return new ConcreteArbitrary<>(generator, emptyImmutableFiniteIterable(), Filter.emptyFilter(), shrinkStrategy, prettyPrinter, Gauntlet.DEFAULT_MAX_DISCARDS);
    }

    static <A> Arbitrary<A> concreteArbitrary(Generator<A> generator) {
        Fn0<String> labelSupplier = () -> generator.getLabel().orElseGet(generator::toString);
        return concreteArbitrary(p -> new UnfilteredSupply<>(generator.prepare(p), labelSupplier),
                nothing(), Object::toString);
    }

    @Override
    public Supply<A> createSupply(GeneratorParameters parameters) {
        GeneratorParameters transformedParameters = parameterTransforms.foldLeft((acc, f) -> f.apply(acc), parameters);
        Supply<A> vs = generator.apply(transformedParameters);
        if (filter.isEmpty()) {
            return vs;
        } else {
            return new FilteredSupply<>(vs, filter, maxDiscards);
        }
    }

    @Override
    public Maybe<ShrinkStrategy<A>> getShrinkStrategy() {
        return shrinkStrategy;
    }

    @Override
    public Fn1<? super A, String> getPrettyPrinter() {
        return prettyPrinter;
    }

    @Override
    public Arbitrary<A> withShrinkStrategy(ShrinkStrategy<A> shrinkStrategy) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter, just(shrinkStrategy), prettyPrinter, maxDiscards);
    }

    @Override
    public Arbitrary<A> withNoShrinkStrategy() {
        return shrinkStrategy.match(__ -> this,
                __ -> new ConcreteArbitrary<>(generator, parameterTransforms, filter, nothing(), prettyPrinter, maxDiscards));
    }

    @Override
    public Arbitrary<A> suchThat(Fn1<? super A, Boolean> predicate) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter.add(predicate), shrinkStrategy.fmap(s -> s.filter(predicate)),
                prettyPrinter, maxDiscards);

    }

    @Override
    public Arbitrary<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new ConcreteArbitrary<>(generator, parameterTransforms, filter, shrinkStrategy, prettyPrinter, maxDiscards);
        } else {
            return this;
        }
    }

    @Override
    public Arbitrary<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter, shrinkStrategy, prettyPrinter, maxDiscards);
    }

    @Override
    public <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new ConcreteArbitrary<>(generator.fmap(vs -> vs.fmap(ab)),
                parameterTransforms,
                filter.contraMap(ba),
                shrinkStrategy.fmap(s -> s.convert(ab, ba)),
                prettyPrinter.contraMap(ba),
                maxDiscards);

    }

    @Override
    public Arbitrary<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> modifyFn) {
        return new ConcreteArbitrary<>(generator, parameterTransforms.append(modifyFn), filter, shrinkStrategy, prettyPrinter, maxDiscards);
    }

}
