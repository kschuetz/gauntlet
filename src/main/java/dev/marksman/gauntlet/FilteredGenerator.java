package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.util.FilterChain;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Parameters;

final class FilteredGenerator<A> implements WrappedGenerator<A> {
    private final Generator<A> generator;
    private final FilterChain<A> filter;
    private final int maxDiscards;

    FilteredGenerator(Generator<A> generator, FilterChain<A> filter, int maxDiscards) {
        this.generator = generator;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public final ValueSupplier<A> prepare(Parameters parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WrappedGenerator<A> suchThat(Fn1<A, Boolean> predicate) {
        return new FilteredGenerator<>(generator, filter.add(predicate), maxDiscards);
    }

    @Override
    public WrappedGenerator<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new FilteredGenerator<>(generator, filter, maxDiscards);
        } else {
            return this;
        }
    }

    @Override
    public <B> WrappedGenerator<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new FilteredGenerator<>(generator.fmap(ab),
                filter.contraMap(ba),
                maxDiscards);
    }
}
