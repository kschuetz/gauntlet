package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct2.zip2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct3.zip3;

final class ShrinkProduct4 {

    private ShrinkProduct4() {

    }

    static <A, B, C, D, T> Shrink<T> shrinkProduct4(Shrink<A> sa,
                                                    Shrink<B> sb,
                                                    Shrink<C> sc,
                                                    Shrink<D> sd,
                                                    Fn4<A, B, C, D, T> fromProduct,
                                                    Fn1<T, Product4<A, B, C, D>> toProduct) {
        return input -> {
            Product4<A, B, C, D> p = toProduct.apply(input);
            A constantA = p._1();
            B constantB = p._2();
            C constantC = p._3();
            D constantD = p._4();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);
            ImmutableFiniteIterable<C> cs = sc.apply(constantC);
            ImmutableFiniteIterable<D> ds = sd.apply(constantD);

            return ShrinkResult.concat(
                    zip4(as, bs, cs, ds).fmap(into4(fromProduct::apply)),

                    () -> zip3(as, bs, cs).fmap(into3((a, b, c) -> fromProduct.apply(a, b, c, constantD))),
                    () -> zip3(as, bs, ds).fmap(into3((a, b, d) -> fromProduct.apply(a, b, constantC, d))),
                    () -> zip3(as, cs, ds).fmap(into3((a, c, d) -> fromProduct.apply(a, constantB, c, d))),
                    () -> zip3(bs, cs, ds).fmap(into3((b, c, d) -> fromProduct.apply(constantA, b, c, d))),

                    () -> zip2(as, bs).fmap(into((a, b) -> fromProduct.apply(a, b, constantC, constantD))),
                    () -> zip2(as, cs).fmap(into((a, c) -> fromProduct.apply(a, constantB, c, constantD))),
                    () -> zip2(as, ds).fmap(into((a, d) -> fromProduct.apply(a, constantB, constantC, d))),
                    () -> zip2(bs, cs).fmap(into((b, c) -> fromProduct.apply(constantA, b, c, constantD))),
                    () -> zip2(bs, ds).fmap(into((b, d) -> fromProduct.apply(constantA, b, constantC, d))),
                    () -> zip2(cs, ds).fmap(into((c, d) -> fromProduct.apply(constantA, constantB, c, d))),

                    () -> as.fmap(a -> fromProduct.apply(a, constantB, constantC, constantD)),
                    () -> bs.fmap(b -> fromProduct.apply(constantA, b, constantC, constantD)),
                    () -> cs.fmap(c -> fromProduct.apply(constantA, constantB, c, constantD)),
                    () -> ds.fmap(d -> fromProduct.apply(constantA, constantB, constantC, d)));
        };
    }

    static <A, B, C, D> ImmutableFiniteIterable<Tuple4<A, B, C, D>> zip4(ImmutableFiniteIterable<A> as,
                                                                         ImmutableFiniteIterable<B> bs,
                                                                         ImmutableFiniteIterable<C> cs,
                                                                         ImmutableFiniteIterable<D> ds) {
        return as.zipWith(Tuple2::tuple, bs.zipWith(Tuple2::tuple, cs.zipWith(Tuple2::tuple, ds)))
                .fmap(x -> tuple(x._1(), x._2()._1(), x._2()._2()._1(), x._2()._2()._2()));
    }

}
