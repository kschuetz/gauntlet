package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.constraints.BigDecimalRange;
import dev.marksman.kraftwerk.constraints.BigIntegerRange;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.DurationRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LocalDateRange;
import dev.marksman.kraftwerk.constraints.LocalDateTimeRange;
import dev.marksman.kraftwerk.constraints.LocalTimeRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import dev.marksman.kraftwerk.domain.Characters;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import dev.marksman.kraftwerk.weights.EitherWeights;
import dev.marksman.kraftwerk.weights.MaybeWeights;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.Arbitrary.higherOrderArbitrary;
import static dev.marksman.gauntlet.ArbitraryGenerator.generateArbitrary;
import static dev.marksman.gauntlet.Preconditions.requireNaturalSize;
import static dev.marksman.gauntlet.Preconditions.requirePositiveSize;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkBoolean;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkByte;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkDouble;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkFloat;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkHashMap;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkInt;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkLong;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkShort;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkString;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkVector;
import static dev.marksman.kraftwerk.Generators.generateBigDecimal;
import static dev.marksman.kraftwerk.Generators.generateBigInteger;
import static dev.marksman.kraftwerk.Generators.generateBigIntegerRange;
import static dev.marksman.kraftwerk.Generators.generateBoolean;
import static dev.marksman.kraftwerk.Generators.generateBoxedPrimitive;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateByteArray;
import static dev.marksman.kraftwerk.Generators.generateByteRange;
import static dev.marksman.kraftwerk.Generators.generateChar;
import static dev.marksman.kraftwerk.Generators.generateDayOfWeek;
import static dev.marksman.kraftwerk.Generators.generateDouble;
import static dev.marksman.kraftwerk.Generators.generateDoubleFractional;
import static dev.marksman.kraftwerk.Generators.generateDoubleRange;
import static dev.marksman.kraftwerk.Generators.generateDuration;
import static dev.marksman.kraftwerk.Generators.generateDurationRange;
import static dev.marksman.kraftwerk.Generators.generateFloat;
import static dev.marksman.kraftwerk.Generators.generateFloatFractional;
import static dev.marksman.kraftwerk.Generators.generateFloatRange;
import static dev.marksman.kraftwerk.Generators.generateFromEnum;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateIntIndex;
import static dev.marksman.kraftwerk.Generators.generateIntRange;
import static dev.marksman.kraftwerk.Generators.generateLocalDate;
import static dev.marksman.kraftwerk.Generators.generateLocalDateForMonth;
import static dev.marksman.kraftwerk.Generators.generateLocalDateForYear;
import static dev.marksman.kraftwerk.Generators.generateLocalDateRange;
import static dev.marksman.kraftwerk.Generators.generateLocalDateTime;
import static dev.marksman.kraftwerk.Generators.generateLocalDateTimeRange;
import static dev.marksman.kraftwerk.Generators.generateLocalTime;
import static dev.marksman.kraftwerk.Generators.generateLocalTimeRange;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateLongIndex;
import static dev.marksman.kraftwerk.Generators.generateLongRange;
import static dev.marksman.kraftwerk.Generators.generateMonth;
import static dev.marksman.kraftwerk.Generators.generateSeed;
import static dev.marksman.kraftwerk.Generators.generateShort;
import static dev.marksman.kraftwerk.Generators.generateShortRange;
import static dev.marksman.kraftwerk.Generators.generateShuffled;
import static dev.marksman.kraftwerk.Generators.generateString;
import static dev.marksman.kraftwerk.Generators.generateStringFromCharacters;
import static dev.marksman.kraftwerk.Generators.generateUUID;

public final class Arbitraries {
    private Arbitraries() {
    }

    public static Arbitrary<Integer> ints(Generator<Integer> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkInt());
    }

    /**
     * An arbitrary that generates an {@link Integer} within the specified range.
     */
    public static Arbitrary<Integer> ints() {
        return ints(generateInt());
    }

    /**
     * An arbitrary that generates {@link Integer}s within the specified range.
     */
    public static Arbitrary<Integer> ints(IntRange range) {
        return arbitrary(generateInt(range)).withShrinkStrategy(shrinkInt(range));
    }

    public static Arbitrary<Integer> ints(FrequencyMap<Integer> frequencyMap) {
        return ints(frequencyMap.toGenerator());
    }

    /**
     * An arbitrary that generates {@link Integer}s (0 &lt;= n &lt; bound) that are intended to be used
     * as an index into a collection or sequence.  Output is uniform and unaffected by bias
     * settings (i.e., there will be no emphasis on edge cases).
     */
    public static Arbitrary<Integer> intIndices(int bound) {
        return arbitrary(generateIntIndex(bound));
    }

    /**
     * An arbitrary that generates {@link Integer}s &gt;= 0.
     */
    public static Arbitrary<Integer> intNaturals() {
        return ints(IntRange.from(0).to(Integer.MAX_VALUE));
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
     * An arbitrary that generates {@link Long}s (0 &lt;= n &lt; bound) that are intended to be used
     * as an index into a collection or sequence.  Output is uniform and unaffected by bias
     * settings (i.e., there will be no emphasis on edge cases).
     */
    public static Arbitrary<Long> longIndices(long bound) {
        return arbitrary(generateLongIndex(bound));
    }

    /**
     * An arbitrary that generates {@link Long}s &gt;= 0.
     */
    public static Arbitrary<Long> longNaturals() {
        return longs(LongRange.from(0).to(Long.MAX_VALUE));
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

    public static Arbitrary<Short> shortNaturals() {
        return shorts(ShortRange.from((short) 0).to(Short.MAX_VALUE));
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

    public static Arbitrary<Boolean> booleans(Generator<Boolean> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkBoolean());
    }

    public static Arbitrary<Boolean> booleans() {
        return booleans(generateBoolean());
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

    /**
     * An arbitrary that generates {@link Float}s between 0 (inclusive) and 1 (exclusive).
     */
    public static Arbitrary<Float> floatFractionals() {
        return arbitrary(generateFloatFractional()).withShrinkStrategy(shrinkFloat(FloatRange.exclusive(1)));
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

    /**
     * An arbitrary that generates {@link Double}s between 0 (inclusive) and 1 (exclusive).
     */
    public static Arbitrary<Double> doubleFractionals() {
        return arbitrary(generateDoubleFractional()).withShrinkStrategy(shrinkDouble(DoubleRange.exclusive(1)));
    }

    public static Arbitrary<Byte[]> byteArrays() {
        return arbitrary(generateByteArray());
    }

    public static Arbitrary<Byte[]> byteArrays(int size) {
        return arbitrary(generateByteArray(size));
    }

    public static Arbitrary<Object> boxedPrimitives() {
        return arbitrary(generateBoxedPrimitive());
    }

    public static Arbitrary<String> strings(Generator<String> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkString());
    }

    public static Arbitrary<String> strings() {
        return strings(generateString());
    }

    public static Arbitrary<String> strings(FrequencyMap<String> frequencyMap) {
        return strings(frequencyMap.toGenerator());
    }

    public static Arbitrary<String> stringsOfLength(int length) {
        requireNaturalSize(length);
        return arbitrary(generateStringFromCharacters(length, Characters.asciiPrintable()))
                .withShrinkStrategy(shrinkString(length));
    }

    public static Arbitrary<String> stringsOfLength(IntRange lengthRange) {
        requireNaturalSize(lengthRange);
        return arbitrary(generateInt(lengthRange)
                .flatMap(length -> generateStringFromCharacters(length, Characters.asciiPrintable())))
                .withShrinkStrategy(shrinkString(lengthRange.minInclusive()));
    }

    public static Arbitrary<BigInteger> bigIntegers(Generator<BigInteger> generator) {
        return arbitrary(generator); // TODO: shrink BigIntegers
    }

    public static Arbitrary<BigInteger> bigIntegers() {
        return bigIntegers(generateBigInteger());
    }

    public static Arbitrary<BigInteger> bigIntegers(BigIntegerRange range) {
        return bigIntegers(generateBigInteger(range));
    }

    public static Arbitrary<BigInteger> bigIntegers(FrequencyMap<BigInteger> frequencyMap) {
        return bigIntegers(frequencyMap.toGenerator());
    }

    public static Arbitrary<BigDecimal> bigDecimals(Generator<BigDecimal> generator) {
        return arbitrary(generator); // TODO: shrink BigDecimal
    }

    public static Arbitrary<BigDecimal> bigDecimals() {
        return bigDecimals(generateBigDecimal());
    }

    public static Arbitrary<BigDecimal> bigDecimals(BigDecimalRange range) {
        return bigDecimals(generateBigDecimal(range));
    }

    public static Arbitrary<BigDecimal> bigDecimals(int decimalPlaces, BigDecimalRange range) {
        return bigDecimals(generateBigDecimal(decimalPlaces, range));
    }

    public static Arbitrary<BigDecimal> bigDecimals(IntRange decimalPlaces, BigDecimalRange range) {
        return bigDecimals(generateBigDecimal(generateInt(decimalPlaces), range));
    }

    public static Arbitrary<BigDecimal> bigDecimals(FrequencyMap<BigDecimal> frequencyMap) {
        return bigDecimals(frequencyMap.toGenerator());
    }

    public static <A extends Enum<A>> Arbitrary<A> valuesOfEnumClass(Class<A> enumType) {
        return arbitrary(generateFromEnum(enumType));
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

    public static <A, B, C, D, E, F> Arbitrary<Tuple6<A, B, C, D, E, F>> tuplesOf(Arbitrary<A> a,
                                                                                  Arbitrary<B> b,
                                                                                  Arbitrary<C> c,
                                                                                  Arbitrary<D> d,
                                                                                  Arbitrary<E> e,
                                                                                  Arbitrary<F> f) {
        return CompositeArbitraries.combine(a, b, c, d, e, f);
    }

    public static <A, B, C, D, E, F, G> Arbitrary<Tuple7<A, B, C, D, E, F, G>> tuplesOf(Arbitrary<A> a,
                                                                                        Arbitrary<B> b,
                                                                                        Arbitrary<C> c,
                                                                                        Arbitrary<D> d,
                                                                                        Arbitrary<E> e,
                                                                                        Arbitrary<F> f,
                                                                                        Arbitrary<G> g) {
        return CompositeArbitraries.combine(a, b, c, d, e, f, g);
    }

    public static <A, B, C, D, E, F, G, H> Arbitrary<Tuple8<A, B, C, D, E, F, G, H>> tuplesOf(Arbitrary<A> a,
                                                                                              Arbitrary<B> b,
                                                                                              Arbitrary<C> c,
                                                                                              Arbitrary<D> d,
                                                                                              Arbitrary<E> e,
                                                                                              Arbitrary<F> f,
                                                                                              Arbitrary<G> g,
                                                                                              Arbitrary<H> h) {
        return CompositeArbitraries.combine(a, b, c, d, e, f, g, h);
    }

    public static Arbitrary<Tuple2<?, ?>> tuple2s() {
        return higherOrderArbitrary(generateArbitrary().pair(), x -> tuplesOf(x._1(), x._2()));
    }

    public static Arbitrary<Tuple3<?, ?, ?>> tuple3s() {
        return higherOrderArbitrary(generateArbitrary().triple(), x -> tuplesOf(x._1(), x._2(), x._3()));
    }

    public static Arbitrary<Tuple4<?, ?, ?, ?>> tuple4s() {
        return higherOrderArbitrary(Generators.tupled(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary()), x -> tuplesOf(x._1(), x._2(), x._3(), x._4()));
    }

    public static Arbitrary<Tuple5<?, ?, ?, ?, ?>> tuple5s() {
        return higherOrderArbitrary(Generators.tupled(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary(), generateArbitrary()), x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5()));
    }

    public static Arbitrary<Tuple6<?, ?, ?, ?, ?, ?>> tuple6s() {
        return higherOrderArbitrary(Generators.tupled(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary(), generateArbitrary(), generateArbitrary()), x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5(), x._6()));
    }

    public static Arbitrary<Tuple7<?, ?, ?, ?, ?, ?, ?>> tuple7s() {
        return higherOrderArbitrary(Generators.tupled(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary(), generateArbitrary(), generateArbitrary(), generateArbitrary()),
                x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5(), x._6(), x._7()));
    }

    public static Arbitrary<Tuple8<?, ?, ?, ?, ?, ?, ?, ?>> tuple8s() {
        return higherOrderArbitrary(Generators.tupled(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary(), generateArbitrary(), generateArbitrary(), generateArbitrary(), generateArbitrary()),
                x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5(), x._6(), x._7(), x._8()));
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

    public static <A, B, C, D> Arbitrary<Choice4<A, B, C, D>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                        Weighted<Arbitrary<B>> b,
                                                                        Weighted<Arbitrary<C>> c,
                                                                        Weighted<Arbitrary<D>> d) {
        return CoProductArbitraries.arbitraryChoice4(a, b, c, d);
    }

    public static <A, B, C, D> Arbitrary<Choice4<A, B, C, D>> choicesOf(Arbitrary<A> a,
                                                                        Arbitrary<B> b,
                                                                        Arbitrary<C> c,
                                                                        Arbitrary<D> d) {
        return CoProductArbitraries.arbitraryChoice4(a, b, c, d);
    }

    public static <A, B, C, D, E> Arbitrary<Choice5<A, B, C, D, E>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                              Weighted<Arbitrary<B>> b,
                                                                              Weighted<Arbitrary<C>> c,
                                                                              Weighted<Arbitrary<D>> d,
                                                                              Weighted<Arbitrary<E>> e) {
        return CoProductArbitraries.arbitraryChoice5(a, b, c, d, e);
    }

    public static <A, B, C, D, E> Arbitrary<Choice5<A, B, C, D, E>> choicesOf(Arbitrary<A> a,
                                                                              Arbitrary<B> b,
                                                                              Arbitrary<C> c,
                                                                              Arbitrary<D> d,
                                                                              Arbitrary<E> e) {
        return CoProductArbitraries.arbitraryChoice5(a, b, c, d, e);
    }

    public static <A, B, C, D, E, F> Arbitrary<Choice6<A, B, C, D, E, F>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                                    Weighted<Arbitrary<B>> b,
                                                                                    Weighted<Arbitrary<C>> c,
                                                                                    Weighted<Arbitrary<D>> d,
                                                                                    Weighted<Arbitrary<E>> e,
                                                                                    Weighted<Arbitrary<F>> f) {
        return CoProductArbitraries.arbitraryChoice6(a, b, c, d, e, f);
    }

    public static <A, B, C, D, E, F> Arbitrary<Choice6<A, B, C, D, E, F>> choicesOf(Arbitrary<A> a,
                                                                                    Arbitrary<B> b,
                                                                                    Arbitrary<C> c,
                                                                                    Arbitrary<D> d,
                                                                                    Arbitrary<E> e,
                                                                                    Arbitrary<F> f) {
        return CoProductArbitraries.arbitraryChoice6(a, b, c, d, e, f);
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

    /**
     * An arbitrary that, given one or more source arbitraries, will randomly select and draw from these sources,
     * with probability to be determined by the provided weights.
     *
     * @param first the first source in the frequency map.  The resulting {@code Arbitrary} will assume the
     *              {@link ShrinkStrategy} and {@link PrettyPrinter} of this argument.
     * @param rest  additional sources in the frequency map.
     * @return an {@code Arbitrary<A>}
     */
    @SafeVarargs
    public static <A> Arbitrary<A> frequencies(Weighted<Arbitrary<A>> first,
                                               Weighted<Arbitrary<A>>... rest) {
        return FrequencyMapArbitrary.frequencies(first, rest);
    }

    /**
     * An arbitrary that, given one or more source arbitraries, will randomly select and draw from these sources,
     * with equal probability for each source.
     *
     * @param first the first entry in the frequency map.  The resulting {@code Arbitrary} will assume the
     *              {@link ShrinkStrategy} and {@link PrettyPrinter} of this argument.
     * @param rest  additional sources in the frequency map.
     * @return an {@code Arbitrary<A>}
     */
    @SafeVarargs
    public static <A> Arbitrary<A> frequencies(Arbitrary<A> first,
                                               Arbitrary<A>... rest) {
        return FrequencyMapArbitrary.frequencies(first, rest);
    }

    public static <Collection> Arbitrary<Collection> homogeneousCollections(Fn1<? super Vector<?>, ? extends Collection> fromVector,
                                                                            Fn1<? super Collection, ? extends Vector<?>> toVector) {
        return vectors().convert(fromVector, toVector);
    }

    public static <Collection> Arbitrary<Collection> homogeneousCollections(Iso<? super Vector<?>, ? extends Vector<?>, ? extends Collection, ? super Collection> iso) {
        return vectors().convert(iso);
    }

    public static <Collection> Arbitrary<Collection> homogeneousCollections(Fn1<? super Vector<?>, ? extends Collection> fromVector,
                                                                            Fn1<? super Collection, ? extends Vector<?>> toVector,
                                                                            int size) {
        return vectors(size).convert(fromVector, toVector);
    }

    public static <Collection> Arbitrary<Collection> homogeneousCollections(Iso<? super Vector<?>, ? extends Vector<?>, ? extends Collection, ? super Collection> iso,
                                                                            int size) {
        return vectors(size).convert(iso);
    }

    public static <Collection> Arbitrary<Collection> homogeneousCollections(Fn1<? super Vector<?>, ? extends Collection> fromVector,
                                                                            Fn1<? super Collection, ? extends Vector<?>> toVector,
                                                                            IntRange sizeRange) {
        return vectors(sizeRange).convert(fromVector, toVector);
    }

    @Deprecated
    public static <Collection> Arbitrary<Collection> homogeneousCollections(Iso<? super Vector<?>, ? extends Vector<?>, ? extends Collection, ? super Collection> iso,
                                                                            IntRange sizeRange) {
        return vectors(sizeRange).convert(iso);
    }

    @SuppressWarnings("unchecked")
    public static Arbitrary<Vector<?>> vectors() {
        ShrinkStrategy<Vector<?>> shrinkStrategy = (ShrinkStrategy<Vector<?>>) (ShrinkStrategy<? extends Vector<?>>) shrinkVector(ShrinkStrategy.none());
        return Arbitrary.<Arbitrary<?>, Vector<?>>higherOrderArbitrary(generateArbitrary(), Arbitrary::vector)
                .withShrinkStrategy(shrinkStrategy);
    }

    public static Arbitrary<Vector<?>> vectors(int size) {
        return Arbitrary.higherOrderArbitrary(generateArbitrary(), a -> a.vectorOfSize(size));
    }

    public static Arbitrary<Vector<?>> vectors(IntRange sizeRange) {
        return Arbitrary.higherOrderArbitrary(generateArbitrary(), a -> a.vectorOfSize(sizeRange));
    }

    public static <A> Arbitrary<Vector<A>> vectorsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.vector(elements);
    }

    public static <A> Arbitrary<Vector<A>> vectorsOf(int size, Arbitrary<A> elements) {
        return CollectionArbitraries.vectorOfSize(size, elements);
    }

    public static Arbitrary<NonEmptyVector<?>> nonEmptyVectors() {
        return Arbitrary.higherOrderArbitrary(generateArbitrary(), Arbitrary::nonEmptyVector);
    }

    public static Arbitrary<NonEmptyVector<?>> nonEmptyVectors(int size) {
        requirePositiveSize(size);
        return Arbitrary.higherOrderArbitrary(generateArbitrary(), elements -> nonEmptyVectorsOf(size, elements));
    }

    public static Arbitrary<NonEmptyVector<?>> nonEmptyVectors(IntRange sizeRange) {
        requirePositiveSize(sizeRange);
        return Arbitrary.higherOrderArbitrary(generateArbitrary(), elements -> nonEmptyVectorsOf(sizeRange, elements));
    }

    public static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVectorsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyVector(elements);
    }

    public static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVectorsOf(int size, Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyVectorOfSize(size, elements);
    }

    public static <A> Arbitrary<NonEmptyVector<A>> nonEmptyVectorsOf(IntRange sizeRange, Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyVectorOfSize(sizeRange, elements);
    }

    public static Arbitrary<ArrayList<?>> arrayLists() {
        return homogeneousCollections(vector -> vector.toCollection(ArrayList::new), Vector::wrap);
    }

    public static Arbitrary<ArrayList<?>> arrayLists(int size) {
        return arrayLists(IntRange.inclusive(size, size));
    }

    public static Arbitrary<ArrayList<?>> arrayLists(IntRange sizeRange) {
        return homogeneousCollections(vector -> vector.toCollection(ArrayList::new), Vector::wrap, sizeRange);
    }

    public static <A> Arbitrary<ArrayList<A>> arrayListsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.arrayList(elements);
    }

    public static <A> Arbitrary<ArrayList<A>> arrayListsOf(int size, Arbitrary<A> elements) {
        return CollectionArbitraries.arrayListOfSize(size, elements);
    }

    public static <A> Arbitrary<ArrayList<A>> arrayListsOf(IntRange sizeRange, Arbitrary<A> elements) {
        return CollectionArbitraries.arrayListOfSize(sizeRange, elements);
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

    @SuppressWarnings("unchecked")
    public static Arbitrary<HashMap<?, ?>> hashMaps() {
        ShrinkStrategy<HashMap<?, ?>> shrinkStrategy = (ShrinkStrategy<HashMap<?, ?>>) (ShrinkStrategy<? extends HashMap<?, ?>>) shrinkHashMap(ShrinkStrategy.none());
        return Arbitrary.<Arbitrary<?>, HashMap<?, ?>>higherOrderArbitrary(generateArbitrary(), keys ->
                higherOrderArbitrary(generateArbitrary(), values -> hashMapsOf(keys, values)))
                .withShrinkStrategy(shrinkStrategy);
    }

    public static <K, V> Arbitrary<HashMap<K, V>> hashMapsOf(Arbitrary<K> keys,
                                                             Arbitrary<V> values) {
        return CollectionArbitraries.hashMap(keys, values);
    }

    public static <K, V> Arbitrary<HashMap<K, V>> nonEmptyHashMapsOf(Arbitrary<K> keys,
                                                                     Arbitrary<V> values) {
        return CollectionArbitraries.nonEmptyHashMap(keys, values);
    }

    public static <A> Arbitrary<LocalDate> localDates() {
        return arbitrary(generateLocalDate());
    }

    public static <A> Arbitrary<LocalDate> localDates(LocalDateRange range) {
        return arbitrary(generateLocalDate(range));
    }

    public static <A> Arbitrary<LocalDate> localDatesForYear(Year year) {
        return arbitrary(generateLocalDateForYear(year));
    }

    public static <A> Arbitrary<LocalDate> localDatesForMonth(YearMonth month) {
        return arbitrary(generateLocalDateForMonth(month));
    }

    public static <A> Arbitrary<LocalTime> localTimes() {
        return arbitrary(generateLocalTime());
    }

    public static <A> Arbitrary<LocalTime> localTimes(LocalTimeRange range) {
        return arbitrary(generateLocalTime(range));
    }

    public static <A> Arbitrary<LocalDateTime> localDateTimes() {
        return arbitrary(generateLocalDateTime());
    }

    public static <A> Arbitrary<LocalDateTime> localDateTimes(LocalDateRange range) {
        return arbitrary(generateLocalDateTime(range));
    }

    public static <A> Arbitrary<LocalDateTime> localDateTimes(LocalDateTimeRange range) {
        return arbitrary(generateLocalDateTime(range));
    }

    public static <A> Arbitrary<Duration> durations() {
        return arbitrary(generateDuration());
    }

    public static <A> Arbitrary<Duration> durations(DurationRange range) {
        return arbitrary(generateDuration(range));
    }

    public static Arbitrary<DayOfWeek> daysOfWeek() {
        return arbitrary(generateDayOfWeek());
    }

    public static Arbitrary<Month> months() {
        return arbitrary(generateMonth());
    }

    public static Arbitrary<UUID> uuids() {
        return arbitrary(generateUUID());
    }

    public static Arbitrary<IntRange> intRanges() {
        return arbitrary(generateIntRange());
    }

    public static Arbitrary<IntRange> intRanges(IntRange parentRange) {
        return arbitrary(generateIntRange(parentRange));
    }

    public static Arbitrary<LongRange> longRanges() {
        return arbitrary(generateLongRange());
    }

    public static Arbitrary<LongRange> longRanges(LongRange parentRange) {
        return arbitrary(generateLongRange(parentRange));
    }

    public static Arbitrary<ShortRange> shortRanges() {
        return arbitrary(generateShortRange());
    }

    public static Arbitrary<ShortRange> shortRanges(ShortRange parentRange) {
        return arbitrary(generateShortRange(parentRange));
    }

    public static Arbitrary<ByteRange> byteRanges() {
        return arbitrary(generateByteRange());
    }

    public static Arbitrary<ByteRange> byteRanges(ByteRange parentRange) {
        return arbitrary(generateByteRange(parentRange));
    }

    public static Arbitrary<DoubleRange> doubleRanges() {
        return arbitrary(generateDoubleRange());
    }

    public static Arbitrary<DoubleRange> doubleRanges(DoubleRange parentRange) {
        return arbitrary(generateDoubleRange(parentRange));
    }

    public static Arbitrary<FloatRange> floatRanges() {
        return arbitrary(generateFloatRange());
    }

    public static Arbitrary<FloatRange> floatRanges(FloatRange parentRange) {
        return arbitrary(generateFloatRange(parentRange));
    }

    public static Arbitrary<BigIntegerRange> bigIntegerRanges() {
        return arbitrary(generateBigIntegerRange());
    }

    public static Arbitrary<BigIntegerRange> bigIntegerRanges(BigIntegerRange parentRange) {
        return arbitrary(generateBigIntegerRange(parentRange));
    }

    public static Arbitrary<LocalDateRange> localDateRanges() {
        return arbitrary(generateLocalDateRange());
    }

    public static Arbitrary<LocalDateRange> localDateRanges(LocalDateRange parentRange) {
        return arbitrary(generateLocalDateRange(parentRange));
    }

    public static Arbitrary<LocalTimeRange> localTimeRanges() {
        return arbitrary(generateLocalTimeRange());
    }

    public static Arbitrary<LocalTimeRange> localTimeRanges(LocalTimeRange parentRange) {
        return arbitrary(generateLocalTimeRange(parentRange));
    }

    public static Arbitrary<LocalDateTimeRange> localDateTimeRanges() {
        return arbitrary(generateLocalDateTimeRange());
    }

    public static Arbitrary<LocalDateTimeRange> localDateTimeRanges(LocalDateRange parentRange) {
        return arbitrary(generateLocalDateTimeRange(parentRange));
    }

    public static Arbitrary<LocalDateTimeRange> localDateTimeRanges(LocalDateTimeRange parentRange) {
        return arbitrary(generateLocalDateTimeRange(parentRange));
    }

    public static Arbitrary<DurationRange> durationRanges() {
        return arbitrary(generateDurationRange());
    }

    public static Arbitrary<DurationRange> durationRanges(DurationRange parentRange) {
        return arbitrary(generateDurationRange(parentRange));
    }

    public static <A> Arbitrary<Vector<A>> shufflesOf(Vector<A> elements) {
        return arbitrary(generateShuffled(elements));
    }

    public static Arbitrary<Vector<Integer>> shufflesOfIndices(int count) {
        return arbitrary(generateShuffled(Vector.range(count)));
    }

    public static <A> Arbitrary<NonEmptyVector<A>> nonEmptyShufflesOf(NonEmptyVector<A> elements) {
        return arbitrary(generateShuffled(elements));
    }

    public static Arbitrary<Seed> seeds() {
        return arbitrary(generateSeed());
    }
}
