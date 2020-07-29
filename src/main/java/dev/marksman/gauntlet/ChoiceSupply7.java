package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

final class ChoiceSupply7<A, B, C, D, E, F, G> implements Supply<Choice7<A, B, C, D, E, F, G>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Supply<E> supplyE;
    private final Supply<F> supplyF;
    private final Supply<G> supplyG;
    private final Fn1<Seed, Result<? extends Seed, Choice7<Unit, Unit, Unit, Unit, Unit, Unit, Unit>>> generateWhich;

    ChoiceSupply7(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD, Supply<E> supplyE, Supply<F> supplyF, Supply<G> supplyG,
                  Fn1<Seed, Result<? extends Seed, Choice7<Unit, Unit, Unit, Unit, Unit, Unit, Unit>>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.supplyE = supplyE;
        this.supplyF = supplyF;
        this.supplyG = supplyG;
        this.generateWhich = generateWhich;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Choice7<A, B, C, D, E, F, G>> getNext(Seed input) {
        Result<? extends Seed, Choice7<Unit, Unit, Unit, Unit, Unit, Unit, Unit>> r1 = generateWhich.apply(input);
        return r1.getValue()
                .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice7::a),
                        __ -> supplyB.getNext(r1.getNextState()).fmap(Choice7::b),
                        __ -> supplyC.getNext(r1.getNextState()).fmap(Choice7::c),
                        __ -> supplyD.getNext(r1.getNextState()).fmap(Choice7::d),
                        __ -> supplyE.getNext(r1.getNextState()).fmap(Choice7::e),
                        __ -> supplyF.getNext(r1.getNextState()).fmap(Choice7::f),
                        __ -> supplyG.getNext(r1.getNextState()).fmap(Choice7::g));
    }
}
