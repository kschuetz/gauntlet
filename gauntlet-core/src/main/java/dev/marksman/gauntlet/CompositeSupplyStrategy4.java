package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.CompositeSupplyStrategy2.threadSeed;
import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupplyStrategy4<A, B, C, D, Out> implements SupplyStrategy<Out> {
    private final SupplyStrategy<A> strategyA;
    private final SupplyStrategy<B> strategyB;
    private final SupplyStrategy<C> strategyC;
    private final SupplyStrategy<D> strategyD;
    private final Fn4<A, B, C, D, Out> fn;

    CompositeSupplyStrategy4(SupplyStrategy<A> strategyA, SupplyStrategy<B> strategyB, SupplyStrategy<C> strategyC, SupplyStrategy<D> strategyD, Fn4<A, B, C, D, Out> fn) {
        this.strategyA = strategyA;
        this.strategyB = strategyB;
        this.strategyC = strategyC;
        this.strategyD = strategyD;
        this.fn = fn;
    }

    @Override
    public StatefulSupply<Out> createSupply() {
        return new CompositeSupply4(strategyA.createSupply(), strategyB.createSupply(), strategyC.createSupply(), strategyD.createSupply());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(strategyA.getSupplyTree(), strategyB.getSupplyTree(), strategyC.getSupplyTree(), strategyD.getSupplyTree());
    }

    class CompositeSupply4 implements StatefulSupply<Out> {
        private final StatefulSupply<A> supplyA;
        private final StatefulSupply<B> supplyB;
        private final StatefulSupply<C> supplyC;
        private final StatefulSupply<D> supplyD;

        CompositeSupply4(StatefulSupply<A> supplyA, StatefulSupply<B> supplyB, StatefulSupply<C> supplyC, StatefulSupply<D> supplyD) {
            this.supplyA = supplyA;
            this.supplyB = supplyB;
            this.supplyC = supplyC;
            this.supplyD = supplyD;
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
}
