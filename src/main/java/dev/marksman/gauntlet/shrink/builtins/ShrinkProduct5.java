package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn5;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

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
                    .build();
        };
    }
}
