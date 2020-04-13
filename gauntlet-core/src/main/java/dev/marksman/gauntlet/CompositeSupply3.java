package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.CompositeSupply2.threadSeed;
import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupply3<A, B, C, Out> implements Supply<Out> {
    private final Supply<A> vsA;
    private final Supply<B> vsB;
    private final Supply<C> vsC;
    private final Fn3<A, B, C, Out> fn;

    CompositeSupply3(Supply<A> vsA, Supply<B> vsB, Supply<C> vsC, Fn3<A, B, C, Out> fn) {
        this.vsA = vsA;
        this.vsB = vsB;
        this.vsC = vsC;
        this.fn = fn;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(vsA.getSupplyTree(), vsB.getSupplyTree(), vsC.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        return threadSeed(0,
                vsA.getNext(input), (a, s1) -> threadSeed(1, vsB.getNext(s1),
                        (b, s2) -> threadSeed(2, vsC.getNext(s2),
                                (c, s3) -> GeneratorOutput.success(s3, fn.apply(a, b, c)))));
    }

}
