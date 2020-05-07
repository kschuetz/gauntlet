package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;

import java.util.Objects;

import static dev.marksman.gauntlet.filter.Filter.filter;

final class EnumeratedDomain<A> implements Domain<A> {
    private final ImmutableVector<A> elements;
    private final Fn1<? super A, String> prettyPrinter;

    EnumeratedDomain(ImmutableVector<A> elements, Fn1<? super A, String> prettyPrinter) {
        this.elements = elements;
        this.prettyPrinter = prettyPrinter;
    }

    static <A> EnumeratedDomain<A> enumeratedDomain(Iterable<A> elements) {
        return new EnumeratedDomain<>(Vector.copyFrom(elements), Objects::toString);
    }

    @Override
    public Domain<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter) {
        return new EnumeratedDomain<>(elements, prettyPrinter);
    }

    @Override
    public Domain<A> suchThat(Fn1<? super A, Boolean> predicate) {
        return new FilteredDomain<>(elements, filter(predicate), prettyPrinter);
    }

    public ImmutableVector<A> getElements() {
        return this.elements;
    }

    public Fn1<? super A, String> getPrettyPrinter() {
        return this.prettyPrinter;
    }
}
