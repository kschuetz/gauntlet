package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn6;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.CompositeSupply2.threadSeed;
import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupply6<A, B, C, D, E, F, Out> implements Supply<Out> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Supply<E> supplyE;
    private final Supply<F> supplyF;
    private final Fn6<A, B, C, D, E, F, Out> fn;

    CompositeSupply6(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD, Supply<E> supplyE, Supply<F> supplyF, Fn6<A, B, C, D, E, F, Out> fn) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.supplyE = supplyE;
        this.supplyF = supplyF;
        this.fn = fn;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree(), supplyD.getSupplyTree(),
                supplyE.getSupplyTree(), supplyF.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        return threadSeed(0,
                supplyA.getNext(input), (a, s1) -> threadSeed(1, supplyB.getNext(s1),
                        (b, s2) -> threadSeed(2, supplyC.getNext(s2),
                                (c, s3) -> threadSeed(3, supplyD.getNext(s3),
                                        (d, s4) -> threadSeed(4, supplyE.getNext(s4),
                                                (e, s5) -> threadSeed(5, supplyF.getNext(s5),
                                                        (f, s6) -> GeneratorOutput.success(s6, fn.apply(a, b, c, d, e, f))))))));
    }
}
