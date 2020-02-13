package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeneratorOutput<A> implements Functor<A, GeneratorOutput<?>> {
    private final Seed nextState;
    private final Either<GeneratorFailure, A> value;

    public final boolean isFailure() {
        return value.match(__ -> true, __ -> false);
    }

    @Override
    public <B> GeneratorOutput<B> fmap(Fn1<? super A, ? extends B> fn) {
        return generatorOutput(nextState, value.fmap(fn));
    }

    static <A> GeneratorOutput<A> generatorOutput(Seed nextState, Either<GeneratorFailure, A> value) {
        return new GeneratorOutput<>(nextState, value);
    }

    static <A> GeneratorOutput<A> success(Seed nextState, A value) {
        return generatorOutput(nextState, right(value));
    }

    static <A> GeneratorOutput<A> success(Result<? extends Seed, A> result) {
        return generatorOutput(result.getNextState(), right(result.getValue()));
    }

    static <A> GeneratorOutput<A> failure(Seed nextState, GeneratorFailure failure) {
        return generatorOutput(nextState, left(failure));
    }
}
