package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.shrink.ShrinkResult;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct2.zip2;

final class ShrinkProduct3 {

    private ShrinkProduct3() {

    }

    static <A, B, C, T> Shrink<T> shrinkProduct3(Shrink<A> sa,
                                                 Shrink<B> sb,
                                                 Shrink<C> sc,
                                                 Fn3<A, B, C, T> fromProduct,
                                                 Fn1<T, Product3<A, B, C>> toProduct) {
        return input -> {
            Product3<A, B, C> p = toProduct.apply(input);
            A constantA = p._1();
            B constantB = p._2();
            C constantC = p._3();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);
            ImmutableFiniteIterable<C> cs = sc.apply(constantC);

            return ShrinkResult.concat(
                    zip3(as, bs, cs).fmap(into3(fromProduct::apply)),

                    () -> zip2(as, bs).fmap(into((a, b) -> fromProduct.apply(a, b, constantC))),
                    () -> zip2(as, cs).fmap(into((a, c) -> fromProduct.apply(a, constantB, c))),
                    () -> zip2(bs, cs).fmap(into((b, c) -> fromProduct.apply(constantA, b, c))),

                    () -> as.fmap(a -> fromProduct.apply(a, constantB, constantC)),
                    () -> bs.fmap(b -> fromProduct.apply(constantA, b, constantC)),
                    () -> cs.fmap(c -> fromProduct.apply(constantA, constantB, c)));
        };
    }

    static <A, B, C> ImmutableFiniteIterable<Tuple3<A, B, C>> zip3(ImmutableFiniteIterable<A> as,
                                                                   ImmutableFiniteIterable<B> bs,
                                                                   ImmutableFiniteIterable<C> cs) {
        return as
                .zipWith(Tuple2::tuple, bs
                        .zipWith(Tuple2::tuple, cs))
                .fmap(x -> tuple(x._1(), x._2()._1(), x._2()._2()));
    }

}
