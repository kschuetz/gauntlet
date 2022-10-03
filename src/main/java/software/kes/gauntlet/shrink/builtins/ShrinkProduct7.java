package software.kes.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.product.Product7;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn7;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.gauntlet.shrink.ShrinkResultBuilder;
import software.kes.gauntlet.shrink.ShrinkStrategy;

final class ShrinkProduct7 {
    private ShrinkProduct7() {
    }

    static <A, B, C, D, E, F, G, T> ShrinkStrategy<T> shrinkProduct7(ShrinkStrategy<A> sa,
                                                                     ShrinkStrategy<B> sb,
                                                                     ShrinkStrategy<C> sc,
                                                                     ShrinkStrategy<D> sd,
                                                                     ShrinkStrategy<E> se,
                                                                     ShrinkStrategy<F> sf,
                                                                     ShrinkStrategy<G> sg,
                                                                     Fn7<A, B, C, D, E, F, G, T> fromProduct,
                                                                     Fn1<T, Product7<A, B, C, D, E, F, G>> toProduct) {
        return input -> {
            Product7<A, B, C, D, E, F, G> p = toProduct.apply(input);
            A constantA = p._1();
            B constantB = p._2();
            C constantC = p._3();
            D constantD = p._4();
            E constantE = p._5();
            F constantF = p._6();
            G constantG = p._7();

            ImmutableFiniteIterable<A> as = sa.apply(constantA);
            ImmutableFiniteIterable<B> bs = sb.apply(constantB);
            ImmutableFiniteIterable<C> cs = sc.apply(constantC);
            ImmutableFiniteIterable<D> ds = sd.apply(constantD);
            ImmutableFiniteIterable<E> es = se.apply(constantE);
            ImmutableFiniteIterable<F> fs = sf.apply(constantF);
            ImmutableFiniteIterable<G> gs = sg.apply(constantG);

            return ShrinkResultBuilder.<T>shrinkResultBuilder()
                    .lazyConcat(() -> as.fmap(a -> fromProduct.apply(a, constantB, constantC, constantD, constantE, constantF, constantG)))
                    .lazyConcat(() -> bs.fmap(b -> fromProduct.apply(constantA, b, constantC, constantD, constantE, constantF, constantG)))
                    .lazyConcat(() -> cs.fmap(c -> fromProduct.apply(constantA, constantB, c, constantD, constantE, constantF, constantG)))
                    .lazyConcat(() -> ds.fmap(d -> fromProduct.apply(constantA, constantB, constantC, d, constantE, constantF, constantG)))
                    .lazyConcat(() -> es.fmap(e -> fromProduct.apply(constantA, constantB, constantC, constantD, e, constantF, constantG)))
                    .lazyConcat(() -> fs.fmap(f -> fromProduct.apply(constantA, constantB, constantC, constantD, constantE, f, constantG)))
                    .lazyConcat(() -> gs.fmap(g -> fromProduct.apply(constantA, constantB, constantC, constantD, constantE, constantF, g)))
                    .build();
        };
    }
}
