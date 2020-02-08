package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Result.result;

final class CompositeValueSupplier2<A, B, Out> implements ValueSupplier<Out> {
    private final ValueSupplier<A> vsA;
    private final ValueSupplier<B> vsB;
    private final Fn2<A, B, Out> fn;
    private final Fn1<Out, Boolean> filter;
    private final int maxDiscards;

    CompositeValueSupplier2(ValueSupplier<A> vsA, ValueSupplier<B> vsB, Fn2<A, B, Out> fn, Fn1<Out, Boolean> filter, int maxDiscards) {
        this.vsA = vsA;
        this.vsB = vsB;
        this.fn = fn;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public Result<Seed, Maybe<Out>> getNext(Seed input) {
        // TODO: this needs work!
        Result<Seed, Maybe<A>> r1 = vsA.getNext(input);
        return r1.getValue()
                .match(__ -> result(r1.getNextState(), nothing()),
                        a -> {
                            Result<Seed, Maybe<B>> r2 = vsB.getNext(r1.getNextState());
                            return r2.getValue()
                                    .match(__ -> result(r2.getNextState(), nothing()),
                                            b -> result(r2.getNextState(), just(fn.apply(a, b))));
                        });
    }
}
