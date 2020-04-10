package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

import static dev.marksman.gauntlet.PrettyPrinting.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Domain<A> {
    @Getter
    ImmutableFiniteIterable<A> elements;
    @Getter
    Fn1<? super A, String> prettyPrinter;

    public Domain<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter) {
        return new Domain<>(elements, prettyPrinter);
    }

    public Domain<A> suchThat(Fn1<? super A, Boolean> predicate) {
        return new Domain<>(elements.filter(predicate), prettyPrinter);
    }

    public static <A> Domain<A> domain(Iterable<A> elements) {
        return new Domain<>(Vector.copyFrom(elements), Objects::toString);
    }

    public static <A, B> Domain<Tuple2<A, B>> combineDomains(Domain<A> domainA,
                                                             Domain<B> domainB) {
        ImmutableFiniteIterable<Tuple2<A, B>> newElements = domainA.getElements().cross(domainB.getElements());
        Fn1<? super Tuple2<A, B>, String> newPrettyPrinter = product2PrettyPrinter(domainA.getPrettyPrinter(),
                domainB.getPrettyPrinter());
        return new Domain<>(newElements,
                newPrettyPrinter);
    }

    public static <A, B, C> Domain<Tuple3<A, B, C>> combineDomains(Domain<A> domainA,
                                                                   Domain<B> domainB,
                                                                   Domain<C> domainC) {
        ImmutableFiniteIterable<Tuple3<A, B, C>> newElements = domainA.getElements().cross(domainB.getElements().cross(domainC.getElements()))
                .fmap(t -> Tuple3.tuple(
                        t._1(),
                        t._2()._1(),
                        t._2()._2()));
        Fn1<? super Tuple3<A, B, C>, String> newPrettyPrinter = product3PrettyPrinter(domainA.getPrettyPrinter(),
                domainB.getPrettyPrinter(),
                domainC.getPrettyPrinter());
        return new Domain<>(newElements, newPrettyPrinter);
    }

    public static <A, B, C, D> Domain<Tuple4<A, B, C, D>> combineDomains(Domain<A> domainA,
                                                                         Domain<B> domainB,
                                                                         Domain<C> domainC,
                                                                         Domain<D> domainD) {
        ImmutableFiniteIterable<Tuple4<A, B, C, D>> newElements = domainA.getElements()
                .cross(domainB.getElements()
                        .cross(domainC.getElements()
                                .cross(domainD.getElements())))
                .fmap(t -> Tuple4.tuple(
                        t._1(),
                        t._2()._1(),
                        t._2()._2()._1(),
                        t._2()._2()._2()));
        Fn1<? super Tuple4<A, B, C, D>, String> newPrettyPrinter = product4PrettyPrinter(domainA.getPrettyPrinter(),
                domainB.getPrettyPrinter(),
                domainC.getPrettyPrinter(),
                domainD.getPrettyPrinter());
        return new Domain<>(newElements, newPrettyPrinter);
    }
}
