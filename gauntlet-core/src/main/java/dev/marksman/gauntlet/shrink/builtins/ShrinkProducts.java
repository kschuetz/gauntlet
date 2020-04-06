package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.builtin.fn2.Into3;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkInt;

public final class ShrinkProducts {

    public static <A, B, C, T> Shrink<T> shrink2(Shrink<A> sa,
                                                 Shrink<B> sb,
                                                 Fn2<A, B, T> f1,
                                                 Fn1<T, Product2<A, B>> f2) {
        return input -> {
            Product2<A, B> p = f2.apply(input);
            A t1 = p._1();
            B t2 = p._2();

            return ShrinkResult.concat(sa.apply(t1).fmap(a -> f1.apply(a, t2)),
                    () -> sb.apply(t2).fmap(b -> f1.apply(t1, b)),
                    () -> sa.apply(t1)
                            .zipWith(Tuple2::tuple, sb.apply(t2))
                            .fmap(x -> f1.apply(x._1(), x._2()))
            );
        };
    }

    public static <A, B, C, T> Shrink<T> shrink2(Shrink<A> sa,
                                                 Shrink<B> sb,
                                                 Iso<Product2<A, B>, Product2<A, B>, T, T> iso) {
        return shrink2(sa, sb,
                (a, b) -> view(iso).apply(tuple(a, b)),
                view(iso.mirror()));
    }

    public static <A, B> Shrink<Tuple2<A, B>> shrink2(Shrink<A> sa,
                                                      Shrink<B> sb) {
        return shrink2(sa, sb,
                Tuple2::tuple, t -> t);
    }

    public static <A, B, C, T> Shrink<T> shrink3(Shrink<A> sa,
                                                 Shrink<B> sb,
                                                 Shrink<C> sc,
                                                 Fn3<A, B, C, T> f1,
                                                 Fn1<T, Product3<A, B, C>> f2) {
        return input -> {
            Product3<A, B, C> p = f2.apply(input);
            A t1 = p._1();
            B t2 = p._2();
            C t3 = p._3();

            return ShrinkResult.concat(sa.apply(t1).fmap(a -> f1.apply(a, t2, t3)),
                    () -> sb.apply(t2).fmap(b -> f1.apply(t1, b, t3)),
                    () -> sc.apply(t3).fmap(c -> f1.apply(t1, t2, c)),
                    () -> zip2(sb.apply(t2), sc.apply(t3)).fmap(t -> f1.apply(t1, t._1(), t._2())),
                    () -> zip2(sa.apply(t1), sc.apply(t3)).fmap(t -> f1.apply(t._1(), t2, t._2())),
                    () -> zip2(sa.apply(t1), sb.apply(t2)).fmap(t -> f1.apply(t._1(), t._2(), t3)),
                    () -> zip3(sa.apply(t1), sb.apply(t2), sc.apply(t3)).fmap(Into3.into3(f1))
            );
        };
    }


    public static <A, B, C, T> Shrink<T> shrink3(Shrink<A> sa,
                                                 Shrink<B> sb,
                                                 Shrink<C> sc,
                                                 Iso<Product3<A, B, C>, Product3<A, B, C>, T, T> iso) {
        return shrink3(sa, sb, sc,
                (a, b, c) -> view(iso).apply(tuple(a, b, c)),
                view(iso.mirror()));
    }

    public static <A, B, C> Shrink<Tuple3<A, B, C>> shrink3(Shrink<A> sa,
                                                            Shrink<B> sb,
                                                            Shrink<C> sc) {
        return shrink3(sa, sb, sc,
                Tuple3::tuple, t -> t);
    }

    public static <A, B, C, D, T> Shrink<T> shrink4(Shrink<A> sa,
                                                    Shrink<B> sb,
                                                    Shrink<C> sc,
                                                    Shrink<D> sd,
                                                    Fn4<A, B, C, D, T> f1,
                                                    Fn1<T, Product4<A, B, C, D>> f2) {
        return input -> {
            Product4<A, B, C, D> p = f2.apply(input);
            A t1 = p._1();
            B t2 = p._2();
            C t3 = p._3();
            D t4 = p._4();
            return null;

//            return ShrinkResult.concat(sa.apply(t1).fmap(a -> f1.apply(a, t2, t3, t4)),
//                    () -> sb.apply(t2).fmap(b -> f1.apply(t1, b, t3, t4)),
//                    () -> sc.apply(t3).fmap(c -> f1.apply(t1, t2, c, t4)),
//                    () -> sc.apply(t4).fmap(d -> f1.apply(t1, t2, t3, d)),
//
//                    () -> sb.apply(t2).zipWith(Tuple2::tuple, sc.apply(t3)).fmap(t -> f1.apply(t1, t._1(), t._2())),
//                    () -> sa.apply(t1).zipWith(Tuple2::tuple, sc.apply(t3)).fmap(t -> f1.apply(t._1(), t2, t._2())),
//                    () -> sa.apply(t1).zipWith(Tuple2::tuple, sb.apply(t2)).fmap(t -> f1.apply(t._1(), t._2(), t3)),
//
//                    () -> sa.apply(t1)
//                            .zipWith(Tuple2::tuple, sb.apply(t2)
//                                    .zipWith(Tuple2::tuple, sc.apply(t3)))
//                            .fmap(x -> f1.apply(x._1(), x._2()._1(), x._2()._2()))
//            );
        };
    }

    private static <A, B> ImmutableFiniteIterable<Tuple2<A, B>> zip2(ImmutableFiniteIterable<A> as,
                                                                     ImmutableFiniteIterable<B> bs) {
        return as.zipWith(Tuple2::tuple, bs);
    }

    private static <A, B, C> ImmutableFiniteIterable<Tuple3<A, B, C>> zip3(ImmutableFiniteIterable<A> as,
                                                                           ImmutableFiniteIterable<B> bs,
                                                                           ImmutableFiniteIterable<C> cs) {
        return as
                .zipWith(Tuple2::tuple, bs
                        .zipWith(Tuple2::tuple, cs))
                .fmap(x -> tuple(x._1(), x._2()._1(), x._2()._2()));
    }

    public static void main(String[] args) {
        Shrink<Tuple3<Integer, Integer, Integer>> shrinker = shrink3(shrinkInt(), shrinkInt(), shrinkInt());

        ImmutableFiniteIterable<Tuple3<Integer, Integer, Integer>> result = shrinker.apply(tuple(17, -3, 10));

        result.forEach(System.out::println);
    }
}
