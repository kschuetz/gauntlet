package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.product.Product6;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn6;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkResultBuilder;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

final class ShrinkProduct6 {
    private ShrinkProduct6() {
    }

    static <A, B, C, D, E, F, T> ShrinkStrategy<T> shrinkProduct6(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb,
                                                                  ShrinkStrategy<C> sc,
                                                                  ShrinkStrategy<D> sd,
                                                                  ShrinkStrategy<E> se,
                                                                  ShrinkStrategy<F> sf,
                                                                  Fn6<A, B, C, D, E, F, T> fromProduct,
                                                                  Fn1<T, Product6<A, B, C, D, E, F>> toProduct) {
        return input -> {
            Product6<A, B, C, D, E, F> p = toProduct.apply(input);
            A constantA = p._1();
            B constantB = p._2();
            C constantC = p._3();
            D constantD = p._4();
            E constantE = p._5();
            F constantF = p._6();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);
            ImmutableFiniteIterable<C> cs = sc.apply(constantC);
            ImmutableFiniteIterable<D> ds = sd.apply(constantD);
            ImmutableFiniteIterable<E> es = se.apply(constantE);
            ImmutableFiniteIterable<F> fs = sf.apply(constantF);

            return ShrinkResultBuilder.<T>shrinkResultBuilder()
                    .lazyConcat(() -> as.fmap(a -> fromProduct.apply(a, constantB, constantC, constantD, constantE, constantF)))
                    .lazyConcat(() -> bs.fmap(b -> fromProduct.apply(constantA, b, constantC, constantD, constantE, constantF)))
                    .lazyConcat(() -> cs.fmap(c -> fromProduct.apply(constantA, constantB, c, constantD, constantE, constantF)))
                    .lazyConcat(() -> ds.fmap(d -> fromProduct.apply(constantA, constantB, constantC, d, constantE, constantF)))
                    .lazyConcat(() -> es.fmap(e -> fromProduct.apply(constantA, constantB, constantC, constantD, e, constantF)))
                    .lazyConcat(() -> fs.fmap(f -> fromProduct.apply(constantA, constantB, constantC, constantD, constantE, f)))
                    .build();
        };
    }
}
