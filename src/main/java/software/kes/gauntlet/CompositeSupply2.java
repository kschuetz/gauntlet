package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn2;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;

final class CompositeSupply2<A, B, Out> implements Supply<Out> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Fn2<A, B, Out> fn;

    CompositeSupply2(Supply<A> supplyA, Supply<B> supplyB, Fn2<A, B, Out> fn) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.fn = fn;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.composite(supplyA.getSupplyTree(), supplyB.getSupplyTree());
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        GeneratorOutput<A> output1 = supplyA.getNext(input);
        if (output1.isFailure()) {
            return (GeneratorOutput<Out>) output1.mapFailure(sf -> sf.modifySupplyTree(st -> SupplyTree.composite(st)))
                    .fmap(upcast());
        }
        GeneratorOutput<B> output2 = supplyB.getNext(output1.getNextState());
        if (output2.isFailure()) {
            return (GeneratorOutput<Out>) output2.mapFailure(sf -> sf.modifySupplyTree(st -> SupplyTree.composite(supplyA.getSupplyTree(), st)))
                    .fmap(upcast());
        }
        return GeneratorOutput.success(output2.getNextState(), fn.apply(output1.getSuccessOrThrow(), output2.getSuccessOrThrow()));
    }
}
