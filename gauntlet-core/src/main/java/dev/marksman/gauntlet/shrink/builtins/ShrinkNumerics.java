package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;

public final class ShrinkNumerics {

    private ShrinkNumerics() {

    }

    private static Shrink<Integer> INT = input -> {
        if (input < 0) {
            return ShrinkResult.cons(-input, () -> series(0, -input)).fmap(n -> -n);
        } else {
            return series(0, input);
        }
    };

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


    private static Shrink<Long> LONG = new Shrink<Long>() {
        @Override
        public ImmutableFiniteIterable<Long> apply(Long input) {
            if (input == 0) return ShrinkResult.empty();
            else return halves(input);
        }

        private ImmutableFiniteIterable<Long> halves(Long x) {
            long q = x / 2;
            if (q == 0) {
                return ImmutableIterable.of(0L);
            } else {
                return ShrinkResult.cons(q, () -> ShrinkResult.cons(-q, () -> halves(q)));
            }
        }
    };

    public static Shrink<Integer> shrinkInt() {
        return INT;
    }

    public static Shrink<Long> shrinkLong() {
        return LONG;
    }

}
