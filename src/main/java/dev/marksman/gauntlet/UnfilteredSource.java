package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class UnfilteredSource<A> implements Source<A> {
    private final Generate<A> generateFn;

    UnfilteredSource(Generate<A> generateFn) {
        this.generateFn = generateFn;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result<Seed, Maybe<A>> getNext(Seed input) {
        return (Result<Seed, Maybe<A>>) generateFn.apply(input).fmap(Maybe::just);
    }
}
