package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct2.shrinkProduct2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct3.shrinkProduct3;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct4.shrinkProduct4;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct5.shrinkProduct5;

public final class ShrinkProduct {

    private ShrinkProduct() {

    }

    public static <A, B, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                            ShrinkStrategy<B> sb,
                                                            Fn2<A, B, T> fromProduct,
                                                            Fn1<T, Product2<A, B>> toProduct) {
        return shrinkProduct2(sa, sb, fromProduct, toProduct);
    }

    public static <A, B, C, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                               ShrinkStrategy<B> sb,
                                                               Iso<Product2<A, B>, Product2<A, B>, T, T> iso) {
        return shrinkProduct(sa, sb,
                (a, b) -> view(iso).apply(tuple(a, b)),
                view(iso.mirror()));
    }

    public static <A, B, C, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                               ShrinkStrategy<B> sb,
                                                               ShrinkStrategy<C> sc,
                                                               Fn3<A, B, C, T> fromProduct,
                                                               Fn1<T, Product3<A, B, C>> toProduct) {
        return shrinkProduct3(sa, sb, sc, fromProduct, toProduct);
    }

    public static <A, B, C, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                               ShrinkStrategy<B> sb,
                                                               ShrinkStrategy<C> sc,
                                                               Iso<Product3<A, B, C>, Product3<A, B, C>, T, T> iso) {
        return shrinkProduct(sa, sb, sc,
                (a, b, c) -> view(iso).apply(tuple(a, b, c)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb,
                                                                  ShrinkStrategy<C> sc,
                                                                  ShrinkStrategy<D> sd,
                                                                  Fn4<A, B, C, D, T> fromProduct,
                                                                  Fn1<T, Product4<A, B, C, D>> toProduct) {
        return shrinkProduct4(sa, sb, sc, sd, fromProduct, toProduct);
    }

    public static <A, B, C, D, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb,
                                                                  ShrinkStrategy<C> sc,
                                                                  ShrinkStrategy<D> sd,
                                                                  Iso<Product4<A, B, C, D>, Product4<A, B, C, D>, T, T> iso) {
        return shrinkProduct(sa, sb, sc, sd,
                (a, b, c, d) -> view(iso).apply(tuple(a, b, c, d)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, E, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                     ShrinkStrategy<B> sb,
                                                                     ShrinkStrategy<C> sc,
                                                                     ShrinkStrategy<D> sd,
                                                                     ShrinkStrategy<E> se,
                                                                     Fn5<A, B, C, D, E, T> fromProduct,
                                                                     Fn1<T, Product5<A, B, C, D, E>> toProduct) {
        return shrinkProduct5(sa, sb, sc, sd, se, fromProduct, toProduct);
    }

    public static <A, B, C, D, E, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                     ShrinkStrategy<B> sb,
                                                                     ShrinkStrategy<C> sc,
                                                                     ShrinkStrategy<D> sd,
                                                                     ShrinkStrategy<E> se,
                                                                     Iso<Product5<A, B, C, D, E>, Product5<A, B, C, D, E>, T, T> iso) {
        return shrinkProduct(sa, sb, sc, sd, se,
                (a, b, c, d, e) -> view(iso).apply(tuple(a, b, c, d, e)),
                view(iso.mirror()));
    }

}
