package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.CompositeSupplyStrategy2.threadSeed;
import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupplyStrategy3<A, B, C, Out> implements SupplyStrategy<Out> {
    private final SupplyStrategy<A> strategyA;
    private final SupplyStrategy<B> strategyB;
    private final SupplyStrategy<C> strategyC;
    private final Fn3<A, B, C, Out> fn;

    CompositeSupplyStrategy3(SupplyStrategy<A> strategyA, SupplyStrategy<B> strategyB, SupplyStrategy<C> strategyC, Fn3<A, B, C, Out> fn) {
        this.strategyA = strategyA;
        this.strategyB = strategyB;
        this.strategyC = strategyC;
        this.fn = fn;
    }

    @Override
    public StatefulSupply<Out> createSupply() {
        return new CompositeSupply3(strategyA.createSupply(), strategyB.createSupply(), strategyC.createSupply());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(strategyA.getSupplyTree(), strategyB.getSupplyTree(), strategyC.getSupplyTree());
    }

    class CompositeSupply3 implements StatefulSupply<Out> {
        private final StatefulSupply<A> supplyA;
        private final StatefulSupply<B> supplyB;
        private final StatefulSupply<C> supplyC;

        CompositeSupply3(StatefulSupply<A> supplyA, StatefulSupply<B> supplyB, StatefulSupply<C> supplyC) {
            this.supplyA = supplyA;
            this.supplyB = supplyB;
            this.supplyC = supplyC;
        }

        @Override
        public GeneratorOutput<Out> getNext(Seed input) {
            return threadSeed(0,
                    supplyA.getNext(input), (a, s1) -> threadSeed(1, supplyB.getNext(s1),
                            (b, s2) -> threadSeed(2, supplyC.getNext(s2),
                                    (c, s3) -> GeneratorOutput.success(s3, fn.apply(a, b, c)))));
        }
    }
}
