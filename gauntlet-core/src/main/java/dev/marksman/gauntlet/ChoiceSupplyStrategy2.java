package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class ChoiceSupplyStrategy2<A, B> implements SupplyStrategy<Choice2<A, B>> {
    private final SupplyStrategy<A> supplyA;
    private final SupplyStrategy<B> supplyB;
    private final Generate<Choice2<Unit, Unit>> generateWhich;

    ChoiceSupplyStrategy2(SupplyStrategy<A> supplyA, SupplyStrategy<B> supplyB, Generate<Choice2<Unit, Unit>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.generateWhich = generateWhich;
    }

    @Override
    public StatefulSupply<Choice2<A, B>> createSupply() {
        return new ChoiceSupply2(supplyA.createSupply(), supplyB.createSupply());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree());
    }

    class ChoiceSupply2 implements StatefulSupply<Choice2<A, B>> {
        private final StatefulSupply<A> supplyA;
        private final StatefulSupply<B> supplyB;

        ChoiceSupply2(StatefulSupply<A> supplyA, StatefulSupply<B> supplyB) {
            this.supplyA = supplyA;
            this.supplyB = supplyB;
        }

        @Override
        public GeneratorOutput<Choice2<A, B>> getNext(Seed input) {
            Result<? extends Seed, Choice2<Unit, Unit>> r1 = generateWhich.apply(input);
            return r1.getValue()
                    .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice2::a),
                            __ -> supplyB.getNext(r1.getNextState()).fmap(Choice2::b));
        }
    }
}
