package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;
import dev.marksman.kraftwerk.constraints.IntRange;

public final class ShrinkNumerics {

    private ShrinkNumerics() {

    }

    private static final Shrink<Integer> INT = input -> {
        if (input < 0) {
            int high = input == Integer.MIN_VALUE ? Integer.MAX_VALUE : -input;
            return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
        } else {
            return series(0, input);
        }
    };

    private static final Shrink<Long> LONG = input -> {
        if (input < 0) {
            long high = input == Long.MIN_VALUE ? Long.MAX_VALUE : -input;
            return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
        } else {
            return series(0, input);
        }
    };

    private static final Shrink<Short> SHORT = input -> {
        if (input < 0) {
            short high = input == Short.MIN_VALUE ? Short.MAX_VALUE : (short) (-input);
            return ShrinkResult.cons(high, () -> series((short) 0, high).fmap(n -> (short) -n));
        } else {
            return series((short) 0, input);
        }
    };

    private static final Shrink<Byte> BYTE = input -> {
        if (input < 0) {
            byte high = input == Byte.MIN_VALUE ? Byte.MAX_VALUE : (byte) (-input);
            return ShrinkResult.cons(high, () -> series((byte) 0, high).fmap(n -> (byte) -n));
        } else {
            return series((byte) 0, input);
        }
    };

    /**
     * Returns a shrinking strategy that shrinks integers.
     */
    public static Shrink<Integer> shrinkInt() {
        return INT;
    }

    /**
     * Returns a shrinking strategy that shrinks longs.
     */
    public static Shrink<Long> shrinkLong() {
        return LONG;
    }

    /**
     * Returns a shrinking strategy that shrinks shorts.
     */
    public static Shrink<Short> shrinkShort() {
        return SHORT;
    }

    /**
     * Returns a shrinking strategy that shrinks bytes.
     */
    public static Shrink<Byte> shrinkByte() {
        return BYTE;
    }

    /**
     * Returns a shrinking strategy that shrinks integers, but limits values in the output to a given range.
     */
    public static Shrink<Integer> shrinkInt(IntRange range) {
        int min = range.minInclusive();
        int max = range.maxInclusive();
        if (min >= max) {
            return Shrink.none();
        } else if (min < 0 && max < 0) {
            // all negative
            return clamped(range, input -> series(-max, -input).fmap(n -> -n));
        } else if (min < 0) {
            // negative and positive
            return clamped(range, input -> {
                if (input < 0) {
                    int high = Math.min(-input, max);
                    return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
                } else {
                    return series(0, input);
                }
            });
        } else {
            // non-negative
            return clamped(range, input -> series(min, input));
        }
    }

    private static Shrink<Integer> clamped(IntRange range, Fn1<Integer, ImmutableFiniteIterable<Integer>> f) {
        return input -> range.includes(input)
                ? f.apply(input)
                : ShrinkResult.empty();
    }

    private static ImmutableFiniteIterable<Integer> series(int low, int high) {
        if (low >= high) {
            return ShrinkResult.empty();
        } else if (low == high - 1) {
            return ShrinkResult.singleton(low);
        } else {
            int middle = low + ((high - low) / 2);
            return ShrinkResult.cons(low, () -> series(middle, high));
        }
    }

    private static ImmutableFiniteIterable<Long> series(long low, long high) {
        if (low >= high) {
            return ShrinkResult.empty();
        } else if (low == high - 1) {
            return ShrinkResult.singleton(low);
        } else {
            long middle = low + ((high - low) / 2);
            return ShrinkResult.cons(low, () -> series(middle, high));
        }
    }

    private static ImmutableFiniteIterable<Short> series(short low, short high) {
        if (low >= high) {
            return ShrinkResult.empty();
        } else if (low == high - 1) {
            return ShrinkResult.singleton(low);
        } else {
            int middle = low + ((high - low) / 2);
            return ShrinkResult.cons(low, () -> series((short) middle, high));
        }
    }

    private static ImmutableFiniteIterable<Byte> series(byte low, byte high) {
        if (low >= high) {
            return ShrinkResult.empty();
        } else if (low == high - 1) {
            return ShrinkResult.singleton(low);
        } else {
            int middle = low + ((high - low) / 2);
            return ShrinkResult.cons(low, () -> series((byte) middle, high));
        }
    }

}
