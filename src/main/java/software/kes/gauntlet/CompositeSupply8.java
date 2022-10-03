package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn8;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;
import static software.kes.gauntlet.SupplyTree.composite;

final class CompositeSupply8<A, B, C, D, E, F, G, H, Out> implements Supply<Out> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Supply<C> supplyC;
    private final Supply<D> supplyD;
    private final Supply<E> supplyE;
    private final Supply<F> supplyF;
    private final Supply<G> supplyG;
    private final Supply<H> supplyH;
    private final Fn8<A, B, C, D, E, F, G, H, Out> fn;

    CompositeSupply8(Supply<A> supplyA, Supply<B> supplyB, Supply<C> supplyC, Supply<D> supplyD, Supply<E> supplyE, Supply<F> supplyF, Supply<G> supplyG, Supply<H> supplyH, Fn8<A, B, C, D, E, F, G, H, Out> fn) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.supplyC = supplyC;
        this.supplyD = supplyD;
        this.supplyE = supplyE;
        this.supplyF = supplyF;
        this.supplyG = supplyG;
        this.supplyH = supplyH;
        this.fn = fn;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(supplyA.getSupplyTree(), supplyB.getSupplyTree(), supplyC.getSupplyTree(), supplyD.getSupplyTree(),
                supplyE.getSupplyTree(), supplyF.getSupplyTree(), supplyG.getSupplyTree(), supplyH.getSupplyTree());
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
        GeneratorOutput<D> output4 = supplyD.getNext(output3.getNextState());
        if (output4.isFailure()) {
            return (GeneratorOutput<Out>) output4.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(supplyA.getSupplyTree(),
                            supplyB.getSupplyTree(),
                            supplyC.getSupplyTree(),
                            st)))
                    .fmap(upcast());
        }
        GeneratorOutput<E> output5 = supplyE.getNext(output4.getNextState());
        if (output5.isFailure()) {
            return (GeneratorOutput<Out>) output5.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(supplyA.getSupplyTree(),
                            supplyB.getSupplyTree(),
                            supplyC.getSupplyTree(),
                            supplyD.getSupplyTree(),
                            st)))
                    .fmap(upcast());
        }
        GeneratorOutput<F> output6 = supplyF.getNext(output5.getNextState());
        if (output6.isFailure()) {
            return (GeneratorOutput<Out>) output6.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(supplyA.getSupplyTree(),
                            supplyB.getSupplyTree(),
                            supplyC.getSupplyTree(),
                            supplyD.getSupplyTree(),
                            supplyE.getSupplyTree(),
                            st)))
                    .fmap(upcast());
        }
        GeneratorOutput<G> output7 = supplyG.getNext(output6.getNextState());
        if (output7.isFailure()) {
            return (GeneratorOutput<Out>) output7.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(supplyA.getSupplyTree(),
                            supplyB.getSupplyTree(),
                            supplyC.getSupplyTree(),
                            supplyD.getSupplyTree(),
                            supplyE.getSupplyTree(),
                            supplyF.getSupplyTree(),
                            st)))
                    .fmap(upcast());
        }
        GeneratorOutput<H> output8 = supplyH.getNext(output7.getNextState());
        if (output8.isFailure()) {
            return (GeneratorOutput<Out>) output8.mapFailure(sf -> sf.modifySupplyTree(st ->
                    composite(supplyA.getSupplyTree(),
                            supplyB.getSupplyTree(),
                            supplyC.getSupplyTree(),
                            supplyD.getSupplyTree(),
                            supplyE.getSupplyTree(),
                            supplyF.getSupplyTree(),
                            supplyG.getSupplyTree(),
                            st)))
                    .fmap(upcast());
        }
        return GeneratorOutput.success(output8.getNextState(),
                fn.apply(output1.getSuccessOrThrow(), output2.getSuccessOrThrow(), output3.getSuccessOrThrow(),
                        output4.getSuccessOrThrow(), output5.getSuccessOrThrow(), output6.getSuccessOrThrow(),
                        output7.getSuccessOrThrow(), output8.getSuccessOrThrow()));
    }
}
