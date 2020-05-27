package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;

final class ShrinkProduct2 {
    private ShrinkProduct2() {
    }

    static <A, B, T> ShrinkStrategy<T> shrinkProduct2(ShrinkStrategy<A> sa,
                                                      ShrinkStrategy<B> sb,
                                                      Fn2<A, B, T> fromProduct,
                                                      Fn1<T, Product2<A, B>> toProduct) {
        return input -> {
            Product2<A, B> p = toProduct.apply(input);
            A constantA = p._1();
            B constantB = p._2();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);

            return ShrinkResultBuilder.<T>shrinkResultBuilder()
                    .lazyConcat(() -> as.fmap(a -> fromProduct.apply(a, constantB)))
                    .lazyConcat(() -> bs.fmap(b -> fromProduct.apply(constantA, b)))
                    .lazyConcat(() -> zip2(as, bs).fmap(into(fromProduct::apply)))
                    .build();
        };
    }

    static <A, B> ImmutableFiniteIterable<Tuple2<A, B>> zip2(ImmutableFiniteIterable<A> as,
                                                             ImmutableFiniteIterable<B> bs) {
        return as.zipWith(Tuple2::tuple, bs);
    }
}
