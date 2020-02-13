package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneratorOutput<A> {
    private final Seed nextState;
    private final Either<GeneratorFailure, A> value;

    static <A> GeneratorOutput<A> generatorOutput(Result<? extends Seed, A> result) {
        return new GeneratorOutput<>(result.getNextState(), right(result.getValue()));
    }

    static <A> GeneratorOutput<A> generatorOutput(Seed nextState, GeneratorFailure failure) {
        return new GeneratorOutput<>(nextState, left(failure));
    }
}
