package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.builtins.ShrinkCollections;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Generators;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.kraftwerk.Generators.generateSize;
import static dev.marksman.kraftwerk.aggregator.Aggregators.collectionAggregator;
import static dev.marksman.kraftwerk.aggregator.Aggregators.vectorAggregator;

final class ConcreteArbitrary<A> implements Arbitrary<A> {
    private final Fn1<GeneratorParameters, Supply<A>> generator;
    private final ImmutableFiniteIterable<Fn1<GeneratorParameters, GeneratorParameters>> parameterTransforms;
    private final Filter<A> filter;
    private final Maybe<Shrink<A>> shrink;
    private final Fn1<? super A, String> prettyPrinter;
    private final int maxDiscards;

    private ConcreteArbitrary(Fn1<GeneratorParameters, Supply<A>> generator,
                              ImmutableFiniteIterable<Fn1<GeneratorParameters, GeneratorParameters>> parameterTransforms,
                              Filter<A> filter, Maybe<Shrink<A>> shrink,
                              Fn1<? super A, String> prettyPrinter,
                              int maxDiscards) {
        this.generator = generator;
        this.parameterTransforms = parameterTransforms;
        this.filter = filter;
        this.shrink = shrink;
        this.prettyPrinter = prettyPrinter;
        this.maxDiscards = maxDiscards;
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
    public Maybe<Shrink<A>> getShrink() {
        return shrink;
    }

    @Override
    public Fn1<? super A, String> getPrettyPrinter() {
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
    public Arbitrary<A> suchThat(Fn1<? super A, Boolean> predicate) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter.add(predicate), shrink.fmap(s -> s.filter(predicate)),
                prettyPrinter, maxDiscards);

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
    public Arbitrary<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter) {
        return new ConcreteArbitrary<>(generator, parameterTransforms, filter, shrink, prettyPrinter, maxDiscards);
    }

    @Override
    public Arbitrary<A> mapPreFilterOutput(Fn1<A, A> f) {
        return new ConcreteArbitrary<>(generator.fmap(vs -> vs.fmap(f)),
                parameterTransforms,
                filter,
                shrink,
                prettyPrinter,
                maxDiscards);
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
                        new CollectionSupply<>(createSupply(parameters),
                                sizeGenerator(parameters),
                                vectorAggregator()),
                shrink.fmap(ShrinkCollections::shrinkVector),
                // TODO: prettyPrinter
                Object::toString);
    }

    public Arbitrary<ImmutableVector<A>> vectorOfN(int count) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                Generators.constant(count).prepare(parameters),
                                vectorAggregator()),
                shrink.fmap(ShrinkCollections::shrinkVector),
                // TODO: prettyPrinter
                Object::toString);
    }

    public Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                sizeGenerator(1, parameters),
                                vectorAggregator())
                                .fmap(ImmutableVector::toNonEmptyOrThrow),
                shrink.fmap(ShrinkCollections::shrinkNonEmptyVector),
                // TODO: prettyPrinter
                Object::toString);
    }

    public Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be >= 1");
        }
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                Generators.constant(count).prepare(parameters),
                                vectorAggregator())
                                .fmap(ImmutableVector::toNonEmptyOrThrow),
                shrink.fmap(ShrinkCollections::shrinkNonEmptyVector),
                // TODO: prettyPrinter
                Object::toString);
    }

    @Override
    public Arbitrary<ArrayList<A>> arrayList() {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(ArrayList::new)),
                shrink.fmap(ShrinkCollections::shrinkArrayList),
                // TODO: prettyPrinter
                Object::toString);
    }

    @Override
    public Arbitrary<ArrayList<A>> arrayListOfN(int count) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                Generators.constant(count).prepare(parameters),
                                collectionAggregator(ArrayList::new)),
                shrink.fmap(ShrinkCollections::shrinkArrayList),
                // TODO: prettyPrinter
                Object::toString);
    }

    @Override
    public Arbitrary<ArrayList<A>> nonEmptyArrayList() {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(ArrayList::new)),
                shrink.fmap(ShrinkCollections::shrinkNonEmptyArrayList),
                // TODO: prettyPrinter
                Object::toString);
    }

    @Override
    public Arbitrary<HashSet<A>> hashSet() {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(HashSet::new)),
                shrink.fmap(ShrinkCollections::shrinkHashSet),
                // TODO: prettyPrinter
                Object::toString);
    }

    @Override
    public Arbitrary<HashSet<A>> nonEmptyHashSet() {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(createSupply(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(HashSet::new)),
                shrink.fmap(ShrinkCollections::shrinkNonEmptyHashSet),
                // TODO: prettyPrinter
                Object::toString);
    }

    @Override
    public Arbitrary<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> modifyFn) {
        return new ConcreteArbitrary<>(generator, parameterTransforms.append(modifyFn), filter, shrink, prettyPrinter, maxDiscards);
    }

    static <A> ConcreteArbitrary<A> concreteArbitrary(Fn1<GeneratorParameters, Supply<A>> generator,
                                                      Maybe<Shrink<A>> shrink,
                                                      Fn1<? super A, String> prettyPrinter) {
        return new ConcreteArbitrary<>(generator, emptyImmutableFiniteIterable(), Filter.emptyFilter(), shrink, prettyPrinter, Gauntlet.DEFAULT_MAX_DISCARDS);
    }

    static <A> Arbitrary<A> concreteArbitrary(Generator<A> generator) {
        Fn0<String> labelSupplier = () -> generator.getLabel().orElseGet(generator::toString);
        return concreteArbitrary(p -> new UnfilteredSupply<>(generator.prepare(p), labelSupplier),
                nothing(), Object::toString);
    }

    private static Generate<Integer> sizeGenerator(GeneratorParameters parameters) {
        return generateSize()
                .prepare(parameters);
    }

    private static Generate<Integer> sizeGenerator(int minSize, GeneratorParameters parameters) {
        return generateSize()
                .fmap(s -> Math.max(minSize, s))
                .prepare(parameters);
    }
}
