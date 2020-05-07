package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class ChoiceSupply2<A, B> implements Supply<Choice2<A, B>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Generate<Choice2<Unit, Unit>> generateWhich;

    ChoiceSupply2(Supply<A> supplyA, Supply<B> supplyB, Generate<Choice2<Unit, Unit>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.generateWhich = generateWhich;
    }

    @Override
    public GeneratorOutput<Choice2<A, B>> getNext(Seed input) {
        Result<? extends Seed, Choice2<Unit, Unit>> r1 = generateWhich.apply(input);
        return r1.getValue()
                .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice2::a),
                        __ -> supplyB.getNext(r1.getNextState()).fmap(Choice2::b));
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree());
    }
}
