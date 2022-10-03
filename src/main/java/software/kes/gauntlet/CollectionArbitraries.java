package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn2.ToMap;
import software.kes.collectionviews.NonEmptyVector;
import software.kes.collectionviews.Vector;
import software.kes.collectionviews.VectorBuilder;
import software.kes.gauntlet.shrink.ShrinkStrategy;
import software.kes.gauntlet.shrink.builtins.ShrinkStrategies;
import software.kes.kraftwerk.GenerateFn;
import software.kes.kraftwerk.GeneratorParameters;
import software.kes.kraftwerk.aggregator.Aggregator;
import software.kes.kraftwerk.constraints.IntRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateSize;
import static software.kes.kraftwerk.aggregator.Aggregator.aggregator;
import static software.kes.kraftwerk.aggregator.Aggregators.collectionAggregator;

final class CollectionArbitraries {
    static <A> Arbitrary<Vector<A>> vector(Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(parameters),
                                vectorAggregator()),
                just(ShrinkStrategies.shrinkVector(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }

    static <A> Arbitrary<Vector<A>> vectorOfSize(IntRange sizeRange, Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(sizeRange, parameters),
                                vectorAggregator()),
                just(ShrinkStrategies.shrinkVector(sizeRange.minInclusive(), elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }

    static <A> Arbitrary<Vector<A>> vectorOfSize(int count, Arbitrary<A> elements) {
        return vectorOfSize(IntRange.inclusive(count, count), elements);
    }

    static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVector(Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(1, parameters),
                                vectorAggregator())
                                .fmap(Vector::toNonEmptyOrThrow),
                just(ShrinkStrategies.shrinkNonEmptyVector(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }

    static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVectorOfSize(IntRange sizeRange, Arbitrary<A> elements) {
        Preconditions.requirePositiveSize(sizeRange);
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(sizeRange, parameters),
                                vectorAggregator())
                                .fmap(Vector::toNonEmptyOrThrow),
                just(ShrinkStrategies.shrinkNonEmptyVector(sizeRange.minInclusive(), elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }

    static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVectorOfSize(int size, Arbitrary<A> elements) {
        return nonEmptyVectorOfSize(IntRange.inclusive(size, size), elements);
    }

    static <A> Arbitrary<ArrayList<A>> arrayList(Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(ArrayList::new)),
                just(ShrinkStrategies.shrinkArrayList(elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }


    static <A> Arbitrary<ArrayList<A>> arrayListOfSize(IntRange sizeRange, Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(sizeRange, parameters),
                                collectionAggregator(ArrayList::new)),
                just(ShrinkStrategies.shrinkArrayList(sizeRange.minInclusive(), elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }

    static <A> Arbitrary<ArrayList<A>> arrayListOfSize(int count, Arbitrary<A> elements) {
        return arrayListOfSize(IntRange.inclusive(count, count), elements);
    }


    static <A> Arbitrary<ArrayList<A>> nonEmptyArrayList(Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(ArrayList::new)),
                just(ShrinkStrategies.shrinkArrayList(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }


    static <A> Arbitrary<HashSet<A>> hashSet(Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(parameters),
                                collectionAggregator(HashSet::new)),
                just(ShrinkStrategies.shrinkHashSet(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }


    static <A> Arbitrary<HashSet<A>> nonEmptyHashSet(Arbitrary<A> elements) {
        return Arbitrary.arbitrary(parameters ->
                        new CollectionSupply<>(elements.createSupply(parameters),
                                sizeGenerator(1, parameters),
                                collectionAggregator(HashSet::new)),
                just(ShrinkStrategies.shrinkHashSet(1, elements.getShrinkStrategy().orElse(ShrinkStrategy.none()))),
                // TODO: prettyPrinter
                PrettyPrinter.defaultPrettyPrinter());
    }

    static <K, V> Arbitrary<HashMap<K, V>> hashMap(Arbitrary<K> keys,
                                                   Arbitrary<V> values) {
        return convertToHashMap(Arbitraries.tuplesOf(keys, values).vector());
    }

    static <K, V> Arbitrary<HashMap<K, V>> nonEmptyHashMap(Arbitrary<K> keys,
                                                           Arbitrary<V> values) {
        return convertToNonEmptyHashMap(Arbitraries.tuplesOf(keys, values).nonEmptyVector());
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

    private static GenerateFn<Integer> sizeGenerator(GeneratorParameters parameters) {
        return generateSize()
                .createGenerateFn(parameters);
    }

    private static GenerateFn<Integer> sizeGenerator(int minSize, GeneratorParameters parameters) {
        return generateSize()
                .fmap(s -> Math.max(minSize, s))
                .createGenerateFn(parameters);
    }

    private static GenerateFn<Integer> sizeGenerator(IntRange sizeRange, GeneratorParameters parameters) {
        return generateInt(sizeRange)
                .createGenerateFn(parameters);
    }

    // TODO: move to kraftwerk?
    private static <A> Aggregator<A, VectorBuilder<A>, Vector<A>> vectorAggregator() {
        return aggregator(VectorBuilder::builder, VectorBuilder::add, VectorBuilder::build);
    }
}
