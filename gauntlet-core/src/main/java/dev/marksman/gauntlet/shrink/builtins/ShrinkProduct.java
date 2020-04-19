package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into5.into5;
import static com.jnape.palatable.lambda.optics.functions.View.view;

public final class ShrinkProduct {

    private ShrinkProduct() {

    }

    public static <A, B, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                    Shrink<B> sb,
                                                    Fn2<A, B, T> f1,
                                                    Fn1<T, Product2<A, B>> f2) {
        return input -> {
            Product2<A, B> p = f2.apply(input);
            A constantA = p._1();
            B constantB = p._2();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);

            return ShrinkResult.concat(
                    zip2(as, bs).fmap(into(f1::apply)),

                    () -> as.fmap(a -> f1.apply(a, constantB)),
                    () -> bs.fmap(b -> f1.apply(constantA, b)));
        };
    }

    public static <A, B, C, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                       Shrink<B> sb,
                                                       Iso<Product2<A, B>, Product2<A, B>, T, T> iso) {
        return shrinkProduct(sa, sb,
                (a, b) -> view(iso).apply(tuple(a, b)),
                view(iso.mirror()));
    }

    public static <A, B, C, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                       Shrink<B> sb,
                                                       Shrink<C> sc,
                                                       Fn3<A, B, C, T> f1,
                                                       Fn1<T, Product3<A, B, C>> f2) {
        return input -> {
            Product3<A, B, C> p = f2.apply(input);
            A constantA = p._1();
            B constantB = p._2();
            C constantC = p._3();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);
            ImmutableFiniteIterable<C> cs = sc.apply(constantC);

            return ShrinkResult.concat(
                    zip3(as, bs, cs).fmap(into3(f1::apply)),

                    () -> zip2(as, bs).fmap(into((a, b) -> f1.apply(a, b, constantC))),
                    () -> zip2(as, cs).fmap(into((a, c) -> f1.apply(a, constantB, c))),
                    () -> zip2(bs, cs).fmap(into((b, c) -> f1.apply(constantA, b, c))),

                    () -> as.fmap(a -> f1.apply(a, constantB, constantC)),
                    () -> bs.fmap(b -> f1.apply(constantA, b, constantC)),
                    () -> cs.fmap(c -> f1.apply(constantA, constantB, c)));
        };
    }

    public static <A, B, C, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                       Shrink<B> sb,
                                                       Shrink<C> sc,
                                                       Iso<Product3<A, B, C>, Product3<A, B, C>, T, T> iso) {
        return shrinkProduct(sa, sb, sc,
                (a, b, c) -> view(iso).apply(tuple(a, b, c)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                          Shrink<B> sb,
                                                          Shrink<C> sc,
                                                          Shrink<D> sd,
                                                          Fn4<A, B, C, D, T> f1,
                                                          Fn1<T, Product4<A, B, C, D>> f2) {
        return input -> {
            Product4<A, B, C, D> p = f2.apply(input);
            A constantA = p._1();
            B constantB = p._2();
            C constantC = p._3();
            D constantD = p._4();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);
            ImmutableFiniteIterable<C> cs = sc.apply(constantC);
            ImmutableFiniteIterable<D> ds = sd.apply(constantD);

            return ShrinkResult.concat(
                    zip4(as, bs, cs, ds).fmap(into4(f1::apply)),

                    () -> zip3(as, bs, cs).fmap(into3((a, b, c) -> f1.apply(a, b, c, constantD))),
                    () -> zip3(as, bs, ds).fmap(into3((a, b, d) -> f1.apply(a, b, constantC, d))),
                    () -> zip3(as, cs, ds).fmap(into3((a, c, d) -> f1.apply(a, constantB, c, d))),
                    () -> zip3(bs, cs, ds).fmap(into3((b, c, d) -> f1.apply(constantA, b, c, d))),

                    () -> zip2(as, bs).fmap(into((a, b) -> f1.apply(a, b, constantC, constantD))),
                    () -> zip2(as, cs).fmap(into((a, c) -> f1.apply(a, constantB, c, constantD))),
                    () -> zip2(as, ds).fmap(into((a, d) -> f1.apply(a, constantB, constantC, d))),
                    () -> zip2(bs, cs).fmap(into((b, c) -> f1.apply(constantA, b, c, constantD))),
                    () -> zip2(bs, ds).fmap(into((b, d) -> f1.apply(constantA, b, constantC, d))),
                    () -> zip2(cs, ds).fmap(into((c, d) -> f1.apply(constantA, constantB, c, d))),

                    () -> as.fmap(a -> f1.apply(a, constantB, constantC, constantD)),
                    () -> bs.fmap(b -> f1.apply(constantA, b, constantC, constantD)),
                    () -> cs.fmap(c -> f1.apply(constantA, constantB, c, constantD)),
                    () -> ds.fmap(d -> f1.apply(constantA, constantB, constantC, d)));
        };
    }

    public static <A, B, C, D, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                          Shrink<B> sb,
                                                          Shrink<C> sc,
                                                          Shrink<D> sd,
                                                          Iso<Product4<A, B, C, D>, Product4<A, B, C, D>, T, T> iso) {
        return shrinkProduct(sa, sb, sc, sd,
                (a, b, c, d) -> view(iso).apply(tuple(a, b, c, d)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, E, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                             Shrink<B> sb,
                                                             Shrink<C> sc,
                                                             Shrink<D> sd,
                                                             Shrink<E> se,
                                                             Fn5<A, B, C, D, E, T> f1,
                                                             Fn1<T, Product5<A, B, C, D, E>> f2) {
        return input -> {
            Product5<A, B, C, D, E> p = f2.apply(input);
            A constantA = p._1();
            B constantB = p._2();
            C constantC = p._3();
            D constantD = p._4();
            E constantE = p._5();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);
            ImmutableFiniteIterable<C> cs = sc.apply(constantC);
            ImmutableFiniteIterable<D> ds = sd.apply(constantD);
            ImmutableFiniteIterable<E> es = se.apply(constantE);

            return ShrinkResult.concat(
                    zip5(as, bs, cs, ds, es).fmap(into5(f1::apply)),

                    () -> zip4(as, bs, cs, ds).fmap(into4((a, b, c, d) -> f1.apply(a, b, c, d, constantE))),
                    () -> zip4(as, bs, cs, es).fmap(into4((a, b, c, e) -> f1.apply(a, b, c, constantD, e))),
                    () -> zip4(as, bs, ds, es).fmap(into4((a, b, d, e) -> f1.apply(a, b, constantC, d, e))),
                    () -> zip4(as, cs, ds, es).fmap(into4((a, c, d, e) -> f1.apply(a, constantB, c, d, e))),
                    () -> zip4(bs, cs, ds, es).fmap(into4((b, c, d, e) -> f1.apply(constantA, b, c, d, e))),

                    () -> zip3(as, bs, cs).fmap(into3((a, b, c) -> f1.apply(a, b, c, constantD, constantE))),
                    () -> zip3(as, bs, ds).fmap(into3((a, b, d) -> f1.apply(a, b, constantC, d, constantE))),
                    () -> zip3(as, bs, es).fmap(into3((a, b, e) -> f1.apply(a, b, constantC, constantD, e))),
                    () -> zip3(as, cs, ds).fmap(into3((a, c, d) -> f1.apply(a, constantB, c, d, constantE))),
                    () -> zip3(as, cs, es).fmap(into3((a, c, e) -> f1.apply(a, constantB, c, constantD, e))),
                    () -> zip3(as, ds, es).fmap(into3((a, d, e) -> f1.apply(a, constantB, constantC, d, e))),
                    () -> zip3(bs, cs, ds).fmap(into3((b, c, d) -> f1.apply(constantA, b, c, d, constantE))),
                    () -> zip3(bs, cs, es).fmap(into3((b, c, e) -> f1.apply(constantA, b, c, constantD, e))),
                    () -> zip3(bs, ds, es).fmap(into3((b, d, e) -> f1.apply(constantA, b, constantC, d, e))),
                    () -> zip3(cs, ds, es).fmap(into3((c, d, e) -> f1.apply(constantA, constantB, c, d, e))),

                    () -> zip2(as, bs).fmap(into((a, b) -> f1.apply(a, b, constantC, constantD, constantE))),
                    () -> zip2(as, cs).fmap(into((a, c) -> f1.apply(a, constantB, c, constantD, constantE))),
                    () -> zip2(as, ds).fmap(into((a, d) -> f1.apply(a, constantB, constantC, d, constantE))),
                    () -> zip2(as, es).fmap(into((a, e) -> f1.apply(a, constantB, constantC, constantD, e))),
                    () -> zip2(bs, cs).fmap(into((b, c) -> f1.apply(constantA, b, c, constantD, constantE))),
                    () -> zip2(bs, ds).fmap(into((b, d) -> f1.apply(constantA, b, constantC, d, constantE))),
                    () -> zip2(bs, es).fmap(into((b, e) -> f1.apply(constantA, b, constantC, constantD, e))),
                    () -> zip2(cs, ds).fmap(into((c, d) -> f1.apply(constantA, constantB, c, d, constantE))),
                    () -> zip2(cs, es).fmap(into((c, e) -> f1.apply(constantA, constantB, c, constantD, e))),
                    () -> zip2(ds, es).fmap(into((d, e) -> f1.apply(constantA, constantB, constantC, d, e))),

                    () -> as.fmap(a -> f1.apply(a, constantB, constantC, constantD, constantE)),
                    () -> bs.fmap(b -> f1.apply(constantA, b, constantC, constantD, constantE)),
                    () -> cs.fmap(c -> f1.apply(constantA, constantB, c, constantD, constantE)),
                    () -> ds.fmap(d -> f1.apply(constantA, constantB, constantC, d, constantE)),
                    () -> es.fmap(e -> f1.apply(constantA, constantB, constantC, constantD, e)));
        };
    }

    public static <A, B, C, D, E, T> Shrink<T> shrinkProduct(Shrink<A> sa,
                                                             Shrink<B> sb,
                                                             Shrink<C> sc,
                                                             Shrink<D> sd,
                                                             Shrink<E> se,
                                                             Iso<Product5<A, B, C, D, E>, Product5<A, B, C, D, E>, T, T> iso) {
        return shrinkProduct(sa, sb, sc, sd, se,
                (a, b, c, d, e) -> view(iso).apply(tuple(a, b, c, d, e)),
                view(iso.mirror()));
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

    private static <A, B, C, D> ImmutableFiniteIterable<Tuple4<A, B, C, D>> zip4(ImmutableFiniteIterable<A> as,
                                                                                 ImmutableFiniteIterable<B> bs,
                                                                                 ImmutableFiniteIterable<C> cs,
                                                                                 ImmutableFiniteIterable<D> ds) {
        return as.zipWith(Tuple2::tuple, bs.zipWith(Tuple2::tuple, cs.zipWith(Tuple2::tuple, ds)))
                .fmap(x -> tuple(x._1(), x._2()._1(), x._2()._2()._1(), x._2()._2()._2()));
    }

    private static <A, B, C, D, E> ImmutableFiniteIterable<Tuple5<A, B, C, D, E>> zip5(ImmutableFiniteIterable<A> as,
                                                                                       ImmutableFiniteIterable<B> bs,
                                                                                       ImmutableFiniteIterable<C> cs,
                                                                                       ImmutableFiniteIterable<D> ds,
                                                                                       ImmutableFiniteIterable<E> es) {
        return as.zipWith(Tuple2::tuple, bs.zipWith(Tuple2::tuple, cs.zipWith(Tuple2::tuple, ds.zipWith(Tuple2::tuple, es))))
                .fmap(x -> tuple(x._1(), x._2()._1(), x._2()._2()._1(), x._2()._2()._2()._1(), x._2()._2()._2()._2()));
    }

}
