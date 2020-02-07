package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Result.result;

final class FilteredValueSupplier<A> implements ValueSupplier<A> {
    private final Generate<A> generateFn;
    private final Fn1<A, Boolean> filter;
    private final int maxDiscards;

    FilteredValueSupplier(Generate<A> generateFn, Fn1<A, Boolean> filter, int maxDiscards) {
        this.generateFn = generateFn;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public Result<Seed, Maybe<A>> getNext(Seed input) {
        int discardsRemaining = maxDiscards;
        Seed current = input;
        while (discardsRemaining >= 0) {
            Result<? extends Seed, A> result = generateFn.apply(current);
            if (filter.apply(result._2())) {
                return result(result._1(), just(result._2()));
            } else {
                discardsRemaining -= 1;
                current = result._1();
            }
        }

        return result(current, nothing());
    }
}
