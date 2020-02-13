package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Result.result;

final class CompositeValueSupplier2<A, B, Out> implements ValueSupplier<Out> {
    private final ValueSupplier<A> vsA;
    private final ValueSupplier<B> vsB;
    private final Fn2<A, B, Out> fn;

    CompositeValueSupplier2(ValueSupplier<A> vsA, ValueSupplier<B> vsB, Fn2<A, B, Out> fn) {
        this.vsA = vsA;
        this.vsB = vsB;
        this.fn = fn;
    }

    @Override
    public Result<Seed, Maybe<Out>> getNext(Seed input) {
        return threadSeed(vsA.getNext(input),
                (a, s1) -> threadSeed(vsB.getNext(s1),
                        (b, s2) -> result(s2, just(fn.apply(a, b)))));
    }

    @Override
    public Result<Seed, Either<GeneratorFailure, Out>> getNext2(Seed input) {
        return threadSeed2(vsA.getNext2(input),
                (a, s1) -> threadSeed2(vsB.getNext2(s1),
                        (b, s2) -> result(s2, right(fn.apply(a, b)))));

    }

    @Override
    public GeneratorOutput<Out> getNext3(Seed input) {
        return null;
    }

    static <A, B> Result<Seed, Maybe<B>> threadSeed(Result<Seed, Maybe<A>> ra,
                                                    Fn2<A, Seed, Result<Seed, Maybe<B>>> f) {
        return ra.getValue()
                .match(__ -> result(ra.getNextState(), nothing()),
                        a -> f.apply(a, ra.getNextState()));
    }

    static <A, B> Result<Seed, Either<GeneratorFailure, B>> threadSeed2(Result<Seed, Either<GeneratorFailure, A>> ra,
                                                                        Fn2<A, Seed, Result<Seed, Either<GeneratorFailure, B>>> f) {
        return ra.getValue()
                .match(gf -> result(ra.getNextState(), left(gf)),
                        a -> f.apply(a, ra.getNextState()));
    }
}
