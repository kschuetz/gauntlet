package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.collectionviews.Vector;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

import java.util.TreeMap;

final class FrequencyMapSupply<A> implements Supply<A> {
    private final Fn1<Seed, Result<? extends Seed, Long>> generateWhich;
    private final TreeMap<Long, Supply<A>> tree;

    FrequencyMapSupply(Fn1<Seed, Result<? extends Seed, Long>> generateWhich, TreeMap<Long, Supply<A>> tree) {
        this.generateWhich = generateWhich;
        this.tree = tree;
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        Result<? extends Seed, Long> r1 = generateWhich.apply(input);
        long n = r1.getValue();
        Supply<A> source = tree.ceilingEntry(1 + n).getValue();
        return source.getNext(r1.getNextState());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(Vector.copyFrom(tree.values()).fmap(Supply::getSupplyTree));
    }
}
