package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import software.kes.collectionviews.NonEmptyVector;
import software.kes.collectionviews.Vector;
import software.kes.gauntlet.shrink.ShrinkStrategy;
import software.kes.gauntlet.shrink.builtins.ShrinkStrategies;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Seed;
import software.kes.kraftwerk.Weighted;
import software.kes.kraftwerk.constraints.BigDecimalRange;
import software.kes.kraftwerk.constraints.BigIntegerRange;
import software.kes.kraftwerk.constraints.ByteRange;
import software.kes.kraftwerk.constraints.CharRange;
import software.kes.kraftwerk.constraints.DoubleRange;
import software.kes.kraftwerk.constraints.DurationRange;
import software.kes.kraftwerk.constraints.FloatRange;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.constraints.LocalDateRange;
import software.kes.kraftwerk.constraints.LocalDateTimeRange;
import software.kes.kraftwerk.constraints.LocalTimeRange;
import software.kes.kraftwerk.constraints.LongRange;
import software.kes.kraftwerk.constraints.ShortRange;
import software.kes.kraftwerk.domain.Characters;
import software.kes.kraftwerk.weights.EitherWeights;
import software.kes.kraftwerk.weights.MaybeWeights;

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

import static software.kes.gauntlet.ArbitraryGenerator.generateArbitrary;
import static software.kes.gauntlet.Preconditions.requireNaturalSize;
import static software.kes.gauntlet.Preconditions.requirePositiveSize;
import static software.kes.gauntlet.shrink.builtins.ShrinkStrategies.shrinkString;
import static software.kes.kraftwerk.Generators.generateAlphaChar;
import static software.kes.kraftwerk.Generators.generateAlphaLowerChar;
import static software.kes.kraftwerk.Generators.generateAlphaLowerString;
import static software.kes.kraftwerk.Generators.generateAlphaString;
import static software.kes.kraftwerk.Generators.generateAlphaUpperChar;
import static software.kes.kraftwerk.Generators.generateAlphaUpperString;
import static software.kes.kraftwerk.Generators.generateAlphanumericChar;
import static software.kes.kraftwerk.Generators.generateAlphanumericString;
import static software.kes.kraftwerk.Generators.generateAsciiPrintableChar;
import static software.kes.kraftwerk.Generators.generateBigDecimal;
import static software.kes.kraftwerk.Generators.generateBigInteger;
import static software.kes.kraftwerk.Generators.generateBigIntegerRange;
import static software.kes.kraftwerk.Generators.generateBoolean;
import static software.kes.kraftwerk.Generators.generateBoxedPrimitive;
import static software.kes.kraftwerk.Generators.generateByte;
import static software.kes.kraftwerk.Generators.generateByteArray;
import static software.kes.kraftwerk.Generators.generateByteRange;
import static software.kes.kraftwerk.Generators.generateChar;
import static software.kes.kraftwerk.Generators.generateControlChar;
import static software.kes.kraftwerk.Generators.generateDayOfWeek;
import static software.kes.kraftwerk.Generators.generateDouble;
import static software.kes.kraftwerk.Generators.generateDoubleFractional;
import static software.kes.kraftwerk.Generators.generateDoubleRange;
import static software.kes.kraftwerk.Generators.generateDuration;
import static software.kes.kraftwerk.Generators.generateDurationRange;
import static software.kes.kraftwerk.Generators.generateFloat;
import static software.kes.kraftwerk.Generators.generateFloatFractional;
import static software.kes.kraftwerk.Generators.generateFloatRange;
import static software.kes.kraftwerk.Generators.generateFromEnum;
import static software.kes.kraftwerk.Generators.generateIdentifier;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateIntIndex;
import static software.kes.kraftwerk.Generators.generateIntRange;
import static software.kes.kraftwerk.Generators.generateLocalDate;
import static software.kes.kraftwerk.Generators.generateLocalDateForMonth;
import static software.kes.kraftwerk.Generators.generateLocalDateForYear;
import static software.kes.kraftwerk.Generators.generateLocalDateRange;
import static software.kes.kraftwerk.Generators.generateLocalDateTime;
import static software.kes.kraftwerk.Generators.generateLocalDateTimeRange;
import static software.kes.kraftwerk.Generators.generateLocalTime;
import static software.kes.kraftwerk.Generators.generateLocalTimeRange;
import static software.kes.kraftwerk.Generators.generateLong;
import static software.kes.kraftwerk.Generators.generateLongIndex;
import static software.kes.kraftwerk.Generators.generateLongRange;
import static software.kes.kraftwerk.Generators.generateMonth;
import static software.kes.kraftwerk.Generators.generateNumericChar;
import static software.kes.kraftwerk.Generators.generatePunctuationChar;
import static software.kes.kraftwerk.Generators.generateSeed;
import static software.kes.kraftwerk.Generators.generateShort;
import static software.kes.kraftwerk.Generators.generateShortRange;
import static software.kes.kraftwerk.Generators.generateShuffled;
import static software.kes.kraftwerk.Generators.generateString;
import static software.kes.kraftwerk.Generators.generateStringFromCharacters;
import static software.kes.kraftwerk.Generators.generateTuple;
import static software.kes.kraftwerk.Generators.generateUUID;

public final class Arbitraries {
    private Arbitraries() {
    }

    /**
     * An arbitrary that generates {@link Integer}s using a custom generator.
     */
    public static Arbitrary<Integer> ints(Generator<Integer> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkInt());
    }

    /**
     * An arbitrary that generates {@link Integer}s.
     */
    public static Arbitrary<Integer> ints() {
        return ints(generateInt());
    }

    /**
     * An arbitrary that generates {@link Integer}s within a specific range.
     */
    public static Arbitrary<Integer> ints(IntRange range) {
        return Arbitrary.arbitrary(generateInt(range)).withShrinkStrategy(ShrinkStrategies.shrinkInt(range));
    }

    /**
     * An arbitrary that generates {@link Integer}s (0 &lt;= n &lt; bound) that are intended to be used
     * as an index into a collection or sequence.  Output is uniform and unaffected by bias
     * settings (i.e., there will be no emphasis on edge cases).
     */
    public static Arbitrary<Integer> intIndices(int bound) {
        return Arbitrary.arbitrary(generateIntIndex(bound));
    }

    /**
     * An arbitrary that generates {@link Integer}s &gt;= 0.
     */
    public static Arbitrary<Integer> intNaturals() {
        return ints(IntRange.from(0).to(Integer.MAX_VALUE));
    }

    /**
     * An arbitrary that generates {@link Long}s using a custom generator.
     */
    public static Arbitrary<Long> longs(Generator<Long> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkLong());
    }

    /**
     * An arbitrary that generates {@link Long}s.
     */
    public static Arbitrary<Long> longs() {
        return longs(generateLong());
    }

    /**
     * An arbitrary that generates {@link Long}s within a specific range.
     */
    public static Arbitrary<Long> longs(LongRange range) {
        return Arbitrary.arbitrary(generateLong(range)).withShrinkStrategy(ShrinkStrategies.shrinkLong(range));
    }

    /**
     * An arbitrary that generates {@link Long}s (0 &lt;= n &lt; bound) that are intended to be used
     * as an index into a collection or sequence.  Output is uniform and unaffected by bias
     * settings (i.e., there will be no emphasis on edge cases).
     */
    public static Arbitrary<Long> longIndices(long bound) {
        return Arbitrary.arbitrary(generateLongIndex(bound));
    }

    /**
     * An arbitrary that generates {@link Long}s &gt;= 0.
     */
    public static Arbitrary<Long> longNaturals() {
        return longs(LongRange.from(0).to(Long.MAX_VALUE));
    }

    /**
     * An arbitrary that generates {@link Short}s using a custom generator.
     */
    public static Arbitrary<Short> shorts(Generator<Short> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkShort());
    }

    /**
     * An arbitrary that generates {@link Short}s.
     */
    public static Arbitrary<Short> shorts() {
        return shorts(generateShort());
    }

    /**
     * An arbitrary that generates {@link Short}s within a specific range.
     */
    public static Arbitrary<Short> shorts(ShortRange range) {
        return Arbitrary.arbitrary(generateShort(range)).withShrinkStrategy(ShrinkStrategies.shrinkShort(range));
    }

    /**
     * An arbitrary that generates {@link Short}s &gt;= 0.
     */
    public static Arbitrary<Short> shortNaturals() {
        return shorts(ShortRange.from((short) 0).to(Short.MAX_VALUE));
    }

    /**
     * An arbitrary that generates {@link Byte}s using a custom generator.
     */
    public static Arbitrary<Byte> bytes(Generator<Byte> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkByte());
    }

    /**
     * An arbitrary that generates {@link Byte}s.
     */
    public static Arbitrary<Byte> bytes() {
        return bytes(generateByte());
    }

    /**
     * An arbitrary that generates {@link Byte}s within a specific range.
     */
    public static Arbitrary<Byte> bytes(ByteRange range) {
        return Arbitrary.arbitrary(generateByte(range)).withShrinkStrategy(ShrinkStrategies.shrinkByte(range));
    }

    /**
     * An arbitrary that generates {@link Boolean}s using a custom generator.
     */
    public static Arbitrary<Boolean> booleans(Generator<Boolean> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkBoolean());
    }

    /**
     * An arbitrary that generates {@link Boolean}s.
     */
    public static Arbitrary<Boolean> booleans() {
        return booleans(generateBoolean());
    }

    /**
     * An arbitrary that generates {@link Character}s using a custom generator.
     */
    public static Arbitrary<Character> characters(Generator<Character> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkCharacter());
    }

    /**
     * An arbitrary that generates {@link Character}s.
     */
    public static Arbitrary<Character> characters() {
        return characters(generateChar());
    }

    /**
     * An arbitrary that generates {@link Character}s within a specific range.
     */
    public static Arbitrary<Character> characters(CharRange range) {
        return Arbitrary.arbitrary(generateChar(range)).withShrinkStrategy(ShrinkStrategies.shrinkCharacter().filter(range::includes));
    }

    /**
     * An arbitrary that generates {@link Float}s using a custom generator.
     */
    public static Arbitrary<Float> floats(Generator<Float> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkFloat());
    }

    /**
     * An arbitrary that generates {@link Float}s.
     */
    public static Arbitrary<Float> floats() {
        return floats(generateFloat());
    }

    /**
     * An arbitrary that generates {@link Float}s within a specific range.
     */
    public static Arbitrary<Float> floats(FloatRange range) {
        return Arbitrary.arbitrary(generateFloat(range)).withShrinkStrategy(ShrinkStrategies.shrinkFloat(range));
    }

    /**
     * An arbitrary that generates {@link Float}s between 0 (inclusive) and 1 (exclusive).
     */
    public static Arbitrary<Float> floatFractionals() {
        return Arbitrary.arbitrary(generateFloatFractional()).withShrinkStrategy(ShrinkStrategies.shrinkFloat(FloatRange.exclusive(1)));
    }

    /**
     * An arbitrary that generates {@link Double}s using a custom generator.
     */
    public static Arbitrary<Double> doubles(Generator<Double> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkDouble());
    }

    /**
     * An arbitrary that generates {@link Double}s.
     */
    public static Arbitrary<Double> doubles() {
        return doubles(generateDouble());
    }

    /**
     * An arbitrary that generates {@link Double}s within a specific range.
     */
    public static Arbitrary<Double> doubles(DoubleRange range) {
        return Arbitrary.arbitrary(generateDouble(range)).withShrinkStrategy(ShrinkStrategies.shrinkDouble(range));
    }

    /**
     * An arbitrary that generates {@link Double}s between 0 (inclusive) and 1 (exclusive).
     */
    public static Arbitrary<Double> doubleFractionals() {
        return Arbitrary.arbitrary(generateDoubleFractional()).withShrinkStrategy(ShrinkStrategies.shrinkDouble(DoubleRange.exclusive(1)));
    }

    /**
     * An arbitrary that generates byte arrays of varying sizes.
     */
    public static Arbitrary<Byte[]> byteArrays() {
        return Arbitrary.arbitrary(generateByteArray());
    }

    /**
     * An arbitrary that generates byte arrays of a specific size.
     */
    public static Arbitrary<Byte[]> byteArrays(int size) {
        return Arbitrary.arbitrary(generateByteArray(size));
    }

    /**
     * An arbitrary that generates boxed primitives, i.e., {@link Integer}s, {@link Long}s, {@link Short}s, {@link Byte}s,
     * {@link Double}s, {@link Float}s, {@link Boolean}s, or {@link Character}s.
     */
    public static Arbitrary<Object> boxedPrimitives() {
        return Arbitrary.arbitrary(generateBoxedPrimitive());
    }

    /**
     * An arbitrary that generates {@link String}s using a custom generator.
     */
    public static Arbitrary<String> strings(Generator<String> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkString());
    }

    /**
     * An arbitrary that generates {@link String}s.
     */
    public static Arbitrary<String> strings() {
        return strings(generateString());
    }

    /**
     * An arbitrary that generates {@link String}s of a specific length.
     */
    public static Arbitrary<String> stringsOfLength(int length) {
        requireNaturalSize(length);
        return Arbitrary.arbitrary(generateStringFromCharacters(length, Characters.asciiPrintable()))
                .withShrinkStrategy(ShrinkStrategies.shrinkString(length));
    }

    /**
     * An arbitrary that generates {@link String}s with lengths that are within a specific range.
     */
    public static Arbitrary<String> stringsOfLength(IntRange lengthRange) {
        requireNaturalSize(lengthRange);
        return Arbitrary.arbitrary(generateInt(lengthRange)
                        .flatMap(length -> generateStringFromCharacters(length, Characters.asciiPrintable())))
                .withShrinkStrategy(ShrinkStrategies.shrinkString(lengthRange.minInclusive()));
    }

    public static Arbitrary<String> alphaStrings() {
        return Arbitrary.arbitrary(generateAlphaString()).withShrinkStrategy(ShrinkStrategies.shrinkAlphaString(0));
    }

    public static Arbitrary<String> alphaStringsOfLength(int length) {
        return Arbitrary.arbitrary(generateAlphaString(length)).withShrinkStrategy(ShrinkStrategies.shrinkAlphaString(length));
    }

    public static Arbitrary<String> alphaStringsOfLength(IntRange lengthRange) {
        return Arbitrary.arbitrary(generateAlphaString(lengthRange)).withShrinkStrategy(ShrinkStrategies.shrinkAlphaString(lengthRange.minInclusive()));
    }

    public static Arbitrary<String> alphaUpperStrings() {
        return Arbitrary.arbitrary(generateAlphaUpperString()).withShrinkStrategy(ShrinkStrategies.shrinkAlphaUpperString(0));
    }

    public static Arbitrary<String> alphaUpperStringsOfLength(int length) {
        return Arbitrary.arbitrary(generateAlphaUpperString(length)).withShrinkStrategy(ShrinkStrategies.shrinkAlphaUpperString(length));
    }

    public static Arbitrary<String> alphaUpperStringsOfLength(IntRange lengthRange) {
        return Arbitrary.arbitrary(generateAlphaUpperString(lengthRange)).withShrinkStrategy(ShrinkStrategies.shrinkAlphaUpperString(lengthRange.minInclusive()));
    }

    public static Arbitrary<String> alphaLowerStrings() {
        return Arbitrary.arbitrary(generateAlphaLowerString()).withShrinkStrategy(ShrinkStrategies.shrinkAlphaLowerString(0));
    }

    public static Arbitrary<String> alphaLowerStringsOfLength(int length) {
        return Arbitrary.arbitrary(generateAlphaLowerString()).withShrinkStrategy(ShrinkStrategies.shrinkAlphaLowerString(length));
    }

    public static Arbitrary<String> alphaLowerStringsOfLength(IntRange lengthRange) {
        return Arbitrary.arbitrary(generateAlphaLowerString(lengthRange)).withShrinkStrategy(ShrinkStrategies.shrinkAlphaLowerString(lengthRange.minInclusive()));
    }

    public static Arbitrary<String> alphanumericStrings() {
        return Arbitrary.arbitrary(generateAlphanumericString()).withShrinkStrategy(ShrinkStrategies.shrinkAlphanumericString(0));
    }

    public static Arbitrary<String> alphanumericStringsOfLength(int length) {
        return Arbitrary.arbitrary(generateAlphanumericString()).withShrinkStrategy(ShrinkStrategies.shrinkAlphanumericString(length));
    }

    public static Arbitrary<String> alphanumericStringsOfLength(IntRange lengthRange) {
        return Arbitrary.arbitrary(generateAlphanumericString(lengthRange)).withShrinkStrategy(ShrinkStrategies.shrinkAlphanumericString(lengthRange.minInclusive()));
    }

    public static Arbitrary<String> identifiers() {
        return Arbitrary.arbitrary(generateIdentifier()).withShrinkStrategy(shrinkString(ShrinkStrategy.none()));
    }

    public static Arbitrary<String> identifiersOfLength(int length) {
        return Arbitrary.arbitrary(generateIdentifier(length)).withShrinkStrategy(ShrinkStrategies.shrinkString(length, ShrinkStrategy.none()));
    }

    public static Arbitrary<String> identifiersOfLength(IntRange lengthRange) {
        return Arbitrary.arbitrary(generateIdentifier(lengthRange)).withShrinkStrategy(ShrinkStrategies.shrinkString(lengthRange.minInclusive(), ShrinkStrategy.none()));
    }

    public static Arbitrary<Character> alphaCharacters() {
        return Arbitrary.arbitrary(generateAlphaChar()).withShrinkStrategy(ShrinkStrategies.shrinkAlphaCharacter());
    }

    public static Arbitrary<Character> alphaUpperCharacters() {
        return Arbitrary.arbitrary(generateAlphaUpperChar()).withShrinkStrategy(ShrinkStrategies.shrinkAlphaUpperCharacter());
    }

    public static Arbitrary<Character> alphaLowerCharacters() {
        return Arbitrary.arbitrary(generateAlphaLowerChar()).withShrinkStrategy(ShrinkStrategies.shrinkAlphaLowerCharacter());
    }

    public static Arbitrary<Character> alphanumericCharacters() {
        return Arbitrary.arbitrary(generateAlphanumericChar()).withShrinkStrategy(ShrinkStrategies.shrinkAlphanumericCharacter());
    }

    public static Arbitrary<Character> numericCharacters() {
        return Arbitrary.arbitrary(generateNumericChar()).withShrinkStrategy(ShrinkStrategies.shrinkNumericCharacter());
    }

    public static Arbitrary<Character> punctuationCharacters() {
        return characters(generatePunctuationChar());
    }

    public static Arbitrary<Character> asciiPrintableCharacters() {
        return characters(generateAsciiPrintableChar());
    }

    public static Arbitrary<Character> controlCharacters() {
        return Arbitrary.arbitrary(generateControlChar()).withShrinkStrategy(ShrinkStrategies.shrinkCharacter().filter(c -> c < 32));
    }

    /**
     * An arbitrary that generates {@link BigInteger}s using a custom generator.
     */
    public static Arbitrary<BigInteger> bigIntegers(Generator<BigInteger> generator) {
        return Arbitrary.arbitrary(generator).withShrinkStrategy(ShrinkStrategies.shrinkBigInteger());
    }

    /**
     * An arbitrary that generates {@link BigInteger}s.
     */
    public static Arbitrary<BigInteger> bigIntegers() {
        return Arbitrary.arbitrary(generateBigInteger()).withShrinkStrategy(ShrinkStrategies.shrinkBigInteger());
    }

    /**
     * An arbitrary that generates {@link BigInteger}s within a specific range.
     */
    public static Arbitrary<BigInteger> bigIntegers(BigIntegerRange range) {
        return Arbitrary.arbitrary(generateBigInteger(range)).withShrinkStrategy(ShrinkStrategies.shrinkBigInteger(range));
    }

    /**
     * An arbitrary that generates {@link BigDecimal}s using a custom generator.
     */
    public static Arbitrary<BigDecimal> bigDecimals(Generator<BigDecimal> generator) {
        return Arbitrary.arbitrary(generator); // TODO: shrink BigDecimal
    }

    /**
     * An arbitrary that generates {@link BigDecimal}s.
     */
    public static Arbitrary<BigDecimal> bigDecimals() {
        return bigDecimals(generateBigDecimal());
    }

    /**
     * An arbitrary that generates {@link BigDecimal}s within a specific range.
     */
    public static Arbitrary<BigDecimal> bigDecimals(BigDecimalRange range) {
        return bigDecimals(generateBigDecimal(range));
    }

    /**
     * An arbitrary that generates {@link BigDecimal}s within a specific range, and with a specific number of decimal places.
     */
    public static Arbitrary<BigDecimal> bigDecimals(int decimalPlaces, BigDecimalRange range) {
        return bigDecimals(generateBigDecimal(decimalPlaces, range));
    }

    /**
     * An arbitrary that generates {@link BigDecimal}s within a specific range, and with the number of decimal places within a specific range.
     */
    public static Arbitrary<BigDecimal> bigDecimals(IntRange decimalPlaces, BigDecimalRange range) {
        return bigDecimals(generateBigDecimal(generateInt(decimalPlaces), range));
    }

    /**
     * An arbitrary that chooses values from a specific enum.
     */
    public static <A extends Enum<A>> Arbitrary<A> valuesOfEnumClass(Class<A> enumType) {
        return Arbitrary.arbitrary(generateFromEnum(enumType));
    }

    /**
     * An arbitrary that generates {@code Tuple2}s, formed by combining two component arbitraries.
     */
    public static <A, B> Arbitrary<Tuple2<A, B>> tuplesOf(Arbitrary<A> a,
                                                          Arbitrary<B> b) {
        return CompositeArbitraries.combine(a, b);
    }

    /**
     * An arbitrary that generates {@code Tuple3}s, formed by combining three component arbitraries.
     */
    public static <A, B, C> Arbitrary<Tuple3<A, B, C>> tuplesOf(Arbitrary<A> a,
                                                                Arbitrary<B> b,
                                                                Arbitrary<C> c) {
        return CompositeArbitraries.combine(a, b, c);
    }

    /**
     * An arbitrary that generates {@code Tuple4}s, formed by combining four component arbitraries.
     */
    public static <A, B, C, D> Arbitrary<Tuple4<A, B, C, D>> tuplesOf(Arbitrary<A> a,
                                                                      Arbitrary<B> b,
                                                                      Arbitrary<C> c,
                                                                      Arbitrary<D> d) {
        return CompositeArbitraries.combine(a, b, c, d);
    }

    /**
     * An arbitrary that generates {@code Tuple5}s, formed by combining five component arbitraries.
     */
    public static <A, B, C, D, E> Arbitrary<Tuple5<A, B, C, D, E>> tuplesOf(Arbitrary<A> a,
                                                                            Arbitrary<B> b,
                                                                            Arbitrary<C> c,
                                                                            Arbitrary<D> d,
                                                                            Arbitrary<E> e) {
        return CompositeArbitraries.combine(a, b, c, d, e);
    }

    /**
     * An arbitrary that generates {@code Tuple6}s, formed by combining six component arbitraries.
     */
    public static <A, B, C, D, E, F> Arbitrary<Tuple6<A, B, C, D, E, F>> tuplesOf(Arbitrary<A> a,
                                                                                  Arbitrary<B> b,
                                                                                  Arbitrary<C> c,
                                                                                  Arbitrary<D> d,
                                                                                  Arbitrary<E> e,
                                                                                  Arbitrary<F> f) {
        return CompositeArbitraries.combine(a, b, c, d, e, f);
    }

    /**
     * An arbitrary that generates {@code Tuple7}s, formed by combining seven component arbitraries.
     */
    public static <A, B, C, D, E, F, G> Arbitrary<Tuple7<A, B, C, D, E, F, G>> tuplesOf(Arbitrary<A> a,
                                                                                        Arbitrary<B> b,
                                                                                        Arbitrary<C> c,
                                                                                        Arbitrary<D> d,
                                                                                        Arbitrary<E> e,
                                                                                        Arbitrary<F> f,
                                                                                        Arbitrary<G> g) {
        return CompositeArbitraries.combine(a, b, c, d, e, f, g);
    }

    /**
     * An arbitrary that generates {@code Tuple8}s, formed by combining eight component arbitraries.
     */
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

    /**
     * An arbitrary that generates {@code Tuple2}s of arbitrary component types.
     */
    public static Arbitrary<Tuple2<?, ?>> tuple2s() {
        return Arbitrary.higherOrderArbitrary(generateArbitrary().pair(), x -> tuplesOf(x._1(), x._2()));
    }

    /**
     * An arbitrary that generates {@code Tuple3}s of arbitrary component types.
     */
    public static Arbitrary<Tuple3<?, ?, ?>> tuple3s() {
        return Arbitrary.higherOrderArbitrary(generateArbitrary().triple(), x -> tuplesOf(x._1(), x._2(), x._3()));
    }

    /**
     * An arbitrary that generates {@code Tuple4}s of arbitrary component types.
     */
    public static Arbitrary<Tuple4<?, ?, ?, ?>> tuple4s() {
        return Arbitrary.higherOrderArbitrary(generateTuple(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary()), x -> tuplesOf(x._1(), x._2(), x._3(), x._4()));
    }

    /**
     * An arbitrary that generates {@code Tuple5}s of arbitrary component types.
     */
    public static Arbitrary<Tuple5<?, ?, ?, ?, ?>> tuple5s() {
        return Arbitrary.higherOrderArbitrary(generateTuple(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary(), generateArbitrary()), x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5()));
    }

    /**
     * An arbitrary that generates {@code Tuple6}s of arbitrary component types.
     */
    public static Arbitrary<Tuple6<?, ?, ?, ?, ?, ?>> tuple6s() {
        return Arbitrary.higherOrderArbitrary(generateTuple(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                generateArbitrary(), generateArbitrary(), generateArbitrary()), x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5(), x._6()));
    }

    /**
     * An arbitrary that generates {@code Tuple7}s of arbitrary component types.
     */
    public static Arbitrary<Tuple7<?, ?, ?, ?, ?, ?, ?>> tuple7s() {
        return Arbitrary.higherOrderArbitrary(generateTuple(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                        generateArbitrary(), generateArbitrary(), generateArbitrary(), generateArbitrary()),
                x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5(), x._6(), x._7()));
    }

    /**
     * An arbitrary that generates {@code Tuple8}s of arbitrary component types.
     */
    public static Arbitrary<Tuple8<?, ?, ?, ?, ?, ?, ?, ?>> tuple8s() {
        return Arbitrary.higherOrderArbitrary(generateTuple(generateArbitrary(), generateArbitrary(), generateArbitrary(),
                        generateArbitrary(), generateArbitrary(), generateArbitrary(), generateArbitrary(), generateArbitrary()),
                x -> tuplesOf(x._1(), x._2(), x._3(), x._4(), x._5(), x._6(), x._7(), x._8()));
    }

    /**
     * An arbitrary that only generates a single value, {@code Unit.UNIT}.
     */
    public static Arbitrary<Unit> unit() {
        return CoProductArbitraries.arbitraryUnit();
    }

    /**
     * An arbitrary that generates {@code Choice2}s, formed by randomly selecting and drawing from two component arbitraries
     * with custom probabilities for each.
     */
    public static <A, B> Arbitrary<Choice2<A, B>> choicesOf(Weighted<Arbitrary<A>> a,
                                                            Weighted<Arbitrary<B>> b) {
        return CoProductArbitraries.arbitraryChoice2(a, b);
    }

    /**
     * An arbitrary that generates {@code Choice2}s, formed by randomly selecting and drawing from two component arbitraries,
     * with equal probability for each.
     */
    public static <A, B> Arbitrary<Choice2<A, B>> choicesOf(Arbitrary<A> a,
                                                            Arbitrary<B> b) {
        return CoProductArbitraries.arbitraryChoice2(a, b);
    }

    /**
     * An arbitrary that generates {@code Choice3}s, formed by randomly selecting and drawing from three component arbitraries
     * with custom probabilities for each.
     */
    public static <A, B, C> Arbitrary<Choice3<A, B, C>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                  Weighted<Arbitrary<B>> b,
                                                                  Weighted<Arbitrary<C>> c) {
        return CoProductArbitraries.arbitraryChoice3(a, b, c);
    }

    /**
     * An arbitrary that generates {@code Choice3}s, formed by randomly selecting and drawing from three component arbitraries,
     * with equal probability for each.
     */
    public static <A, B, C> Arbitrary<Choice3<A, B, C>> choicesOf(Arbitrary<A> a,
                                                                  Arbitrary<B> b,
                                                                  Arbitrary<C> c) {
        return CoProductArbitraries.arbitraryChoice3(a, b, c);
    }

    /**
     * An arbitrary that generates {@code Choice4}s, formed by randomly selecting and drawing from four component arbitraries
     * with custom probabilities for each.
     */
    public static <A, B, C, D> Arbitrary<Choice4<A, B, C, D>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                        Weighted<Arbitrary<B>> b,
                                                                        Weighted<Arbitrary<C>> c,
                                                                        Weighted<Arbitrary<D>> d) {
        return CoProductArbitraries.arbitraryChoice4(a, b, c, d);
    }

    /**
     * An arbitrary that generates {@code Choice4}s, formed by randomly selecting and drawing from four component arbitraries,
     * with equal probability for each.
     */
    public static <A, B, C, D> Arbitrary<Choice4<A, B, C, D>> choicesOf(Arbitrary<A> a,
                                                                        Arbitrary<B> b,
                                                                        Arbitrary<C> c,
                                                                        Arbitrary<D> d) {
        return CoProductArbitraries.arbitraryChoice4(a, b, c, d);
    }

    /**
     * An arbitrary that generates {@code Choice5}s, formed by randomly selecting and drawing from five component arbitraries
     * with custom probabilities for each.
     */
    public static <A, B, C, D, E> Arbitrary<Choice5<A, B, C, D, E>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                              Weighted<Arbitrary<B>> b,
                                                                              Weighted<Arbitrary<C>> c,
                                                                              Weighted<Arbitrary<D>> d,
                                                                              Weighted<Arbitrary<E>> e) {
        return CoProductArbitraries.arbitraryChoice5(a, b, c, d, e);
    }

    /**
     * An arbitrary that generates {@code Choice5}s, formed by randomly selecting and drawing from five component arbitraries,
     * with equal probability for each.
     */
    public static <A, B, C, D, E> Arbitrary<Choice5<A, B, C, D, E>> choicesOf(Arbitrary<A> a,
                                                                              Arbitrary<B> b,
                                                                              Arbitrary<C> c,
                                                                              Arbitrary<D> d,
                                                                              Arbitrary<E> e) {
        return CoProductArbitraries.arbitraryChoice5(a, b, c, d, e);
    }

    /**
     * An arbitrary that generates {@code Choice6}s, formed by randomly selecting and drawing from six component arbitraries
     * with custom probabilities for each.
     */
    public static <A, B, C, D, E, F> Arbitrary<Choice6<A, B, C, D, E, F>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                                    Weighted<Arbitrary<B>> b,
                                                                                    Weighted<Arbitrary<C>> c,
                                                                                    Weighted<Arbitrary<D>> d,
                                                                                    Weighted<Arbitrary<E>> e,
                                                                                    Weighted<Arbitrary<F>> f) {
        return CoProductArbitraries.arbitraryChoice6(a, b, c, d, e, f);
    }

    /**
     * An arbitrary that generates {@code Choice6}s, formed by randomly selecting and drawing from six component arbitraries,
     * with equal probability for each.
     */
    public static <A, B, C, D, E, F> Arbitrary<Choice6<A, B, C, D, E, F>> choicesOf(Arbitrary<A> a,
                                                                                    Arbitrary<B> b,
                                                                                    Arbitrary<C> c,
                                                                                    Arbitrary<D> d,
                                                                                    Arbitrary<E> e,
                                                                                    Arbitrary<F> f) {
        return CoProductArbitraries.arbitraryChoice6(a, b, c, d, e, f);
    }

    /**
     * An arbitrary that generates {@code Choice7}s, formed by randomly selecting and drawing from seven component arbitraries
     * with custom probabilities for each.
     */
    public static <A, B, C, D, E, F, G> Arbitrary<Choice7<A, B, C, D, E, F, G>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                                          Weighted<Arbitrary<B>> b,
                                                                                          Weighted<Arbitrary<C>> c,
                                                                                          Weighted<Arbitrary<D>> d,
                                                                                          Weighted<Arbitrary<E>> e,
                                                                                          Weighted<Arbitrary<F>> f,
                                                                                          Weighted<Arbitrary<G>> g) {
        return CoProductArbitraries.arbitraryChoice7(a, b, c, d, e, f, g);
    }

    /**
     * An arbitrary that generates {@code Choice7}s, formed by randomly selecting and drawing from seven component arbitraries,
     * with equal probability for each.
     */
    public static <A, B, C, D, E, F, G> Arbitrary<Choice7<A, B, C, D, E, F, G>> choicesOf(Arbitrary<A> a,
                                                                                          Arbitrary<B> b,
                                                                                          Arbitrary<C> c,
                                                                                          Arbitrary<D> d,
                                                                                          Arbitrary<E> e,
                                                                                          Arbitrary<F> f,
                                                                                          Arbitrary<G> g) {
        return CoProductArbitraries.arbitraryChoice7(a, b, c, d, e, f, g);
    }

    /**
     * An arbitrary that generates {@code Choice8}s, formed by randomly selecting and drawing from eight component arbitraries
     * with custom probabilities for each.
     */
    public static <A, B, C, D, E, F, G, H> Arbitrary<Choice8<A, B, C, D, E, F, G, H>> choicesOf(Weighted<Arbitrary<A>> a,
                                                                                                Weighted<Arbitrary<B>> b,
                                                                                                Weighted<Arbitrary<C>> c,
                                                                                                Weighted<Arbitrary<D>> d,
                                                                                                Weighted<Arbitrary<E>> e,
                                                                                                Weighted<Arbitrary<F>> f,
                                                                                                Weighted<Arbitrary<G>> g,
                                                                                                Weighted<Arbitrary<H>> h) {
        return CoProductArbitraries.arbitraryChoice8(a, b, c, d, e, f, g, h);
    }

    /**
     * An arbitrary that generates {@code Choice8}s, formed by randomly selecting and drawing from eight component arbitraries,
     * with equal probability for each.
     */
    public static <A, B, C, D, E, F, G, H> Arbitrary<Choice8<A, B, C, D, E, F, G, H>> choicesOf(Arbitrary<A> a,
                                                                                                Arbitrary<B> b,
                                                                                                Arbitrary<C> c,
                                                                                                Arbitrary<D> d,
                                                                                                Arbitrary<E> e,
                                                                                                Arbitrary<F> f,
                                                                                                Arbitrary<G> g,
                                                                                                Arbitrary<H> h) {
        return CoProductArbitraries.arbitraryChoice8(a, b, c, d, e, f, g, h);
    }

    /**
     * Converts an arbitrary that generates {@code A} to an arbitrary that generates {@code Maybe<A>},
     * with custom probabilities for {@code just} vs. {@code nothing}.
     */
    public static <A> Arbitrary<Maybe<A>> maybesOf(MaybeWeights weights,
                                                   Arbitrary<A> a) {
        return CoProductArbitraries.arbitraryMaybe(weights, a);
    }

    /**
     * Converts an arbitrary that generates {@code A} to an arbitrary that generates {@code Maybe<A>}.
     * Most frequently will yield a {@code just}, but will occasionally yield a {@code nothing}.
     */
    public static <A> Arbitrary<Maybe<A>> maybesOf(Arbitrary<A> a) {
        return CoProductArbitraries.arbitraryMaybe(a);
    }

    /**
     * An arbitrary that generates {@code Either}s, formed by randomly selecting and drawing from two component arbitraries, with
     * custom probabilities for each.
     */
    public static <L, R> Arbitrary<Either<L, R>> eithersOf(Weighted<Arbitrary<L>> left,
                                                           Weighted<Arbitrary<R>> right) {
        return CoProductArbitraries.arbitraryEither(left, right);
    }

    /**
     * An arbitrary that generates {@code Either}s, formed by randomly selecting and drawing from two component arbitraries, with
     * equal probabilities for each.
     */
    public static <L, R> Arbitrary<Either<L, R>> eithersOf(Arbitrary<L> left,
                                                           Arbitrary<R> right) {
        return CoProductArbitraries.arbitraryEither(left, right);
    }

    /**
     * An arbitrary that generates {@code Either}s, formed by randomly selecting and drawing from two component arbitraries, with
     * custom probabilities for each.
     */
    public static <L, R> Arbitrary<Either<L, R>> eithersOf(EitherWeights weights,
                                                           Arbitrary<L> left,
                                                           Arbitrary<R> right) {
        return CoProductArbitraries.arbitraryEither(weights, left, right);
    }

    /**
     * An arbitrary that generates {@code These}s, formed by randomly selecting and drawing from two component arbitraries, with
     * equal probabilities for {@code a}, {@code b}, and {@code both}.
     */
    public static <A, B> Arbitrary<These<A, B>> theseOf(Arbitrary<A> a,
                                                        Arbitrary<B> b) {
        return CoProductArbitraries.arbitraryThese(a, b);
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
        ShrinkStrategy<Vector<?>> shrinkStrategy = (ShrinkStrategy<Vector<?>>) (ShrinkStrategy<? extends Vector<?>>) ShrinkStrategies.shrinkVector(ShrinkStrategy.none());
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

    // TODO: vectorsOf size range

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

    // TODO: hashSets()

    public static <A> Arbitrary<HashSet<A>> hashSetsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.hashSet(elements);
    }

    public static <A> Arbitrary<HashSet<A>> nonEmptyHashSetsOf(Arbitrary<A> elements) {
        return CollectionArbitraries.nonEmptyHashSet(elements);
    }

    @SuppressWarnings("unchecked")
    public static Arbitrary<HashMap<?, ?>> hashMaps() {
        ShrinkStrategy<HashMap<?, ?>> shrinkStrategy = (ShrinkStrategy<HashMap<?, ?>>) (ShrinkStrategy<? extends HashMap<?, ?>>) ShrinkStrategies.shrinkHashMap(ShrinkStrategy.none());
        return Arbitrary.<Arbitrary<?>, HashMap<?, ?>>higherOrderArbitrary(generateArbitrary(), keys ->
                        Arbitrary.higherOrderArbitrary(generateArbitrary(), values -> hashMapsOf(keys, values)))
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
        return Arbitrary.arbitrary(generateLocalDate());
    }

    public static <A> Arbitrary<LocalDate> localDates(LocalDateRange range) {
        return Arbitrary.arbitrary(generateLocalDate(range));
    }

    public static <A> Arbitrary<LocalDate> localDatesForYear(Year year) {
        return Arbitrary.arbitrary(generateLocalDateForYear(year));
    }

    public static <A> Arbitrary<LocalDate> localDatesForMonth(YearMonth month) {
        return Arbitrary.arbitrary(generateLocalDateForMonth(month));
    }

    public static <A> Arbitrary<LocalTime> localTimes() {
        return Arbitrary.arbitrary(generateLocalTime());
    }

    public static <A> Arbitrary<LocalTime> localTimes(LocalTimeRange range) {
        return Arbitrary.arbitrary(generateLocalTime(range));
    }

    public static <A> Arbitrary<LocalDateTime> localDateTimes() {
        return Arbitrary.arbitrary(generateLocalDateTime());
    }

    public static <A> Arbitrary<LocalDateTime> localDateTimes(LocalDateRange range) {
        return Arbitrary.arbitrary(generateLocalDateTime(range));
    }

    public static <A> Arbitrary<LocalDateTime> localDateTimes(LocalDateTimeRange range) {
        return Arbitrary.arbitrary(generateLocalDateTime(range));
    }

    public static <A> Arbitrary<Duration> durations() {
        return Arbitrary.arbitrary(generateDuration());
    }

    public static <A> Arbitrary<Duration> durations(DurationRange range) {
        return Arbitrary.arbitrary(generateDuration(range));
    }

    public static Arbitrary<DayOfWeek> daysOfWeek() {
        return Arbitrary.arbitrary(generateDayOfWeek());
    }

    public static Arbitrary<Month> months() {
        return Arbitrary.arbitrary(generateMonth());
    }

    public static Arbitrary<UUID> uuids() {
        return Arbitrary.arbitrary(generateUUID());
    }

    public static Arbitrary<IntRange> intRanges() {
        return Arbitrary.arbitrary(generateIntRange());
    }

    public static Arbitrary<IntRange> intRanges(IntRange parentRange) {
        return Arbitrary.arbitrary(generateIntRange(parentRange));
    }

    public static Arbitrary<LongRange> longRanges() {
        return Arbitrary.arbitrary(generateLongRange());
    }

    public static Arbitrary<LongRange> longRanges(LongRange parentRange) {
        return Arbitrary.arbitrary(generateLongRange(parentRange));
    }

    public static Arbitrary<ShortRange> shortRanges() {
        return Arbitrary.arbitrary(generateShortRange());
    }

    public static Arbitrary<ShortRange> shortRanges(ShortRange parentRange) {
        return Arbitrary.arbitrary(generateShortRange(parentRange));
    }

    public static Arbitrary<ByteRange> byteRanges() {
        return Arbitrary.arbitrary(generateByteRange());
    }

    public static Arbitrary<ByteRange> byteRanges(ByteRange parentRange) {
        return Arbitrary.arbitrary(generateByteRange(parentRange));
    }

    public static Arbitrary<DoubleRange> doubleRanges() {
        return Arbitrary.arbitrary(generateDoubleRange());
    }

    public static Arbitrary<DoubleRange> doubleRanges(DoubleRange parentRange) {
        return Arbitrary.arbitrary(generateDoubleRange(parentRange));
    }

    public static Arbitrary<FloatRange> floatRanges() {
        return Arbitrary.arbitrary(generateFloatRange());
    }

    public static Arbitrary<FloatRange> floatRanges(FloatRange parentRange) {
        return Arbitrary.arbitrary(generateFloatRange(parentRange));
    }

    public static Arbitrary<BigIntegerRange> bigIntegerRanges() {
        return Arbitrary.arbitrary(generateBigIntegerRange());
    }

    public static Arbitrary<BigIntegerRange> bigIntegerRanges(BigIntegerRange parentRange) {
        return Arbitrary.arbitrary(generateBigIntegerRange(parentRange));
    }

    public static Arbitrary<LocalDateRange> localDateRanges() {
        return Arbitrary.arbitrary(generateLocalDateRange());
    }

    public static Arbitrary<LocalDateRange> localDateRanges(LocalDateRange parentRange) {
        return Arbitrary.arbitrary(generateLocalDateRange(parentRange));
    }

    public static Arbitrary<LocalTimeRange> localTimeRanges() {
        return Arbitrary.arbitrary(generateLocalTimeRange());
    }

    public static Arbitrary<LocalTimeRange> localTimeRanges(LocalTimeRange parentRange) {
        return Arbitrary.arbitrary(generateLocalTimeRange(parentRange));
    }

    public static Arbitrary<LocalDateTimeRange> localDateTimeRanges() {
        return Arbitrary.arbitrary(generateLocalDateTimeRange());
    }

    public static Arbitrary<LocalDateTimeRange> localDateTimeRanges(LocalDateRange parentRange) {
        return Arbitrary.arbitrary(generateLocalDateTimeRange(parentRange));
    }

    public static Arbitrary<LocalDateTimeRange> localDateTimeRanges(LocalDateTimeRange parentRange) {
        return Arbitrary.arbitrary(generateLocalDateTimeRange(parentRange));
    }

    public static Arbitrary<DurationRange> durationRanges() {
        return Arbitrary.arbitrary(generateDurationRange());
    }

    public static Arbitrary<DurationRange> durationRanges(DurationRange parentRange) {
        return Arbitrary.arbitrary(generateDurationRange(parentRange));
    }

    public static <A> Arbitrary<Vector<A>> shufflesOf(Vector<A> elements) {
        return Arbitrary.arbitrary(generateShuffled(elements));
    }

    public static Arbitrary<Vector<Integer>> shufflesOfIndices(int count) {
        return Arbitrary.arbitrary(generateShuffled(Vector.range(count)));
    }

    public static <A> Arbitrary<NonEmptyVector<A>> nonEmptyShufflesOf(NonEmptyVector<A> elements) {
        return Arbitrary.arbitrary(generateShuffled(elements));
    }

    public static Arbitrary<Seed> seeds() {
        return Arbitrary.arbitrary(generateSeed());
    }
}
