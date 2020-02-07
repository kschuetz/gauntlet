package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Parameters;

import static dev.marksman.gauntlet.util.FilterChain.filterChain;

final class UnfilteredGeneratorMaxDiscards<A> implements WrappedGenerator<A> {
    private final Generator<A> generator;
    private final int maxDiscards;

    UnfilteredGeneratorMaxDiscards(Generator<A> generator, int maxDiscards) {
        this.generator = generator;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public Source<A> prepare(Parameters parameters) {
        return new UnfilteredSource<>(generator.prepare(parameters));
    }

    @Override
    public WrappedGenerator<A> suchThat(Fn1<A, Boolean> predicate) {
        return new FilteredGenerator<>(generator, filterChain(predicate), maxDiscards);
    }

    @Override
    public WrappedGenerator<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new UnfilteredGeneratorMaxDiscards<>(generator, maxDiscards);
        } else {
            return this;
        }
    }
}
