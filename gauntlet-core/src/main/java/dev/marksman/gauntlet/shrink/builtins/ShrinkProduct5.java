package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn5;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into5.into5;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct2.zip2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct3.zip3;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct4.zip4;

final class ShrinkProduct5 {

    private ShrinkProduct5() {

    }

    static <A, B, C, D, E, T> ShrinkStrategy<T> shrinkProduct5(ShrinkStrategy<A> sa,
                                                               ShrinkStrategy<B> sb,
                                                               ShrinkStrategy<C> sc,
                                                               ShrinkStrategy<D> sd,
                                                               ShrinkStrategy<E> se,
                                                               Fn5<A, B, C, D, E, T> fromProduct,
                                                               Fn1<T, Product5<A, B, C, D, E>> toProduct) {
        return input -> {
            Product5<A, B, C, D, E> p = toProduct.apply(input);
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

            return ShrinkResultBuilder.<T>shrinkResultBuilder()
                    .lazyConcat(() -> as.fmap(a -> fromProduct.apply(a, constantB, constantC, constantD, constantE)))
                    .lazyConcat(() -> bs.fmap(b -> fromProduct.apply(constantA, b, constantC, constantD, constantE)))
                    .lazyConcat(() -> cs.fmap(c -> fromProduct.apply(constantA, constantB, c, constantD, constantE)))
                    .lazyConcat(() -> ds.fmap(d -> fromProduct.apply(constantA, constantB, constantC, d, constantE)))
                    .lazyConcat(() -> es.fmap(e -> fromProduct.apply(constantA, constantB, constantC, constantD, e)))
                    .lazyConcat(() -> zip2(as, bs).fmap(into((a, b) -> fromProduct.apply(a, b, constantC, constantD, constantE))))
                    .lazyConcat(() -> zip2(as, cs).fmap(into((a, c) -> fromProduct.apply(a, constantB, c, constantD, constantE))))
                    .lazyConcat(() -> zip2(as, ds).fmap(into((a, d) -> fromProduct.apply(a, constantB, constantC, d, constantE))))
                    .lazyConcat(() -> zip2(as, es).fmap(into((a, e) -> fromProduct.apply(a, constantB, constantC, constantD, e))))
                    .lazyConcat(() -> zip2(bs, cs).fmap(into((b, c) -> fromProduct.apply(constantA, b, c, constantD, constantE))))
                    .lazyConcat(() -> zip2(bs, ds).fmap(into((b, d) -> fromProduct.apply(constantA, b, constantC, d, constantE))))
                    .lazyConcat(() -> zip2(bs, es).fmap(into((b, e) -> fromProduct.apply(constantA, b, constantC, constantD, e))))
                    .lazyConcat(() -> zip2(cs, ds).fmap(into((c, d) -> fromProduct.apply(constantA, constantB, c, d, constantE))))
                    .lazyConcat(() -> zip2(cs, es).fmap(into((c, e) -> fromProduct.apply(constantA, constantB, c, constantD, e))))
                    .lazyConcat(() -> zip2(ds, es).fmap(into((d, e) -> fromProduct.apply(constantA, constantB, constantC, d, e))))
                    .lazyConcat(() -> zip3(as, bs, cs).fmap(into3((a, b, c) -> fromProduct.apply(a, b, c, constantD, constantE))))
                    .lazyConcat(() -> zip3(as, bs, ds).fmap(into3((a, b, d) -> fromProduct.apply(a, b, constantC, d, constantE))))
                    .lazyConcat(() -> zip3(as, bs, es).fmap(into3((a, b, e) -> fromProduct.apply(a, b, constantC, constantD, e))))
                    .lazyConcat(() -> zip3(as, cs, ds).fmap(into3((a, c, d) -> fromProduct.apply(a, constantB, c, d, constantE))))
                    .lazyConcat(() -> zip3(as, cs, es).fmap(into3((a, c, e) -> fromProduct.apply(a, constantB, c, constantD, e))))
                    .lazyConcat(() -> zip3(as, ds, es).fmap(into3((a, d, e) -> fromProduct.apply(a, constantB, constantC, d, e))))
                    .lazyConcat(() -> zip3(bs, cs, ds).fmap(into3((b, c, d) -> fromProduct.apply(constantA, b, c, d, constantE))))
                    .lazyConcat(() -> zip3(bs, cs, es).fmap(into3((b, c, e) -> fromProduct.apply(constantA, b, c, constantD, e))))
                    .lazyConcat(() -> zip3(bs, ds, es).fmap(into3((b, d, e) -> fromProduct.apply(constantA, b, constantC, d, e))))
                    .lazyConcat(() -> zip3(cs, ds, es).fmap(into3((c, d, e) -> fromProduct.apply(constantA, constantB, c, d, e))))
                    .lazyConcat(() -> zip4(as, bs, cs, ds).fmap(into4((a, b, c, d) -> fromProduct.apply(a, b, c, d, constantE))))
                    .lazyConcat(() -> zip4(as, bs, cs, es).fmap(into4((a, b, c, e) -> fromProduct.apply(a, b, c, constantD, e))))
                    .lazyConcat(() -> zip4(as, bs, ds, es).fmap(into4((a, b, d, e) -> fromProduct.apply(a, b, constantC, d, e))))
                    .lazyConcat(() -> zip4(as, cs, ds, es).fmap(into4((a, c, d, e) -> fromProduct.apply(a, constantB, c, d, e))))
                    .lazyConcat(() -> zip4(bs, cs, ds, es).fmap(into4((b, c, d, e) -> fromProduct.apply(constantA, b, c, d, e))))
                    .lazyConcat(() -> zip5(as, bs, cs, ds, es).fmap(into5(fromProduct::apply)))
                    .build();
        };
    }

    static <A, B, C, D, E> ImmutableFiniteIterable<Tuple5<A, B, C, D, E>> zip5(ImmutableFiniteIterable<A> as,
                                                                               ImmutableFiniteIterable<B> bs,
                                                                               ImmutableFiniteIterable<C> cs,
                                                                               ImmutableFiniteIterable<D> ds,
                                                                               ImmutableFiniteIterable<E> es) {
        return as.zipWith(Tuple2::tuple, bs.zipWith(Tuple2::tuple, cs.zipWith(Tuple2::tuple, ds.zipWith(Tuple2::tuple, es))))
                .fmap(x -> tuple(x._1(), x._2()._1(), x._2()._2()._1(), x._2()._2()._2()._1(), x._2()._2()._2()._2()));
    }

}
