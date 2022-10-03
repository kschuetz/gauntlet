package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

final class ChoiceSupply8<A, B, C, D, E, F, G, H> implements Supply<Choice8<A, B, C, D, E, F, G, H>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Supply<E> supplyE;
    private final Supply<F> supplyF;
    private final Supply<G> supplyG;
    private final Supply<H> supplyH;
    private final Fn1<Seed, Result<? extends Seed, Choice8<Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit>>> generateWhich;

    ChoiceSupply8(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD, Supply<E> supplyE, Supply<F> supplyF, Supply<G> supplyG, Supply<H> supplyH,
                  Fn1<Seed, Result<? extends Seed, Choice8<Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit>>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.supplyE = supplyE;
        this.supplyF = supplyF;
        this.supplyG = supplyG;
        this.supplyH = supplyH;
        this.generateWhich = generateWhich;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Choice8<A, B, C, D, E, F, G, H>> getNext(Seed input) {
        Result<? extends Seed, Choice8<Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit>> r1 = generateWhich.apply(input);
        return r1.getValue()
                .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice8::a),
                        __ -> supplyB.getNext(r1.getNextState()).fmap(Choice8::b),
                        __ -> supplyC.getNext(r1.getNextState()).fmap(Choice8::c),
                        __ -> supplyD.getNext(r1.getNextState()).fmap(Choice8::d),
                        __ -> supplyE.getNext(r1.getNextState()).fmap(Choice8::e),
                        __ -> supplyF.getNext(r1.getNextState()).fmap(Choice8::f),
                        __ -> supplyG.getNext(r1.getNextState()).fmap(Choice8::g),
                        __ -> supplyH.getNext(r1.getNextState()).fmap(Choice8::h));
    }
}
