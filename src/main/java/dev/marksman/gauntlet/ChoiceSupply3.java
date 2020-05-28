package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class ChoiceSupply3<A, B, C> implements Supply<Choice3<A, B, C>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Generate<Choice3<Unit, Unit, Unit>> generateWhich;

    ChoiceSupply3(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC,
                  Generate<Choice3<Unit, Unit, Unit>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.generateWhich = generateWhich;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
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
