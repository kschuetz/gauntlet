package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.filter.Filter;

import static dev.marksman.gauntlet.filter.Filter.filter;

class LazilyFilteredDomain<A> implements Domain<A> {
    private final Object lock;
    private final Fn1<? super A, String> prettyPrinter;
    private ImmutableVector<A> sourceElements;
    private Filter<A> filter;
    private volatile ImmutableVector<A> filteredElements;

    LazilyFilteredDomain(ImmutableVector<A> sourceElements, Filter<A> filter, Fn1<? super A, String> prettyPrinter) {
        this.lock = new Object();
        this.sourceElements = sourceElements;
        this.filteredElements = null;
        this.filter = filter;
        this.prettyPrinter = prettyPrinter;
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
    public Fn1<? super A, String> getPrettyPrinter() {
        return prettyPrinter;
    }

    @Override
    public Domain<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter) {
        synchronized (lock) {
            if (filteredElements == null) {
                return new LazilyFilteredDomain<>(sourceElements, filter, prettyPrinter);
            } else {
                return new ConcreteDomain<>(filteredElements, prettyPrinter);
            }
        }
    }

    @Override
    public Domain<A> suchThat(Fn1<? super A, Boolean> predicate) {
        synchronized (lock) {
            if (filteredElements == null) {
                return new LazilyFilteredDomain<>(sourceElements, filter.add(predicate), prettyPrinter);
            } else {
                return new LazilyFilteredDomain<>(filteredElements, filter(predicate), prettyPrinter);
            }
        }
    }

    private void applyFilter() {
        filteredElements = Vector.copyFrom(sourceElements.filter(this.filter));
        filter = null;
        sourceElements = null;
    }
}
