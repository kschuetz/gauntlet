package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.CompositeValueSupplier2.threadSeed;

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
    public GeneratorOutput<Out> getNext(Seed input) {
        return threadSeed(0,
                vsA.getNext(input), (a, s1) -> threadSeed(1, vsB.getNext(s1),
                        (b, s2) -> threadSeed(2, vsC.getNext(s2),
                                (c, s3) -> GeneratorOutput.success(s3, fn.apply(a, b, c)))));
    }
}
