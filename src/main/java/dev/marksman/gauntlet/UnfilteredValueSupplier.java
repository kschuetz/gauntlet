package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Either.right;

final class UnfilteredValueSupplier<A> implements ValueSupplier<A> {
    private final Generate<A> generateFn;
    private final String generatorLabel;

    UnfilteredValueSupplier(Generate<A> generateFn, String generatorLabel) {
        this.generateFn = generateFn;
        this.generatorLabel = generatorLabel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result<Seed, Maybe<A>> getNext(Seed input) {
        return (Result<Seed, Maybe<A>>) generateFn.apply(input).fmap(Maybe::just);
    }

    @Override
    public Result<Seed, Either<GeneratorFailure, A>> getNext2(Seed input) {
        Result<? extends Seed, A> r = generateFn.apply(input);
        return Result.result(r._1(), right(r._2()));
    }
}
