package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.ToMap;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.aggregator.Aggregator;
import dev.marksman.kraftwerk.constraints.IntRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.ArbitraryGenerator.generateArbitrary;
import static dev.marksman.gauntlet.PrettyPrinter.defaultPrettyPrinter;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkArrayList;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkHashSet;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkNonEmptyVector;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkVector;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateSize;
import static dev.marksman.kraftwerk.aggregator.Aggregator.aggregator;
import static dev.marksman.kraftwerk.aggregator.Aggregators.collectionAggregator;

final class CollectionArbitraries {

    static <A> Arbitrary<Vector<A>> vector(Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                sizeGenerator(parameters),
                                vectorAggregator()),
                just(shrinkVector(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    static <A> Arbitrary<Vector<A>> vectorOfN(int count, Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                Generators.constant(count).prepare(parameters),
                                vectorAggregator()),
                just(shrinkVector(count, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVector(Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                sizeGenerator(1, parameters),
                                vectorAggregator())
                                .fmap(Vector::toNonEmptyOrThrow),
                just(shrinkNonEmptyVector(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVectorOfN(int count, Arbitrary<A> elements) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be >= 1");
        }
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                Generators.constant(count).prepare(parameters),
                                vectorAggregator())
                                .fmap(Vector::toNonEmptyOrThrow),
                just(shrinkNonEmptyVector(count, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }


    static <A> Arbitrary<ArrayList<A>> arrayList(Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(ArrayList::new)),
                just(shrinkArrayList(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }


    static <A> Arbitrary<ArrayList<A>> arrayListOfN(int count, Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                Generators.constant(count).prepare(parameters),
                                collectionAggregator(ArrayList::new)),
                just(shrinkArrayList(count, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }


    static <A> Arbitrary<ArrayList<A>> nonEmptyArrayList(Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(ArrayList::new)),
                just(shrinkArrayList(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }


    static <A> Arbitrary<HashSet<A>> hashSet(Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(HashSet::new)),
                just(shrinkHashSet(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }


    static <A> Arbitrary<HashSet<A>> nonEmptyHashSet(Arbitrary<A> elements) {
        return arbitrary(parameters ->
                        new CollectionSupplyStrategy<>(elements.supplyStrategy(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(HashSet::new)),
                just(shrinkHashSet(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    static <K, V> Arbitrary<HashMap<K, V>> hashMap(Arbitrary<K> keys,
                                                   Arbitrary<V> values) {
        return convertToHashMap(Arbitraries.tuplesOf(keys, values).vector());
    }

    static <K, V> Arbitrary<HashMap<K, V>> nonEmptyHashMap(Arbitrary<K> keys,
                                                           Arbitrary<V> values) {
        return convertToNonEmptyHashMap(Arbitraries.tuplesOf(keys, values).nonEmptyVector());
    }

    static <Collection> Arbitrary<Collection> customHomogeneousCollection(Fn1<? super Vector<?>, ? extends Collection> fromVector,
                                                                          Fn1<? super Collection, ? extends Vector<?>> toVector) {
        return homogeneousVector().convert(fromVector, toVector);
    }

    static <Collection> Arbitrary<Collection> customHomogeneousCollection(Iso<? super Vector<?>, ? extends Vector<?>, ? extends Collection, ? super Collection> iso) {
        return homogeneousVector().convert(iso);
    }

    static <Collection> Arbitrary<Collection> customHomogeneousCollection(Fn1<? super Vector<?>, ? extends Collection> fromVector,
                                                                          Fn1<? super Collection, ? extends Vector<?>> toVector,
                                                                          IntRange sizeRange) {
        return homogeneousVector(sizeRange).convert(fromVector, toVector);
    }

    static <Collection> Arbitrary<Collection> customHomogeneousCollection(Iso<? super Vector<?>, ? extends Vector<?>, ? extends Collection, ? super Collection> iso,
                                                                          IntRange sizeRange) {
        return homogeneousVector(sizeRange).convert(iso);
    }

    static Arbitrary<Vector<?>> homogeneousVector() {
        return homogeneousVector(CollectionArbitraries::sizeGenerator);
    }

    static Arbitrary<Vector<?>> homogeneousVector(IntRange sizeRange) {
        if (sizeRange.minInclusive() < 0 || sizeRange.maxInclusive() < 0) {
            throw new IllegalArgumentException("size must be >= 0");
        }
        return homogeneousVector(parameters -> sizeGenerator(sizeRange, parameters));
    }

    @SuppressWarnings("unchecked")
    static Arbitrary<Vector<?>> homogeneousVector(Fn1<GeneratorParameters, Generate<Integer>> buildSizeGenerator) {
        ShrinkStrategy<Vector<?>> shrink = (ShrinkStrategy<Vector<?>>) (ShrinkStrategy<? extends Vector<?>>) shrinkVector(ShrinkStrategy.none());
        return arbitrary(parameters -> {
                    SupplyStrategy<Arbitrary<?>> arbitrarySupply = arbitraryArbitrary().supplyStrategy(parameters);
                    return new HomogeneousCollectionSupplyStrategy(arbitrarySupply,
                            buildSizeGenerator.apply(parameters),
                            parameters);
                },
                just(shrink), defaultPrettyPrinter());
    }

    static Arbitrary<Arbitrary<?>> arbitraryArbitrary() {
        return Arbitrary.arbitrary(generateArbitrary());
    }

    private static <K, V> Arbitrary<HashMap<K, V>> convertToHashMap(Arbitrary<Vector<Tuple2<K, V>>> entries) {
        return entries.convert(pairs -> ToMap.toMap(HashMap::new, pairs),
                (HashMap<K, V> map) -> {
                    VectorBuilder<Tuple2<K, V>> builder = Vector.builder();
                    for (K key : map.keySet()) {
                        builder = builder.add(tuple(key, map.get(key)));
                    }
                    return builder.build();
                });
    }

    private static <K, V> Arbitrary<HashMap<K, V>> convertToNonEmptyHashMap(Arbitrary<NonEmptyVector<Tuple2<K, V>>> entries) {
        return entries.convert(pairs -> ToMap.toMap(HashMap::new, pairs),
                (HashMap<K, V> map) -> {
                    VectorBuilder<Tuple2<K, V>> builder = Vector.builder();
                    for (K key : map.keySet()) {
                        builder = builder.add(tuple(key, map.get(key)));
                    }
                    return builder.build().toNonEmptyOrThrow();
                });
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

    private static Generate<Integer> sizeGenerator(IntRange sizeRange, GeneratorParameters parameters) {
        return generateInt(sizeRange)
                .prepare(parameters);
    }

    // TODO: move to kraftwerk?
    private static <A> Aggregator<A, VectorBuilder<A>, Vector<A>> vectorAggregator() {
        return aggregator(VectorBuilder::builder, VectorBuilder::add, VectorBuilder::build);
    }
}
