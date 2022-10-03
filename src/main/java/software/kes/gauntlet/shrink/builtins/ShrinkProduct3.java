package software.kes.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.gauntlet.shrink.ShrinkResultBuilder;
import software.kes.gauntlet.shrink.ShrinkStrategy;

final class ShrinkProduct3 {
    private ShrinkProduct3() {
    }

    static <A, B, C, T> ShrinkStrategy<T> shrinkProduct3(ShrinkStrategy<A> sa,
                                                         ShrinkStrategy<B> sb,
                                                         ShrinkStrategy<C> sc,
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

            return ShrinkResultBuilder.<T>shrinkResultBuilder()
                    .lazyConcat(() -> as.fmap(a -> fromProduct.apply(a, constantB, constantC)))
                    .lazyConcat(() -> bs.fmap(b -> fromProduct.apply(constantA, b, constantC)))
                    .lazyConcat(() -> cs.fmap(c -> fromProduct.apply(constantA, constantB, c)))
                    .build();
        };
    }
}
