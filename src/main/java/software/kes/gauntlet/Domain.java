package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;

public interface Domain<A> {
    static <A> Domain<A> domain(Iterable<A> elements) {
        return EnumeratedDomain.enumeratedDomain(elements);
    }

    @SafeVarargs
    static <A> Domain<A> values(A first, A... more) {
        return EnumeratedDomain.enumeratedDomain(Vector.of(first, more));
    }

    static <A extends Enum<A>> Domain<A> domainFromEnum(Class<A> enumType) {
        return EnumeratedDomain.enumeratedDomain(Vector.copyFrom(enumType.getEnumConstants()));
    }

    static <A, B> Domain<Tuple2<A, B>> cartesianProduct(Domain<A> domainA,
                                                        Domain<B> domainB) {
        return DomainCombinators.cartesianProduct(domainA, domainB);
    }

    static <A, B, C> Domain<Tuple3<A, B, C>> cartesianProduct(Domain<A> domainA,
                                                              Domain<B> domainB,
                                                              Domain<C> domainC) {
        return DomainCombinators.cartesianProduct(domainA, domainB, domainC);
    }

    static <A, B, C, D> Domain<Tuple4<A, B, C, D>> cartesianProduct(Domain<A> domainA,
                                                                    Domain<B> domainB,
                                                                    Domain<C> domainC,
                                                                    Domain<D> domainD) {
        return DomainCombinators.cartesianProduct(domainA, domainB, domainC, domainD);
    }

    ImmutableVector<A> getElements();

    PrettyPrinter<A> getPrettyPrinter();

    Domain<A> withPrettyPrinter(PrettyPrinter<? super A> prettyPrinter);

    Domain<A> suchThat(Fn1<? super A, Boolean> predicate);
}
