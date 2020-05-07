package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;

import static dev.marksman.gauntlet.PrettyPrinting.productPrettyPrinter;

final class DomainCombinators {

    private DomainCombinators() {

    }

    static <A, B> Domain<Tuple2<A, B>> cartesianProduct(Domain<A> domainA,
                                                        Domain<B> domainB) {
        ImmutableVector<Tuple2<A, B>> newElements = domainA.getElements().cross(domainB.getElements());
        Fn1<? super Tuple2<A, B>, String> newPrettyPrinter = productPrettyPrinter(domainA.getPrettyPrinter(),
                domainB.getPrettyPrinter());
        return new EnumeratedDomain<>(newElements,
                newPrettyPrinter);
    }

    static <A, B, C> Domain<Tuple3<A, B, C>> cartesianProduct(Domain<A> domainA,
                                                              Domain<B> domainB,
                                                              Domain<C> domainC) {
        ImmutableVector<Tuple3<A, B, C>> newElements = domainA.getElements().cross(domainB.getElements().cross(domainC.getElements()))
                .fmap(t -> Tuple3.tuple(
                        t._1(),
                        t._2()._1(),
                        t._2()._2()));
        Fn1<? super Tuple3<A, B, C>, String> newPrettyPrinter = PrettyPrinting.productPrettyPrinter(domainA.getPrettyPrinter(),
                domainB.getPrettyPrinter(),
                domainC.getPrettyPrinter());
        return new EnumeratedDomain<>(newElements, newPrettyPrinter);
    }

    static <A, B, C, D> Domain<Tuple4<A, B, C, D>> cartesianProduct(Domain<A> domainA,
                                                                    Domain<B> domainB,
                                                                    Domain<C> domainC,
                                                                    Domain<D> domainD) {
        ImmutableVector<Tuple4<A, B, C, D>> newElements = domainA.getElements()
                .cross(domainB.getElements()
                        .cross(domainC.getElements()
                                .cross(domainD.getElements())))
                .fmap(t -> Tuple4.tuple(
                        t._1(),
                        t._2()._1(),
                        t._2()._2()._1(),
                        t._2()._2()._2()));
        Fn1<? super Tuple4<A, B, C, D>, String> newPrettyPrinter = PrettyPrinting.productPrettyPrinter(domainA.getPrettyPrinter(),
                domainB.getPrettyPrinter(),
                domainC.getPrettyPrinter(),
                domainD.getPrettyPrinter());
        return new EnumeratedDomain<>(newElements, newPrettyPrinter);
    }
}
