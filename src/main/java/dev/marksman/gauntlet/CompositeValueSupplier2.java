package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.kraftwerk.Seed;

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
    public GeneratorOutput<Out> getNext(Seed input) {
        return threadSeed(0,
                vsA.getNext(input), (a, s1) -> threadSeed(1, vsB.getNext(s1),
                        (b, s2) -> GeneratorOutput.success(s2, fn.apply(a, b))));

    }

    static <A, B> GeneratorOutput<B> threadSeed(int posIndex,
                                                GeneratorOutput<A> ra,
                                                Fn2<A, Seed, GeneratorOutput<B>> f) {
        return ra.getValue()
                .match(gf -> GeneratorOutput.failure(ra.getNextState(), gf.prepend(positionName(posIndex))),
                        a -> f.apply(a, ra.getNextState()));
    }

    private static String positionName(int posIndex) {
        return "position " + (posIndex + 1);
    }
}
