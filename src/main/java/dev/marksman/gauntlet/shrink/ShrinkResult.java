package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.gauntlet.shrink.LazyConcat.lazyConcat;
import static dev.marksman.gauntlet.shrink.LazyCons.lazyCons;

public class ShrinkResult {

    public static <A> ImmutableFiniteIterable<A> empty() {
        return emptyImmutableFiniteIterable();
    }

    public static <A> ImmutableNonEmptyFiniteIterable<A> singleton(A value) {
        return ImmutableNonEmptyFiniteIterable.of(value);
    }

    public static <A> ImmutableNonEmptyFiniteIterable<A> cons(A head, Fn0<ImmutableFiniteIterable<A>> tailSupplier) {
        return lazyCons(head, tailSupplier);
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs, Fn0<ImmutableFiniteIterable<A>> other) {
        return lazyConcat(xs, other);
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> ys1,
                                                        Fn0<ImmutableFiniteIterable<A>> ys2) {
        return concat(xs, () -> concat(ys1.apply(), ys2));
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> ys1,
                                                        Fn0<ImmutableFiniteIterable<A>> ys2,
                                                        Fn0<ImmutableFiniteIterable<A>> ys3) {
        return concat(xs, () -> concat(ys1.apply(),
                () -> concat(ys2.apply(), ys3)));
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> ys1,
                                                        Fn0<ImmutableFiniteIterable<A>> ys2,
                                                        Fn0<ImmutableFiniteIterable<A>> ys3,
                                                        Fn0<ImmutableFiniteIterable<A>> ys4) {
        return concat(xs, () -> concat(ys1.apply(),
                () -> concat(ys2.apply(),
                        () -> concat(ys3.apply(), ys4))));
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> ys1,
                                                        Fn0<ImmutableFiniteIterable<A>> ys2,
                                                        Fn0<ImmutableFiniteIterable<A>> ys3,
                                                        Fn0<ImmutableFiniteIterable<A>> ys4,
                                                        Fn0<ImmutableFiniteIterable<A>> ys5) {
        return concat(xs, () -> concat(ys1.apply(), ys2, ys3, ys4, ys5));
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> ys1,
                                                        Fn0<ImmutableFiniteIterable<A>> ys2,
                                                        Fn0<ImmutableFiniteIterable<A>> ys3,
                                                        Fn0<ImmutableFiniteIterable<A>> ys4,
                                                        Fn0<ImmutableFiniteIterable<A>> ys5,
                                                        Fn0<ImmutableFiniteIterable<A>> ys6) {
        return concat(xs, () -> concat(ys1.apply(), ys2, ys3, ys4, ys5, ys6));
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> ys1,
                                                        Fn0<ImmutableFiniteIterable<A>> ys2,
                                                        Fn0<ImmutableFiniteIterable<A>> ys3,
                                                        Fn0<ImmutableFiniteIterable<A>> ys4,
                                                        Fn0<ImmutableFiniteIterable<A>> ys5,
                                                        Fn0<ImmutableFiniteIterable<A>> ys6,
                                                        Fn0<ImmutableFiniteIterable<A>> ys7) {
        return concat(xs, () -> concat(ys1.apply(), ys2, ys3, ys4, ys5, ys6, ys7));
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> ys1,
                                                        Fn0<ImmutableFiniteIterable<A>> ys2,
                                                        Fn0<ImmutableFiniteIterable<A>> ys3,
                                                        Fn0<ImmutableFiniteIterable<A>> ys4,
                                                        Fn0<ImmutableFiniteIterable<A>> ys5,
                                                        Fn0<ImmutableFiniteIterable<A>> ys6,
                                                        Fn0<ImmutableFiniteIterable<A>> ys7,
                                                        Fn0<ImmutableFiniteIterable<A>> ys8) {
        return concat(xs, () -> concat(ys1.apply(), ys2, ys3, ys4, ys5, ys6, ys7, ys8));
    }
}
