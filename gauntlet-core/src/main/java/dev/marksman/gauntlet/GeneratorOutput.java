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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GeneratorOutput)) return false;
        final GeneratorOutput<?> other = (GeneratorOutput<?>) o;
        final Object this$nextState = this.getNextState();
        final Object other$nextState = other.getNextState();
        if (this$nextState == null ? other$nextState != null : !this$nextState.equals(other$nextState)) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        return this$value == null ? other$value == null : this$value.equals(other$value);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $nextState = this.getNextState();
        result = result * PRIME + ($nextState == null ? 43 : $nextState.hashCode());
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "GeneratorOutput(nextState=" + this.getNextState() + ", value=" + this.getValue() + ")";
    }
}
