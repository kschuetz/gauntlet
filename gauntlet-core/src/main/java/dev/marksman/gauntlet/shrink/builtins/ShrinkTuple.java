package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct.shrinkProduct;

public final class ShrinkTuple {

    private ShrinkTuple() {
    }

    public static <A, B> ShrinkStrategy<Tuple2<A, B>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb) {
        return shrinkProduct(sa, sb,
                Tuple2::tuple, t -> t);
    }

    public static <A, B, C> ShrinkStrategy<Tuple3<A, B, C>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                        ShrinkStrategy<B> sb,
                                                                        ShrinkStrategy<C> sc) {
        return shrinkProduct(sa, sb, sc,
                Tuple3::tuple, t -> t);
    }

    public static <A, B, C, D> ShrinkStrategy<Tuple4<A, B, C, D>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                              ShrinkStrategy<B> sb,
                                                                              ShrinkStrategy<C> sc,
                                                                              ShrinkStrategy<D> sd) {
        return shrinkProduct(sa, sb, sc, sd,
                Tuple4::tuple, t -> t);
    }

    public static <A, B, C, D, E> ShrinkStrategy<Tuple5<A, B, C, D, E>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                                    ShrinkStrategy<B> sb,
                                                                                    ShrinkStrategy<C> sc,
                                                                                    ShrinkStrategy<D> sd,
                                                                                    ShrinkStrategy<E> se) {
        return shrinkProduct(sa, sb, sc, sd, se,
                Tuple4::tuple, t -> t);
    }
}
