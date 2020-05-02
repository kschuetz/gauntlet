package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

import static dev.marksman.gauntlet.filter.Filter.filter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class EnumeratedDomain<A> implements Domain<A> {
    @Getter
    ImmutableVector<A> elements;
    @Getter
    Fn1<? super A, String> prettyPrinter;

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

}
