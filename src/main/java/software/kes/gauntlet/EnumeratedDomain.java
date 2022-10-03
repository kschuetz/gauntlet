package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;

import static software.kes.gauntlet.PrettyPrinter.defaultPrettyPrinter;
import static software.kes.gauntlet.filter.Filter.filter;

final class EnumeratedDomain<A> implements Domain<A> {
    private final ImmutableVector<A> elements;
    private final PrettyPrinter<A> prettyPrinter;

    @SuppressWarnings("unchecked")
    EnumeratedDomain(ImmutableVector<A> elements, PrettyPrinter<? super A> prettyPrinter) {
        this.elements = elements;
        this.prettyPrinter = (PrettyPrinter<A>) prettyPrinter;
    }

    static <A> EnumeratedDomain<A> enumeratedDomain(Iterable<A> elements) {
        return new EnumeratedDomain<>(Vector.copyFrom(elements), defaultPrettyPrinter());
    }

    @Override
    public EnumeratedDomain<A> withPrettyPrinter(PrettyPrinter<? super A> prettyPrinter) {
        return new EnumeratedDomain<>(elements, prettyPrinter);
    }

    @Override
    public Domain<A> suchThat(Fn1<? super A, Boolean> predicate) {
        return new FilteredDomain<>(elements, filter(predicate), prettyPrinter);
    }

    public ImmutableVector<A> getElements() {
        return this.elements;
    }

    public PrettyPrinter<A> getPrettyPrinter() {
        return this.prettyPrinter;
    }
}
