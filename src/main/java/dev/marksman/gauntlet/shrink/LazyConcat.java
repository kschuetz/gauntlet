package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

final class LazyConcat {

    private LazyConcat() {

    }

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs, Fn0<ImmutableFiniteIterable<A>> other) {
        return xs.concat(() -> other.apply().iterator());
    }

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs,
                                                            Fn0<ImmutableFiniteIterable<A>> ys1,
                                                            Fn0<ImmutableFiniteIterable<A>> ys2) {
        return lazyConcat(xs, () -> lazyConcat(ys1.apply(), ys2));
    }


    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs,
                                                            Fn0<ImmutableFiniteIterable<A>> ys1,
                                                            Fn0<ImmutableFiniteIterable<A>> ys2,
                                                            Fn0<ImmutableFiniteIterable<A>> ys3) {
        return lazyConcat(xs, () -> lazyConcat(ys1.apply(),
                () -> lazyConcat(ys2.apply(), ys3)));
    }

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs,
                                                            Fn0<ImmutableFiniteIterable<A>> ys1,
                                                            Fn0<ImmutableFiniteIterable<A>> ys2,
                                                            Fn0<ImmutableFiniteIterable<A>> ys3,
                                                            Fn0<ImmutableFiniteIterable<A>> ys4) {
        return lazyConcat(xs, () -> lazyConcat(ys1.apply(),
                () -> lazyConcat(ys2.apply(),
                        () -> lazyConcat(ys3.apply(), ys4))));
    }

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs,
                                                            Fn0<ImmutableFiniteIterable<A>> ys1,
                                                            Fn0<ImmutableFiniteIterable<A>> ys2,
                                                            Fn0<ImmutableFiniteIterable<A>> ys3,
                                                            Fn0<ImmutableFiniteIterable<A>> ys4,
                                                            Fn0<ImmutableFiniteIterable<A>> ys5) {
        return lazyConcat(xs, () -> lazyConcat(ys1.apply(), ys2, ys3, ys4, ys5));
    }

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs,
                                                            Fn0<ImmutableFiniteIterable<A>> ys1,
                                                            Fn0<ImmutableFiniteIterable<A>> ys2,
                                                            Fn0<ImmutableFiniteIterable<A>> ys3,
                                                            Fn0<ImmutableFiniteIterable<A>> ys4,
                                                            Fn0<ImmutableFiniteIterable<A>> ys5,
                                                            Fn0<ImmutableFiniteIterable<A>> ys6) {
        return lazyConcat(xs, () -> lazyConcat(ys1.apply(), ys2, ys3, ys4, ys5, ys6));
    }

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs,
                                                            Fn0<ImmutableFiniteIterable<A>> ys1,
                                                            Fn0<ImmutableFiniteIterable<A>> ys2,
                                                            Fn0<ImmutableFiniteIterable<A>> ys3,
                                                            Fn0<ImmutableFiniteIterable<A>> ys4,
                                                            Fn0<ImmutableFiniteIterable<A>> ys5,
                                                            Fn0<ImmutableFiniteIterable<A>> ys6,
                                                            Fn0<ImmutableFiniteIterable<A>> ys7) {
        return lazyConcat(xs, () -> lazyConcat(ys1.apply(), ys2, ys3, ys4, ys5, ys6, ys7));
    }

    public static <A> ImmutableFiniteIterable<A> lazyConcat(ImmutableFiniteIterable<A> xs,
                                                            Fn0<ImmutableFiniteIterable<A>> ys1,
                                                            Fn0<ImmutableFiniteIterable<A>> ys2,
                                                            Fn0<ImmutableFiniteIterable<A>> ys3,
                                                            Fn0<ImmutableFiniteIterable<A>> ys4,
                                                            Fn0<ImmutableFiniteIterable<A>> ys5,
                                                            Fn0<ImmutableFiniteIterable<A>> ys6,
                                                            Fn0<ImmutableFiniteIterable<A>> ys7,
                                                            Fn0<ImmutableFiniteIterable<A>> ys8) {
        return lazyConcat(xs, () -> lazyConcat(ys1.apply(), ys2, ys3, ys4, ys5, ys6, ys7, ys8));
    }

}
