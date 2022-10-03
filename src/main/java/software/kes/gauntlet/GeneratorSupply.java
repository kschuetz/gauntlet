package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

final class GeneratorSupply<A> implements Supply<A> {
    private final Fn1<Seed, Result<? extends Seed, A>> generateFn;
    private final Fn0<String> labelSupplier;

    GeneratorSupply(Fn1<Seed, Result<? extends Seed, A>> generateFn, Fn0<String> labelSupplier) {
        this.generateFn = generateFn;
        this.labelSupplier = labelSupplier;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.leaf(labelSupplier.apply());
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        return GeneratorOutput.success(generateFn.apply(input));
    }
}
