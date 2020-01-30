package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.ValueSupply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.gauntlet.GeneratorOutput.generatorOutput;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class FilteredValueSupply<A> implements Iterable<GeneratorOutput<A>> {
    private final ValueSupply<A> underlying;
    private final Fn1<A, Boolean> filter;
    private final int maxDiscards;

    @Override
    public Iterator<GeneratorOutput<A>> iterator() {
        return new FilteredValueSupplyIterator<>(underlying.iterator(), filter, maxDiscards);
    }

    private static class FilteredValueSupplyIterator<A> implements Iterator<GeneratorOutput<A>> {
        private final Iterator<A> underlying;
        private final Fn1<A, Boolean> filter;
        private int discardsRemaining;
        private volatile GeneratorOutput<A> cache;

        private FilteredValueSupplyIterator(Iterator<A> underlying, Fn1<A, Boolean> filter, int discardsRemaining) {
            this.underlying = underlying;
            this.filter = filter;
            this.discardsRemaining = discardsRemaining;
            this.cache = null;
        }

        @Override
        public boolean hasNext() {
            if (cache == null) {
                cache = findNext();
            }
            return cache != null;
        }

        @Override
        public GeneratorOutput<A> next() {
            if (cache == null) {
                throw new NoSuchElementException();
            } else {
                synchronized (this) {
                    GeneratorOutput<A> result = cache;
                    cache = null;
                    return result;

                }
            }
        }

        private GeneratorOutput<A> findNext() {
            int discardCount = 0;
            while (discardsRemaining >= 0) {
                A item = underlying.next();
                if (filter.apply(item)) {
                    return generatorOutput(item, discardCount);
                } else {
                    discardsRemaining--;
                    discardCount++;
                }
            }
            return null;
        }

    }

    static <A> Iterable<GeneratorOutput<A>> filteredValueSupply(Fn1<A, Boolean> filter,
                                                                int maxDiscards,
                                                                ValueSupply<A> valueSupply) {
        return new FilteredValueSupply<>(valueSupply, filter, maxDiscards);
    }

}
