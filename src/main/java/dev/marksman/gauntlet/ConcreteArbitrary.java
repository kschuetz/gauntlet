package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.builtins.ShrinkCollections;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.Parameters;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

final class ConcreteArbitrary<A> implements Arbitrary<A> {
    private final Fn1<Parameters, ValueSupplier<A>> generator;
    private final ImmutableFiniteIterable<Fn1<Parameters, Parameters>> parameterTransforms;
    private final Filter<A> filter;
    private final Maybe<Shrink<A>> shrink;
    private final Fn1<A, String> prettyPrinter;
    private final int maxDiscards;

    private ConcreteArbitrary(Fn1<Parameters, ValueSupplier<A>> generator,
                              ImmutableFiniteIterable<Fn1<Parameters, Parameters>> parameterTransforms,
                              Filter<A> filter, Maybe<Shrink<A>> shrink,
                              Fn1<A, String> prettyPrinter,
                              int maxDiscards) {
        this.generator = generator;
        this.parameterTransforms = parameterTransforms;
        this.filter = filter;
        this.shrink = shrink;
        this.prettyPrinter = prettyPrinter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public ValueSupplier<A> prepare(Parameters parameters) {
        Parameters transformedParameters = parameterTransforms.foldLeft((acc, f) -> f.apply(acc), parameters);
        ValueSupplier<A> vs = generator.apply(transformedParameters);
        if (filter.isEmpty()) {
            return vs;
        } else {
            return new FilteredValueSupplier<>(vs, filter, maxDiscards);
        }
    }

    @Override
    public Maybe<Shrink<A>> getShrink() {
        return shrink;
    }

    @Override
    public Fn1<A, String> getPrettyPrinter() {
        return prettyPrinter;
    }

    @Override
    public Arbitrary<A> withShrink(Shrink<A> shrink) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter, just(shrink), prettyPrinter, maxDiscards);
    }

    @Override
    public Arbitrary<A> withNoShrink() {
        return shrink.match(__ -> this,
                __ -> new ConcreteArbitrary<>(generator, parameterTransforms, filter, nothing(), prettyPrinter, maxDiscards));
    }

    @Override
    public Arbitrary<A> suchThat(Fn1<A, Boolean> predicate) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter.add(predicate), shrink, prettyPrinter, maxDiscards);

    }

    @Override
    public Arbitrary<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new ConcreteArbitrary<>(generator, parameterTransforms, filter, shrink, prettyPrinter, maxDiscards);
        } else {
            return this;
        }
    }

    @Override
    public Arbitrary<A> withPrettyPrinter(Fn1<A, String> prettyPrinter) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter, shrink, prettyPrinter, maxDiscards);
    }

    @Override
    public <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new ConcreteArbitrary<>(generator.fmap(vs -> vs.fmap(ab)),
                parameterTransforms,
                filter.contraMap(ba),
                shrink.fmap(s -> s.convert(ab, ba)),
                prettyPrinter.contraMap(ba),
                maxDiscards);

    }

    public Arbitrary<ImmutableVector<A>> vector() {
        return concreteArbitrary(parameters ->
                        new CollectionValueSupplier<>(generator.apply(parameters),
                                sizeGenerator(parameters),
                                Vector::<A>builder,
                                VectorBuilder::add,
                                VectorBuilder::build),
                shrink.fmap(ShrinkCollections::shrinkVector),
                // TODO: prettyPrinter
                Object::toString);
    }

    public Arbitrary<ImmutableVector<A>> vectorOfN(int count) {
        return concreteArbitrary(parameters ->
                        new CollectionValueSupplier<>(generator.apply(parameters),
                                Generators.constant(count).prepare(parameters),
                                Vector::<A>builder,
                                VectorBuilder::add,
                                VectorBuilder::build),
                shrink.fmap(ShrinkCollections::shrinkVector),
                // TODO: prettyPrinter
                Object::toString);
    }

    @Override
    public Arbitrary<A> modifyGeneratorParameters(Fn1<Parameters, Parameters> modifyFn) {
        return new ConcreteArbitrary<>(generator, parameterTransforms.append(modifyFn), filter, shrink, prettyPrinter, maxDiscards);
    }

    static <A> ConcreteArbitrary<A> concreteArbitrary(Fn1<Parameters, ValueSupplier<A>> generator,
                                                      Maybe<Shrink<A>> shrink,
                                                      Fn1<A, String> prettyPrinter) {
        return new ConcreteArbitrary<>(generator, emptyImmutableFiniteIterable(), Filter.emptyFilter(), shrink, prettyPrinter, Gauntlet.DEFAULT_MAX_DISCARDS);
    }

    static <A> Arbitrary<A> concreteArbitrary(Generator<A> generator) {
        Fn0<String> labelSupplier = () -> generator.getLabel().orElseGet(generator::toString);
        return concreteArbitrary(p -> new UnfilteredValueSupplier<>(generator.prepare(p), labelSupplier),
                nothing(), Object::toString);
    }

    private static Generate<Integer> sizeGenerator(Parameters parameters) {
        // TODO:  replace with generateSize when available in kraftwerk
        return Generators.sized(Generators::constant).prepare(parameters);
    }
}
