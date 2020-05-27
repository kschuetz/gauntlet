package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;

public final class GeneratorOutput<A> implements Functor<A, GeneratorOutput<?>> {
    private final Seed nextState;
    private final Either<SupplyFailure, A> value;

    private GeneratorOutput(Seed nextState, Either<SupplyFailure, A> value) {
        this.nextState = nextState;
        this.value = value;
    }

    static <A> GeneratorOutput<A> generatorOutput(Seed nextState, Either<SupplyFailure, A> value) {
        return new GeneratorOutput<>(nextState, value);
    }

    static <A> GeneratorOutput<A> success(Seed nextState, A value) {
        return generatorOutput(nextState, right(value));
    }

    static <A> GeneratorOutput<A> success(Result<? extends Seed, A> result) {
        return generatorOutput(result.getNextState(), right(result.getValue()));
    }

    static <A> GeneratorOutput<A> failure(Seed nextState, SupplyFailure failure) {
        return generatorOutput(nextState, left(failure));
    }

    public final boolean isFailure() {
        return value.match(__ -> true, __ -> false);
    }

    @Override
    public <B> GeneratorOutput<B> fmap(Fn1<? super A, ? extends B> fn) {
        return generatorOutput(nextState, value.fmap(fn));
    }

    public Seed getNextState() {
        return this.nextState;
    }

    public Either<SupplyFailure, A> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "GeneratorOutput{" +
                "nextState=" + nextState +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratorOutput<?> that = (GeneratorOutput<?>) o;

        if (!nextState.equals(that.nextState)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = nextState.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
