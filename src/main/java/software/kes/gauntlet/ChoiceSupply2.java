package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

final class ChoiceSupply2<A, B> implements Supply<Choice2<A, B>> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Fn1<Seed, Result<? extends Seed, Choice2<Unit, Unit>>> generateWhich;

    ChoiceSupply2(Supply<A> supplyA, Supply<B> supplyB, Fn1<Seed, Result<? extends Seed, Choice2<Unit, Unit>>> generateWhich) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.generateWhich = generateWhich;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Choice2<A, B>> getNext(Seed input) {
        Result<? extends Seed, Choice2<Unit, Unit>> r1 = generateWhich.apply(input);
        return r1.getValue()
                .match(__ -> supplyA.getNext(r1.getNextState()).fmap(Choice2::a),
                        __ -> supplyB.getNext(r1.getNextState()).fmap(Choice2::b));
    }
}
