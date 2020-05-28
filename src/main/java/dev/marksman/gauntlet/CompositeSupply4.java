package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.CompositeSupply2.threadSeed;
import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupply4<A, B, C, D, Out> implements Supply<Out> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Fn4<A, B, C, D, Out> fn;

    CompositeSupply4(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD, Fn4<A, B, C, D, Out> fn) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.fn = fn;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree(), supplyD.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        return threadSeed(0,
                supplyA.getNext(input), (a, s1) -> threadSeed(1, supplyB.getNext(s1),
                        (b, s2) -> threadSeed(2, supplyC.getNext(s2),
                                (c, s3) -> threadSeed(3, supplyD.getNext(s3),
                                        (d, s4) -> GeneratorOutput.success(s4, fn.apply(a, b, c, d))))));
    }
}
