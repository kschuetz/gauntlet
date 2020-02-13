package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Result.result;

final class FilteredValueSupplier<A> implements ValueSupplier<A> {
    private final ValueSupplier<A> underlying;
    private final Fn1<A, Boolean> filter;
    private final int maxDiscards;
    private final Fn0<String> labelSupplier;

    FilteredValueSupplier(ValueSupplier<A> underlying, Fn1<A, Boolean> filter, int maxDiscards, Fn0<String> labelSupplier) {
        this.underlying = underlying;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
        this.labelSupplier = labelSupplier;
    }

    @Override
    public Result<Seed, Maybe<A>> getNext(Seed input) {
        int discardsRemaining = maxDiscards;
        Seed state = input;
        while (discardsRemaining >= 0) {
            Result<Seed, Maybe<A>> current = underlying.getNext(state);
            Maybe<A> maybeValue = current.getValue();
            if (!maybeValue.match(__ -> true, __ -> false)) {
                return current;
            }

            A value = maybeValue.orElse(null);

            if (filter.apply(value)) {
                return result(current._1(), just(value));
            } else {
                discardsRemaining -= 1;
                state = current._1();
            }
        }

        return result(state, nothing());
    }

    @Override
    public Result<Seed, Either<GeneratorFailure, A>> getNext2(Seed input) {
        return null;
    }

    @Override
    public GeneratorOutput<A> getNext3(Seed input) {
        return null;
    }
}
