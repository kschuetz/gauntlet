package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import dev.marksman.kraftwerk.weights.EitherWeights;
import dev.marksman.kraftwerk.weights.MaybeWeights;

import java.util.ArrayList;
import java.util.HashSet;

import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkByte;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkDouble;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkFloat;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkInt;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkLong;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkShort;
import static dev.marksman.kraftwerk.Generators.generateBoxedPrimitive;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateChar;
import static dev.marksman.kraftwerk.Generators.generateDouble;
import static dev.marksman.kraftwerk.Generators.generateFloat;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateIntIndex;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateLongIndex;
import static dev.marksman.kraftwerk.Generators.generateShort;

public final class Arbitraries {
    private Arbitraries() {
    }

    public static Arbitrary<Integer> ints(Generator<Integer> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkInt());
    }

    public static Arbitrary<Integer> ints() {
        return ints(generateInt());
    }

    public static Arbitrary<Integer> ints(IntRange range) {
        return arbitrary(generateInt(range)).withShrinkStrategy(shrinkInt(range));
    }

    public static Arbitrary<Integer> ints(FrequencyMap<Integer> frequencyMap) {
        return ints(frequencyMap.toGenerator());
    }

    /**
     * An arbitrary that generates an integer (0 &lt;= n &lt; bound) that is intended to be used
     * as an index into a collection or sequence.  Output is uniform and unaffected by bias
     * settings (i.e., there will be no emphasis on edge cases).
     */
    public static Arbitrary<Integer> intIndices(int bound) {
        return arbitrary(generateIntIndex(bound));
    }

    public static Arbitrary<Long> longs(Generator<Long> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkLong());
    }

    public static Arbitrary<Long> longs() {
        return longs(generateLong());
    }

    public static Arbitrary<Long> longs(LongRange range) {
        return arbitrary(generateLong(range)).withShrinkStrategy(shrinkLong(range));
    }

    public static Arbitrary<Long> longs(FrequencyMap<Long> frequencyMap) {
        return longs(frequencyMap.toGenerator());
    }

    /**
     * An arbitrary that generates a long (0 &lt;= n &lt; bound) that is intended to be used
     * as an index into a collection or sequence.  Output is uniform and unaffected by bias
     * settings (i.e., there will be no emphasis on edge cases).
     */
    public static Arbitrary<Long> longIndices(long bound) {
        return arbitrary(generateLongIndex(bound));
    }

    public static Arbitrary<Short> shorts(Generator<Short> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkShort());
    }

    public static Arbitrary<Short> shorts() {
        return shorts(generateShort());
    }

    public static Arbitrary<Short> shorts(ShortRange range) {
        return arbitrary(generateShort(range)).withShrinkStrategy(shrinkShort(range));
    }

    public static Arbitrary<Short> shorts(FrequencyMap<Short> frequencyMap) {
        return shorts(frequencyMap.toGenerator());
    }

    public static Arbitrary<Byte> bytes(Generator<Byte> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkByte());
    }

    public static Arbitrary<Byte> bytes() {
        return bytes(generateByte());
    }

    public static Arbitrary<Byte> bytes(ByteRange range) {
        return arbitrary(generateByte(range)).withShrinkStrategy(shrinkByte(range));
    }

    public static Arbitrary<Byte> bytes(FrequencyMap<Byte> frequencyMap) {
        return bytes(frequencyMap.toGenerator());
    }

    public static Arbitrary<Character> characters(Generator<Character> generator) {
        return arbitrary(generator); // TODO: shrink characters
    }

    public static Arbitrary<Character> characters() {
        return characters(generateChar());
    }

    public static Arbitrary<Character> characters(CharRange range) {
        return arbitrary(generateChar(range));  // TODO: shrink characters
    }

    public static Arbitrary<Character> characters(FrequencyMap<Character> frequencyMap) {
        return characters(frequencyMap.toGenerator());
    }

    public static Arbitrary<Float> floats(Generator<Float> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkFloat());
    }

    public static Arbitrary<Float> floats() {
        return floats(generateFloat());
    }

    public static Arbitrary<Float> floats(FloatRange range) {
        return arbitrary(generateFloat(range)).withShrinkStrategy(shrinkFloat(range));
    }

    public static Arbitrary<Float> floats(FrequencyMap<Float> frequencyMap) {
        return floats(frequencyMap.toGenerator());
    }

    public static Arbitrary<Double> doubles(Generator<Double> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkDouble());
    }

    public static Arbitrary<Double> doubles() {
        return doubles(generateDouble());
    }

    public static Arbitrary<Double> doubles(DoubleRange range) {
        return arbitrary(generateDouble(range)).withShrinkStrategy(shrinkDouble(range));
    }

    public static Arbitrary<Double> doubles(FrequencyMap<Double> frequencyMap) {
        return doubles(frequencyMap.toGenerator());
    }

    public static Arbitrary<Object> boxedPrimitives() {
        return arbitrary(generateBoxedPrimitive());
    }

    public static <A, B> Arbitrary<Tuple2<A, B>> tuplesOf(Arbitrary<A> a,
                                                          Arbitrary<B> b) {
        return CompositeArbitraries.combine(a, b);
    }

    public static <A, B, C> Arbitrary<Tuple3<A, B, C>> tuplesOf(Arbitrary<A> a,
                                                                Arbitrary<B> b,
                                                                Arbitrary<C> c) {
        return CompositeArbitraries.combine(a, b, c);
    }

    public static <A, B, C, D> Arbitrary<Tuple4<A, B, C, D>> tuplesOf(Arbitrary<A> a,
                                                                      Arbitrary<B> b,
                                                                      Arbitrary<C> c,
                                                                      Arbitrary<D> d) {
        return CompositeArbitraries.combine(a, b, c, d);
    }

    public static <A, B, C, D, E> Arbitrary<Tuple5<A, B, C, D, E>> tuplesOf(Arbitrary<A> a,
                                                                            Arbitrary<B> b,
                                                                            Arbitrary<C> c,
                                                                            Arbitrary<D> d,
                                                                            Arbitrary<E> e) {
        return CompositeArbitraries.combine(a, b, c, d, e);
    }

    public static Arbitrary<Unit> unit() {
        return CoProductArbitraries.arbitraryUnit();
    }

    public static <A, B> Arbitrary<Choice2<A, B>> choicesOf(Weighted<Arbitrary<A>> a,
                                                            Weighted<Arbitrary<B>> b) {
        return CoProductArbitraries.arbitraryChoice2(a, b);
    }

    public static <A, B> Arbitrary<Choice2<A, B>> choicesOf(Arbitrary<A> a,
                                                            Arbitrary<B> b) {
        return CoProductArbitraries.arbitraryChoice2(a, b);
    }

    public static <A, B, C> Arbitrary<Choice3<A, B, C>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                  Weighted<Arbitrary<B>> b,
                                                                  Weighted<Arbitrary<C>> c) {
        return CoProductArbitraries.arbitraryChoice3(a, b, c);
    }

    public static <A, B, C> Arbitrary<Choice3<A, B, C>> choicesOf(Arbitrary<A> a,
                                                                  Arbitrary<B> b,
                                                                  Arbitrary<C> c) {
        return CoProductArbitraries.arbitraryChoice3(a, b, c);
    }

    public static <A> Arbitrary<Maybe<A>> maybesOf(MaybeWeights weights,
                                                   Arbitrary<A> a) {
        return CoProductArbitraries.arbitraryMaybe(weights, a);
    }

    public static <A> Arbitrary<Maybe<A>> maybesOf(Arbitrary<A> a) {
        return CoProductArbitraries.arbitraryMaybe(a);
    }

    public static <L, R> Arbitrary<Either<L, R>> eithersOf(Weighted<Arbitrary<L>> left,
                                                           Weighted<Arbitrary<R>> right) {
        return CoProductArbitraries.arbitraryEither(left, right);
    }

    public static <L, R> Arbitrary<Either<L, R>> eithersOf(Arbitrary<L> left,
                                                           Arbitrary<R> right) {
        return CoProductArbitraries.arbitraryEither(left, right);
    }

    public static <L, R> Arbitrary<Either<L, R>> eithersOf(EitherWeights weights,
                                                           Arbitrary<L> left,
                                                           Arbitrary<R> right) {
        return CoProductArbitraries.arbitraryEither(weights, left, right);
    }

    public static <A> Arbitrary<ImmutableVector<A>> vectorsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.vector(elements);
    }

    public static <A> Arbitrary<ImmutableVector<A>> vectorsOf(int count, Arbitrary<A> elements) {
        return CollectionArbitraries.vectorOfN(count, elements);
    }

    public static <A> Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVectorsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyVector(elements);
    }

    public static <A> Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVectorsOf(int count, Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyVectorOfN(count, elements);
    }

    public static <A> Arbitrary<ArrayList<A>> arrayListsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.arrayList(elements);
    }

    public static <A> Arbitrary<ArrayList<A>> arrayListsOf(int count, Arbitrary<A> elements) {
        return CollectionArbitraries.arrayListOfN(count, elements);
    }

    public static <A> Arbitrary<ArrayList<A>> nonEmptyArrayListsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyArrayList(elements);
    }

    public static <A> Arbitrary<HashSet<A>> hashSetsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.hashSet(elements);
    }

    public static <A> Arbitrary<HashSet<A>> nonEmptyHashSetsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyHashSet(elements);
    }
}
