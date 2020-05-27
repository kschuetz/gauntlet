package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResult;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;

final class ShrinkNumerics {
    private static final ShrinkStrategy<Integer> INT = input -> {
        if (input < 0) {
            int high = input == Integer.MIN_VALUE ? Integer.MAX_VALUE : -input;
            return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
        } else {
            return series(0, input);
        }
    };
    private static final ShrinkStrategy<Long> LONG = input -> {
        if (input < 0) {
            long high = input == Long.MIN_VALUE ? Long.MAX_VALUE : -input;
            return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
        } else {
            return series(0, input);
        }
    };
    private static final ShrinkStrategy<Short> SHORT = input -> {
        if (input < 0) {
            short high = input == Short.MIN_VALUE ? Short.MAX_VALUE : (short) (-input);
            return ShrinkResult.cons(high, () -> series((short) 0, high).fmap(n -> (short) -n));
        } else {
            return series((short) 0, input);
        }
    };
    private static final ShrinkStrategy<Byte> BYTE = input -> {
        if (input < 0) {
            byte high = input == Byte.MIN_VALUE ? Byte.MAX_VALUE : (byte) (-input);
            return ShrinkResult.cons(high, () -> series((byte) 0, high).fmap(n -> (byte) -n));
        } else {
            return series((byte) 0, input);
        }
    };

    private ShrinkNumerics() {
    }

    /**
     * Returns a shrinking strategy that shrinks integers.
     */
    static ShrinkStrategy<Integer> shrinkInt() {
        return INT;
    }

    /**
     * Returns a shrinking strategy that shrinks longs.
     */
    static ShrinkStrategy<Long> shrinkLong() {
        return LONG;
    }

    /**
     * Returns a shrinking strategy that shrinks shorts.
     */
    static ShrinkStrategy<Short> shrinkShort() {
        return SHORT;
    }

    /**
     * Returns a shrinking strategy that shrinks bytes.
     */
    static ShrinkStrategy<Byte> shrinkByte() {
        return BYTE;
    }

    static ShrinkStrategy<Float> shrinkFloat() {
        return shrinkFloat(FloatRange.inclusive(-Float.MAX_VALUE, Float.MAX_VALUE));
    }

    static ShrinkStrategy<Double> shrinkDouble() {
        return shrinkDouble(DoubleRange.inclusive(-Double.MAX_VALUE, Double.MAX_VALUE));
    }

    /**
     * Returns a shrinking strategy that shrinks integers, but limits values in the output to a given range.
     */
    static ShrinkStrategy<Integer> shrinkInt(IntRange range) {
        int min = range.minInclusive();
        int max = range.maxInclusive();
        if (min >= max) {
            return ShrinkStrategy.none();
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

    /**
     * Returns a shrinking strategy that shrinks longs, but limits values in the output to a given range.
     */
    static ShrinkStrategy<Long> shrinkLong(LongRange range) {
        long min = range.minInclusive();
        long max = range.maxInclusive();
        if (min >= max) {
            return ShrinkStrategy.none();
        } else if (min < 0 && max < 0) {
            // all negative
            return clamped(range, input -> series(-max, -input).fmap(n -> -n));
        } else if (min < 0) {
            // negative and positive
            return clamped(range, input -> {
                if (input < 0) {
                    long high = Math.min(-input, max);
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

    /**
     * Returns a shrinking strategy that shrinks shorts, but limits values in the output to a given range.
     */
    static ShrinkStrategy<Short> shrinkShort(ShortRange range) {
        short min = range.minInclusive();
        short max = range.maxInclusive();
        if (min >= max) {
            return ShrinkStrategy.none();
        } else if (min < 0 && max < 0) {
            // all negative
            return clamped(range, input -> series(-max, -input).fmap(n -> (short) (-n)));
        } else if (min < 0) {
            // negative and positive
            return clamped(range, input -> {
                if (input < 0) {
                    short high = (short) Math.min(-input, max);
                    return ShrinkResult.cons(high, () -> series((short) 0, high).fmap(n -> (short) (-n)));
                } else {
                    return series((short) 0, input);
                }
            });
        } else {
            // non-negative
            return clamped(range, input -> series(min, input));
        }
    }

    /**
     * Returns a shrinking strategy that shrinks bytes, but limits values in the output to a given range.
     */
    static ShrinkStrategy<Byte> shrinkByte(ByteRange range) {
        byte min = range.minInclusive();
        byte max = range.maxInclusive();
        if (min >= max) {
            return ShrinkStrategy.none();
        } else if (min < 0 && max < 0) {
            // all negative
            return clamped(range, input -> series(-max, -input).fmap(n -> (byte) (-n)));
        } else if (min < 0) {
            // negative and positive
            return clamped(range, input -> {
                if (input < 0) {
                    byte high = (byte) Math.min(-input, max);
                    return ShrinkResult.cons(high, () -> series((byte) 0, high).fmap(n -> (byte) (-n)));
                } else {
                    return series((byte) 0, input);
                }
            });
        } else {
            // non-negative
            return clamped(range, input -> series(min, input));
        }
    }

    static ShrinkStrategy<Float> shrinkFloat(FloatRange range) {
        float min = range.minInclusive();
        float max = range.maxInclusive();
        if (min >= max) {
            return ShrinkStrategy.none();
        } else if (min < 0 && max < 0) {
            // all negative
            return clamped(range, input -> series(-max, -input).fmap(n -> -n));
        } else if (min < 0) {
            // negative and positive
            return clamped(range, input -> {
                if (input < 0) {
                    float high = Math.min(-input, max);
                    return ShrinkResult.cons(high, () -> series(0f, high).fmap(n -> -n));
                } else {
                    return series(0f, input);
                }
            });
        } else {
            // non-negative
            return clamped(range, input -> series(min, input));
        }
    }

    static ShrinkStrategy<Double> shrinkDouble(DoubleRange range) {
        double min = range.minInclusive();
        double max = range.maxInclusive();
        if (min >= max) {
            return ShrinkStrategy.none();
        } else if (min < 0 && max < 0) {
            // all negative
            return clamped(range, input -> series(-max, -input).fmap(n -> -n));
        } else if (min < 0) {
            // negative and positive
            return clamped(range, input -> {
                if (input < 0) {
                    double high = Math.min(-input, max);
                    return ShrinkResult.cons(high, () -> series(0d, high).fmap(n -> -n));
                } else {
                    return series(0d, input);
                }
            });
        } else {
            // non-negative
            return clamped(range, input -> series(min, input));
        }
    }

    private static ShrinkStrategy<Integer> clamped(IntRange range, Fn1<Integer, ImmutableFiniteIterable<Integer>> f) {
        return input -> range.includes(input)
                ? f.apply(input)
                : ShrinkResult.empty();
    }

    private static ShrinkStrategy<Long> clamped(LongRange range, Fn1<Long, ImmutableFiniteIterable<Long>> f) {
        return input -> range.includes(input)
                ? f.apply(input)
                : ShrinkResult.empty();
    }

    private static ShrinkStrategy<Short> clamped(ShortRange range, Fn1<Short, ImmutableFiniteIterable<Short>> f) {
        return input -> range.includes(input)
                ? f.apply(input)
                : ShrinkResult.empty();
    }

    private static ShrinkStrategy<Byte> clamped(ByteRange range, Fn1<Byte, ImmutableFiniteIterable<Byte>> f) {
        return input -> range.includes(input)
                ? f.apply(input)
                : ShrinkResult.empty();
    }

    private static ShrinkStrategy<Float> clamped(FloatRange range, Fn1<Float, ImmutableFiniteIterable<Float>> f) {
        return input -> range.includes(input)
                ? f.apply(input)
                : ShrinkResult.empty();
    }

    private static ShrinkStrategy<Double> clamped(DoubleRange range, Fn1<Double, ImmutableFiniteIterable<Double>> f) {
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

    private static ImmutableFiniteIterable<Float> series(float low, float high) {
        if (low >= high) {
            return ShrinkResult.empty();
        } else if (low >= high - 0.0001f) {
            if (high != 0.000001f) {
                return ShrinkResult.singleton(low + 0.000001f);
            } else {
                return ShrinkResult.empty();
            }
        } else {
            float middle = low + ((high - low) / 2);
            return ShrinkResult.cons(low, () -> series(middle, high));
        }
    }

    private static ImmutableFiniteIterable<Double> series(double low, double high) {
        if (low >= high) {
            return ShrinkResult.empty();
        } else if (low >= high - 0.0001d) {
            if (high != 0.000001d) {
                return ShrinkResult.singleton(low + 0.000001d);
            } else {
                return ShrinkResult.empty();
            }
        } else {
            double middle = low + ((high - low) / 2);
            return ShrinkResult.cons(low, () -> series(middle, high));
        }
    }
}
