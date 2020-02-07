package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Parameters;

import static dev.marksman.gauntlet.util.FilterChain.filterChain;

final class UnfilteredGenerator<A> implements WrappedGenerator<A> {
    private final Generator<A> generator;

    UnfilteredGenerator(Generator<A> generator) {
        this.generator = generator;
    }

    @Override
    public Source<A> prepare(Parameters parameters) {
        return new UnfilteredSource<>(generator.prepare(parameters));
    }

    @Override
    public WrappedGenerator<A> suchThat(Fn1<A, Boolean> predicate) {
        return new FilteredGenerator<>(generator, filterChain(predicate), Gauntlet.DEFAULT_MAX_DISCARDS);
    }

    @Override
    public WrappedGenerator<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != Gauntlet.DEFAULT_MAX_DISCARDS) {
            return new UnfilteredGeneratorMaxDiscards<>(generator, maxDiscards);
        } else {
            return this;
        }
    }
}
