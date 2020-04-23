package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Generators;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkArrayList;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkHashSet;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkNonEmptyVector;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkVector;
import static dev.marksman.kraftwerk.Generators.generateSize;
import static dev.marksman.kraftwerk.aggregator.Aggregators.collectionAggregator;
import static dev.marksman.kraftwerk.aggregator.Aggregators.vectorAggregator;

final class CollectionArbitraries {

    static <A> Arbitrary<ImmutableVector<A>> vector(Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(parameters),
                                vectorAggregator()),
                just(shrinkVector(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }

    static <A> Arbitrary<ImmutableVector<A>> vectorOfN(int count, Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                Generators.constant(count).prepare(parameters),
                                vectorAggregator()),
                just(shrinkVector(count, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }

    static <A> Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVector(Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(1, parameters),
                                vectorAggregator())
                                .fmap(ImmutableVector::toNonEmptyOrThrow),
                just(shrinkNonEmptyVector(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }

    static <A> Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count, Arbitrary<A> elements) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be >= 1");
        }
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                Generators.constant(count).prepare(parameters),
                                vectorAggregator())
                                .fmap(ImmutableVector::toNonEmptyOrThrow),
                just(shrinkNonEmptyVector(count, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }


    static <A> Arbitrary<ArrayList<A>> arrayList(Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(ArrayList::new)),
                just(shrinkArrayList(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }


    static <A> Arbitrary<ArrayList<A>> arrayListOfN(int count, Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                Generators.constant(count).prepare(parameters),
                                collectionAggregator(ArrayList::new)),
                just(shrinkArrayList(count, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }


    static <A> Arbitrary<ArrayList<A>> nonEmptyArrayList(Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(ArrayList::new)),
                just(shrinkArrayList(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }


    static <A> Arbitrary<HashSet<A>> hashSet(Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(HashSet::new)),
                just(shrinkHashSet(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
    }


    static <A> Arbitrary<HashSet<A>> nonEmptyHashSet(Arbitrary<A> elements) {
        return concreteArbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(HashSet::new)),
                just(shrinkHashSet(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                Object::toString);
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
