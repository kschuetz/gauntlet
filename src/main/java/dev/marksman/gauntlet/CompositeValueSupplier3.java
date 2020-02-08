package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Result.result;

final class CompositeValueSupplier3<A, B, C, Out> implements ValueSupplier<Out> {
    private final ValueSupplier<A> vsA;
    private final ValueSupplier<B> vsB;
    private final ValueSupplier<C> vsC;
    private final Fn3<A, B, C, Out> fn;

    CompositeValueSupplier3(ValueSupplier<A> vsA, ValueSupplier<B> vsB, ValueSupplier<C> vsC, Fn3<A, B, C, Out> fn) {
        this.vsA = vsA;
        this.vsB = vsB;
        this.vsC = vsC;
        this.fn = fn;
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
                                            b -> {
                                                Result<Seed, Maybe<C>> r3 = vsC.getNext(r2.getNextState());
                                                return r3.getValue()
                                                        .match(___ -> result(r3.getNextState(), nothing()),
                                                                c -> result(r3.getNextState(),
                                                                        just(fn.apply(a, b, c))));
                                            });
                        });
    }
}
