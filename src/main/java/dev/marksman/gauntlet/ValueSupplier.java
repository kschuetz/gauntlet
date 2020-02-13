package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.MappedValueSupplier.mappedValueSupplier;

public interface ValueSupplier<A> extends Functor<A, ValueSupplier<?>> {

    Result<Seed, Maybe<A>> getNext(Seed input);

    Result<Seed, Either<GeneratorFailure, A>> getNext2(Seed input);

    GeneratorOutput<A> getNext3(Seed input);

    @Override
    default <B> ValueSupplier<B> fmap(Fn1<? super A, ? extends B> fn) {
        return mappedValueSupplier(fn, this);
    }
}