package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class ChoiceSupply6<A, B, C, D, E, F> implements Supply<Choice6<A, B, C, D, E, F>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Supply<E> supplyE;
    private final Supply<F> supplyF;
    private final Fn1<Seed, Result<? extends Seed, Choice6<Unit, Unit, Unit, Unit, Unit, Unit>>> generateWhich;

    ChoiceSupply6(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD, Supply<E> supplyE, Supply<F> supplyF,
                  Fn1<Seed, Result<? extends Seed, Choice6<Unit, Unit, Unit, Unit, Unit, Unit>>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.supplyE = supplyE;
        this.supplyF = supplyF;
        this.generateWhich = generateWhich;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Choice6<A, B, C, D, E, F>> getNext(Seed input) {
        Result<? extends Seed, Choice6<Unit, Unit, Unit, Unit, Unit, Unit>> r1 = generateWhich.apply(input);
        return r1.getValue()
                .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice6::a),
                        __ -> supplyB.getNext(r1.getNextState()).fmap(Choice6::b),
                        __ -> supplyC.getNext(r1.getNextState()).fmap(Choice6::c),
                        __ -> supplyD.getNext(r1.getNextState()).fmap(Choice6::d),
                        __ -> supplyE.getNext(r1.getNextState()).fmap(Choice6::e),
                        __ -> supplyF.getNext(r1.getNextState()).fmap(Choice6::f));
    }
}
