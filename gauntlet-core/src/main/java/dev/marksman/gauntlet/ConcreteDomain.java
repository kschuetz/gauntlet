package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

import static dev.marksman.gauntlet.PrettyPrinting.productPrettyPrinter;
import static dev.marksman.gauntlet.filter.Filter.filter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class ConcreteDomain<A> implements Domain<A> {
    @Getter
    ImmutableVector<A> elements;
    @Getter
    Fn1<? super A, String> prettyPrinter;

    @Override
    public Domain<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter) {
        return new ConcreteDomain<>(elements, prettyPrinter);
    }

    @Override
    public Domain<A> suchThat(Fn1<? super A, Boolean> predicate) {
        return new LazilyFilteredDomain<>(elements, filter(predicate), prettyPrinter);
    }

    static <A> ConcreteDomain<A> concreteDomain(Iterable<A> elements) {
        return new ConcreteDomain<>(Vector.copyFrom(elements), Objects::toString);
    }

    static <A, B> ConcreteDomain<Tuple2<A, B>> cartesianProduct(Domain<A> domainA,
                                                                Domain<B> domainB) {
        ImmutableVector<Tuple2<A, B>> newElements = domainA.getElements().cross(domainB.getElements());
        Fn1<? super Tuple2<A, B>, String> newPrettyPrinter = productPrettyPrinter(domainA.getPrettyPrinter(),
                domainB.getPrettyPrinter());
        return new ConcreteDomain<>(newElements,
                newPrettyPrinter);
    }

    static <A, B, C> ConcreteDomain<Tuple3<A, B, C>> cartesianProduct(Domain<A> domainA,
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
        return new ConcreteDomain<>(newElements, newPrettyPrinter);
    }

    static <A, B, C, D> ConcreteDomain<Tuple4<A, B, C, D>> cartesianProduct(Domain<A> domainA,
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
        return new ConcreteDomain<>(newElements, newPrettyPrinter);
    }

}
