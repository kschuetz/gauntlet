package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class ChoiceSupplyStrategy3<A, B, C> implements SupplyStrategy<Choice3<A, B, C>> {
    private final SupplyStrategy<A> supplyA;
    private final SupplyStrategy<B> supplyB;
    private final SupplyStrategy<C> supplyC;
    private final Generate<Choice3<Unit, Unit, Unit>> generateWhich;

    ChoiceSupplyStrategy3(SupplyStrategy<A> supplyA, SupplyStrategy<B> supplyB, SupplyStrategy<C> supplyC,
                          Generate<Choice3<Unit, Unit, Unit>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.generateWhich = generateWhich;
    }

    @Override
    public StatefulSupply<Choice3<A, B, C>> createSupply() {
        return new ChoiceSupply3(supplyA.createSupply(), supplyB.createSupply(), supplyC.createSupply());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    class ChoiceSupply3 implements StatefulSupply<Choice3<A, B, C>> {
        private final StatefulSupply<A> supplyA;
        private final StatefulSupply<B> supplyB;
        private final StatefulSupply<C> supplyC;

        ChoiceSupply3(StatefulSupply<A> supplyA, StatefulSupply<B> supplyB, StatefulSupply<C> supplyC) {
            this.supplyA = supplyA;
            this.supplyB = supplyB;
            this.supplyC = supplyC;
        }

        @Override
        public GeneratorOutput<Choice3<A, B, C>> getNext(Seed input) {
            Result<? extends Seed, Choice3<Unit, Unit, Unit>> r1 = generateWhich.apply(input);
            return r1.getValue()
                    .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice3::a),
                            __ -> supplyB.getNext(r1.getNextState()).fmap(Choice3::b),
                            __ -> supplyC.getNext(r1.getNextState()).fmap(Choice3::c));
        }

    }
}
