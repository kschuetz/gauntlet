package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

final class ChoiceSupply4<A, B, C, D> implements Supply<Choice4<A, B, C, D>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Fn1<Seed, Result<? extends Seed, Choice4<Unit, Unit, Unit, Unit>>> generateWhich;

    ChoiceSupply4(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD,
                  Fn1<Seed, Result<? extends Seed, Choice4<Unit, Unit, Unit, Unit>>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.generateWhich = generateWhich;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Choice4<A, B, C, D>> getNext(Seed input) {
        Result<? extends Seed, Choice4<Unit, Unit, Unit, Unit>> r1 = generateWhich.apply(input);
        return r1.getValue()
                .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice4::a),
                        __ -> supplyB.getNext(r1.getNextState()).fmap(Choice4::b),
                        __ -> supplyC.getNext(r1.getNextState()).fmap(Choice4::c),
                        __ -> supplyD.getNext(r1.getNextState()).fmap(Choice4::d));
    }
}
