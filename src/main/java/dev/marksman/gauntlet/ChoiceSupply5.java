package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class ChoiceSupply5<A, B, C, D, E> implements Supply<Choice5<A, B, C, D, E>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Supply<E> supplyE;
    private final Fn1<Seed, Result<? extends Seed, Choice5<Unit, Unit, Unit, Unit, Unit>>> generateWhich;

    ChoiceSupply5(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD, Supply<E> supplyE,
                  Fn1<Seed, Result<? extends Seed, Choice5<Unit, Unit, Unit, Unit, Unit>>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.supplyE = supplyE;
        this.generateWhich = generateWhich;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Choice5<A, B, C, D, E>> getNext(Seed input) {
        Result<? extends Seed, Choice5<Unit, Unit, Unit, Unit, Unit>> r1 = generateWhich.apply(input);
        return r1.getValue()
                .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice5::a),
                        __ -> supplyB.getNext(r1.getNextState()).fmap(Choice5::b),
                        __ -> supplyC.getNext(r1.getNextState()).fmap(Choice5::c),
                        __ -> supplyD.getNext(r1.getNextState()).fmap(Choice5::d),
                        __ -> supplyE.getNext(r1.getNextState()).fmap(Choice5::e));
    }
}
