package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.gauntlet.shrink.Shrink;

import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct.shrinkProduct;

public final class ShrinkTuple {

    private ShrinkTuple() {
    }

    public static <A, B> Shrink<Tuple2<A, B>> shrinkTuple(Shrink<A> sa,
                                                          Shrink<B> sb) {
        return shrinkProduct(sa, sb,
                Tuple2::tuple, t -> t);
    }

    public static <A, B, C> Shrink<Tuple3<A, B, C>> shrinkTuple(Shrink<A> sa,
                                                                Shrink<B> sb,
                                                                Shrink<C> sc) {
        return shrinkProduct(sa, sb, sc,
                Tuple3::tuple, t -> t);
    }

    public static <A, B, C, D> Shrink<Tuple4<A, B, C, D>> shrinkTuple(Shrink<A> sa,
                                                                      Shrink<B> sb,
                                                                      Shrink<C> sc,
                                                                      Shrink<D> sd) {
        return shrinkProduct(sa, sb, sc, sd,
                Tuple4::tuple, t -> t);
    }

    public static <A, B, C, D, E> Shrink<Tuple5<A, B, C, D, E>> shrinkTuple(Shrink<A> sa,
                                                                            Shrink<B> sb,
                                                                            Shrink<C> sc,
                                                                            Shrink<D> sd,
                                                                            Shrink<E> se) {
        return shrinkProduct(sa, sb, sc, sd, se,
                Tuple4::tuple, t -> t);
    }
}
