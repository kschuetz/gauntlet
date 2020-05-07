package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct2.zip2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct3.zip3;

final class ShrinkProduct4 {
    private ShrinkProduct4() {
    }

    static <A, B, C, D, T> ShrinkStrategy<T> shrinkProduct4(ShrinkStrategy<A> sa,
                                                            ShrinkStrategy<B> sb,
                                                            ShrinkStrategy<C> sc,
                                                            ShrinkStrategy<D> sd,
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

            return ShrinkResultBuilder.<T>shrinkResultBuilder()
                    .lazyConcat(() -> as.fmap(a -> fromProduct.apply(a, constantB, constantC, constantD)))
                    .lazyConcat(() -> bs.fmap(b -> fromProduct.apply(constantA, b, constantC, constantD)))
                    .lazyConcat(() -> cs.fmap(c -> fromProduct.apply(constantA, constantB, c, constantD)))
                    .lazyConcat(() -> ds.fmap(d -> fromProduct.apply(constantA, constantB, constantC, d)))
                    .lazyConcat(() -> zip2(as, bs).fmap(into((a, b) -> fromProduct.apply(a, b, constantC, constantD))))
                    .lazyConcat(() -> zip2(as, cs).fmap(into((a, c) -> fromProduct.apply(a, constantB, c, constantD))))
                    .lazyConcat(() -> zip2(as, ds).fmap(into((a, d) -> fromProduct.apply(a, constantB, constantC, d))))
                    .lazyConcat(() -> zip2(bs, cs).fmap(into((b, c) -> fromProduct.apply(constantA, b, c, constantD))))
                    .lazyConcat(() -> zip2(bs, ds).fmap(into((b, d) -> fromProduct.apply(constantA, b, constantC, d))))
                    .lazyConcat(() -> zip2(cs, ds).fmap(into((c, d) -> fromProduct.apply(constantA, constantB, c, d))))
                    .lazyConcat(() -> zip3(as, bs, cs).fmap(into3((a, b, c) -> fromProduct.apply(a, b, c, constantD))))
                    .lazyConcat(() -> zip3(as, bs, ds).fmap(into3((a, b, d) -> fromProduct.apply(a, b, constantC, d))))
                    .lazyConcat(() -> zip3(as, cs, ds).fmap(into3((a, c, d) -> fromProduct.apply(a, constantB, c, d))))
                    .lazyConcat(() -> zip3(bs, cs, ds).fmap(into3((b, c, d) -> fromProduct.apply(constantA, b, c, d))))
                    .lazyConcat(() -> zip4(as, bs, cs, ds).fmap(into4(fromProduct::apply)))
                    .build();
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
