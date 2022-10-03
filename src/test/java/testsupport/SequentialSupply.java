package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.gauntlet.GeneratorOutput;
import software.kes.gauntlet.Supply;
import software.kes.gauntlet.SupplyTree;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

import static software.kes.gauntlet.SupplyTree.leaf;

public class SequentialSupply<A> implements Supply<A> {
    private final Fn1<Seed, Result<? extends Seed, A>> generateFn;

    public static <A> SequentialSupply<A> sequentialSupply(Fn1<Long, A> selector) {
        return new SequentialSupply<>(selector);
    }

    private SequentialSupply(Fn1<Long, A> selector) {
        this.generateFn = seed -> {
            A result = selector.apply(seed.getSeedValue());
            return Result.result(seed.setNextSeedValue(seed.getSeedValue()), result);
        };
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        return GeneratorOutput.success(generateFn.apply(input));
    }

    @Override
    public SupplyTree getSupplyTree() {
        return leaf(getClass().getSimpleName());
    }
}
