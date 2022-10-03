package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;
import software.kes.gauntlet.filter.Filter;

import static software.kes.gauntlet.filter.Filter.filter;

final class FilteredDomain<A> implements Domain<A> {
    private final Object lock;
    private final PrettyPrinter<A> prettyPrinter;
    private ImmutableVector<A> sourceElements;
    private Filter<A> filter;
    private volatile ImmutableVector<A> filteredElements;

    @SuppressWarnings("unchecked")
    FilteredDomain(ImmutableVector<A> sourceElements, Filter<A> filter, PrettyPrinter<? super A> prettyPrinter) {
        this.lock = new Object();
        this.sourceElements = sourceElements;
        this.filteredElements = null;
        this.filter = filter;
        this.prettyPrinter = (PrettyPrinter<A>) prettyPrinter;
    }

    @Override
    public ImmutableVector<A> getElements() {
        if (filteredElements == null) {
            synchronized (lock) {
                if (filteredElements == null) {
                    applyFilter();
                }
            }
        }
        return filteredElements;
    }

    @Override
    public PrettyPrinter<A> getPrettyPrinter() {
        return prettyPrinter;
    }

    @Override
    public Domain<A> withPrettyPrinter(PrettyPrinter<? super A> prettyPrinter) {
        synchronized (lock) {
            if (filteredElements == null) {
                return new FilteredDomain<>(sourceElements, filter, prettyPrinter);
            } else {
                return new EnumeratedDomain<>(filteredElements, prettyPrinter);
            }
        }
    }

    @Override
    public Domain<A> suchThat(Fn1<? super A, Boolean> predicate) {
        synchronized (lock) {
            if (filteredElements == null) {
                return new FilteredDomain<>(sourceElements, filter.add(predicate), prettyPrinter);
            } else {
                return new FilteredDomain<>(filteredElements, filter(predicate), prettyPrinter);
            }
        }
    }

    private void applyFilter() {
        filteredElements = Vector.copyFrom(sourceElements.filter(this.filter));
        filter = null;
        sourceElements = null;
    }
}
