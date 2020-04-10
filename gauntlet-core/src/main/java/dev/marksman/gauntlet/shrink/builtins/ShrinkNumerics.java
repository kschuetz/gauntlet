package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;

import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.gauntlet.Prop.prop;
import static dev.marksman.gauntlet.SimpleResult.fail;
import static dev.marksman.gauntlet.SimpleResult.pass;

public final class ShrinkNumerics {

    private ShrinkNumerics() {

    }

    private static Shrink<Integer> INT = input -> {
        if (input < 0) {
            int high = input == Integer.MIN_VALUE ? Integer.MAX_VALUE : -input;
            return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
        } else {
            return series(0, input);
        }
    };

    private static Shrink<Long> LONG = input -> {
        if (input < 0) {
            long high = input == Long.MIN_VALUE ? Long.MAX_VALUE : -input;
            return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
        } else {
            return series(0, input);
        }
    };

    private static Shrink<Short> SHORT = input -> {
        if (input < 0) {
            short high = input == Short.MIN_VALUE ? Short.MAX_VALUE : (short) (-input);
            return ShrinkResult.cons(high, () -> series((short) 0, high).fmap(n -> (short) -n));
        } else {
            return series((short) 0, input);
        }
    };

    private static Shrink<Byte> BYTE = input -> {
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
     *
     * @param min minimum value (inclusive) allowed in the output
     * @param max maximum value (inclusive) allowed in the output
     */
    public static Shrink<Integer> shrinkInt(int min, int max) {
        if (min >= max) {
            return Shrink.none();
        } else if (min < 0 && max < 0) {
            // all negative
            return clamped(min, max, input -> series(-max, -input).fmap(n -> -n));
        } else if (min < 0) {
            // negative and positive
            return clamped(min, max, input -> {
                if (input < 0) {
                    int high = Math.min(-input, max);
                    return ShrinkResult.cons(high, () -> series(0, high).fmap(n -> -n));
                } else {
                    return series(0, input);
                }
            });
        } else {
            // non-negative
            return clamped(min, max, input -> series(min, input));
        }
    }

    private static Shrink<Integer> clamped(int min, int max, Fn1<Integer, ImmutableFiniteIterable<Integer>> f) {
        return input -> (input < min || input > max)
                ? ShrinkResult.empty() : f.apply(input);
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

    private static <A> Prop<Tuple2<A, ImmutableFiniteIterable<A>>> neverRepeatsAnElement_1() {
        return prop("never repeats an element",
                testCase -> {
                    HashSet<A> seen = new HashSet<>();
                    seen.add(testCase._1());
                    int i = 0;
                    for (A element : testCase._2()) {
                        if (seen.contains(element)) {
                            return fail("repeated element " + element + " in position " + i);
                        }
                        i++;
                        seen.add(element);
                    }
                    return pass();
                });
    }

    public static void main(String[] args) {
        Prop<Tuple2<Integer, ImmutableFiniteIterable<Integer>>> prop = neverRepeatsAnElement_1();

        int input = -1946546245;

        Shrink<Integer> shrinker = shrinkInt(-2094167625, -1946546245);

        ImmutableFiniteIterable<Integer> xs = shrinker.apply(input);

        System.out.println(prop.evaluate(tuple(input, xs)));

        xs.forEach(System.out::println);


    }

}
