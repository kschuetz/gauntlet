package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.CompositeSupply2.threadSeed;
import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupply3<A, B, C, Out> implements Supply<Out> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Fn3<A, B, C, Out> fn;

    CompositeSupply3(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Fn3<A, B, C, Out> fn) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.fn = fn;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        return threadSeed(0,
                supplyA.getNext(input), (a, s1) -> threadSeed(1, supplyB.getNext(s1),
                        (b, s2) -> threadSeed(2, supplyC.getNext(s2),
                                (c, s3) -> GeneratorOutput.success(s3, fn.apply(a, b, c)))));
    }
}
