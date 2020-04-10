package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;

import static dev.marksman.gauntlet.ConcreteDomain.concreteDomain;

public interface Domain<A> {
    ImmutableVector<A> getElements();

    Fn1<? super A, String> getPrettyPrinter();

    Domain<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter);

    Domain<A> suchThat(Fn1<? super A, Boolean> predicate);

    static <A> Domain<A> domain(Iterable<A> elements) {
        return concreteDomain(elements);
    }

    @SafeVarargs
    static <A> Domain<A> of(A first, A... more) {
        return concreteDomain(Vector.of(first, more));
    }

    static <A, B> Domain<Tuple2<A, B>> cartesianProduct(Domain<A> domainA,
                                                        Domain<B> domainB) {
        return ConcreteDomain.cartesianProduct(domainA, domainB);
    }

    static <A, B, C> Domain<Tuple3<A, B, C>> cartesianProduct(Domain<A> domainA,
                                                              Domain<B> domainB,
                                                              Domain<C> domainC) {
        return ConcreteDomain.cartesianProduct(domainA, domainB, domainC);
    }

    static <A, B, C, D> Domain<Tuple4<A, B, C, D>> cartesianProduct(Domain<A> domainA,
                                                                    Domain<B> domainB,
                                                                    Domain<C> domainC,
                                                                    Domain<D> domainD) {
        return ConcreteDomain.cartesianProduct(domainA, domainB, domainC, domainD);
    }
}
