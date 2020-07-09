package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.kraftwerk.Seed;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;
import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupply3<A, B, C, Out> implements Supply<Out> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Fn3<A, B, C, Out> fn;

    CompositeSupply3(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Fn3<A, B, C, Out> fn) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.fn = fn;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree());
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        GeneratorOutput<A> output1 = supplyA.getNext(input);
        if (output1.isFailure()) {
            return (GeneratorOutput<Out>) output1.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(st)))
                    .fmap(upcast());
        }
        GeneratorOutput<B> output2 = supplyB.getNext(output1.getNextState());
        if (output2.isFailure()) {
            return (GeneratorOutput<Out>) output2.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(supplyA.getSupplyTree(),
                            st)))
                    .fmap(upcast());
        }
        GeneratorOutput<C> output3 = supplyC.getNext(output2.getNextState());
        if (output3.isFailure()) {
            return (GeneratorOutput<Out>) output3.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(supplyA.getSupplyTree(),
                            supplyB.getSupplyTree(),
                            st)))
                    .fmap(upcast());
        }
        return GeneratorOutput.success(output3.getNextState(),
                fn.apply(output1.getSuccessOrThrow(), output2.getSuccessOrThrow(), output3.getSuccessOrThrow()));
    }
}
