package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

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
                    .build();
        };
    }
}
