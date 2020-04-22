package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct2.shrinkProduct2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct3.shrinkProduct3;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct4.shrinkProduct4;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct5.shrinkProduct5;

final class ShrinkTuple {

    private ShrinkTuple() {
    }

    static <A, B> ShrinkStrategy<Tuple2<A, B>> shrinkTuple2(ShrinkStrategy<A> sa,
                                                            ShrinkStrategy<B> sb) {
        return shrinkProduct2(sa, sb,
                Tuple2::tuple, t -> t);
    }

    static <A, B, C> ShrinkStrategy<Tuple3<A, B, C>> shrinkTuple3(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb,
                                                                  ShrinkStrategy<C> sc) {
        return shrinkProduct3(sa, sb, sc,
                Tuple3::tuple, t -> t);
    }

    static <A, B, C, D> ShrinkStrategy<Tuple4<A, B, C, D>> shrinkTuple4(ShrinkStrategy<A> sa,
                                                                        ShrinkStrategy<B> sb,
                                                                        ShrinkStrategy<C> sc,
                                                                        ShrinkStrategy<D> sd) {
        return shrinkProduct4(sa, sb, sc, sd,
                Tuple4::tuple, t -> t);
    }

    static <A, B, C, D, E> ShrinkStrategy<Tuple5<A, B, C, D, E>> shrinkTuple5(ShrinkStrategy<A> sa,
                                                                              ShrinkStrategy<B> sb,
                                                                              ShrinkStrategy<C> sc,
                                                                              ShrinkStrategy<D> sd,
                                                                              ShrinkStrategy<E> se) {
        return shrinkProduct5(sa, sb, sc, sd, se,
                Tuple5::tuple, t -> t);
    }
}
