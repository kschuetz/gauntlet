package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkByte;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkInt;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkLong;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkShort;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateShort;

public final class Arbitraries {

    private Arbitraries() {

    }

    public static Arbitrary<Integer> arbitraryInt(Generator<Integer> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkInt());
    }

    public static Arbitrary<Integer> arbitraryInt() {
        return arbitraryInt(generateInt());
    }

    public static Arbitrary<Integer> arbitraryInt(IntRange range) {
        return arbitrary(generateInt(range)).withShrinkStrategy(shrinkInt(range));
    }

    public static Arbitrary<Integer> arbitraryInt(FrequencyMap<Integer> frequencyMap) {
        return arbitraryInt(frequencyMap.toGenerator());
    }

    public static Arbitrary<Long> arbitraryLong(Generator<Long> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkLong());
    }

    public static Arbitrary<Long> arbitraryLong() {
        return arbitraryLong(generateLong());
    }

    public static Arbitrary<Long> arbitraryLong(LongRange range) {
        return arbitrary(generateLong(range)).withShrinkStrategy(shrinkLong(range));
    }

    public static Arbitrary<Long> arbitraryLong(FrequencyMap<Long> frequencyMap) {
        return arbitraryLong(frequencyMap.toGenerator());
    }

    public static Arbitrary<Short> arbitraryShort(Generator<Short> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkShort());
    }

    public static Arbitrary<Short> arbitraryShort() {
        return arbitraryShort(generateShort());
    }

    public static Arbitrary<Short> arbitraryShort(ShortRange range) {
        return arbitrary(generateShort(range)).withShrinkStrategy(shrinkShort(range));
    }

    public static Arbitrary<Short> arbitraryShort(FrequencyMap<Short> frequencyMap) {
        return arbitraryShort(frequencyMap.toGenerator());
    }

    public static Arbitrary<Byte> arbitraryByte(Generator<Byte> generator) {
        return arbitrary(generator).withShrinkStrategy(shrinkByte());
    }

    public static Arbitrary<Byte> arbitraryByte() {
        return arbitraryByte(generateByte());
    }

    public static Arbitrary<Byte> arbitraryByte(ByteRange range) {
        return arbitrary(generateByte(range)).withShrinkStrategy(shrinkByte(range));
    }

    public static Arbitrary<Byte> arbitraryByte(FrequencyMap<Byte> frequencyMap) {
        return arbitraryByte(frequencyMap.toGenerator());
    }

    public static <A, B> Arbitrary<Tuple2<A, B>> combine(Arbitrary<A> a,
                                                         Arbitrary<B> b) {
        return CompositeArbitraries.combine(a, b);
    }

    public static <A, B, C> Arbitrary<Tuple3<A, B, C>> combine(Arbitrary<A> a,
                                                               Arbitrary<B> b,
                                                               Arbitrary<C> c) {
        return CompositeArbitraries.combine(a, b, c);
    }

    public static <A, B, C, D> Arbitrary<Tuple4<A, B, C, D>> combine(Arbitrary<A> a,
                                                                     Arbitrary<B> b,
                                                                     Arbitrary<C> c,
                                                                     Arbitrary<D> d) {
        return CompositeArbitraries.combine(a, b, c, d);
    }

    public static <A, B, C, D, E> Arbitrary<Tuple5<A, B, C, D, E>> combine(Arbitrary<A> a,
                                                                           Arbitrary<B> b,
                                                                           Arbitrary<C> c,
                                                                           Arbitrary<D> d,
                                                                           Arbitrary<E> e) {
        return CompositeArbitraries.combine(a, b, c, d, e);
    }

}
