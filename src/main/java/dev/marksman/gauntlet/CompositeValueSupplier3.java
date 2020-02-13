package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.CompositeValueSupplier2.threadSeed;
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
        return threadSeed(vsA.getNext(input),
                (a, s1) -> threadSeed(vsB.getNext(s1),
                        (b, s2) -> threadSeed(vsC.getNext(s2),
                                (c, s3) -> result(s3, just(fn.apply(a, b, c))))));
    }

    @Override
    public Result<Seed, Either<GeneratorFailure, Out>> getNext2(Seed input) {
        return null;
    }

    @Override
    public GeneratorOutput<Out> getNext3(Seed input) {
        return null;
    }
}
