package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.SupplyTree.composite;

final class CompositeSupply2<A, B, Out> implements Supply<Out> {
    private final Supply<A> supplyA;
    private final Supply<B> supplyB;
    private final Fn2<A, B, Out> fn;

    CompositeSupply2(Supply<A> supplyA, Supply<B> supplyB, Fn2<A, B, Out> fn) {
        this.supplyA = supplyA;
        this.supplyB = supplyB;
        this.fn = fn;
    }

    static <A, B> GeneratorOutput<B> threadSeed(int posIndex,
                                                GeneratorOutput<A> ra,
                                                Fn2<A, Seed, GeneratorOutput<B>> f) {
        return ra.getValue()
                .match(gf -> GeneratorOutput.failure(ra.getNextState(), gf),
                        a -> f.apply(a, ra.getNextState()));
    }

    private static String positionName(int posIndex) {
        return "position " + (posIndex + 1);
    }

    // TODO: find another way to build SupplyTree because this is going to be way too ugly

//    private GeneratorOutput<Out> getNext2(Seed input) {
//        GeneratorOutput<A> ra = vsA.getNext(input);
//        return ra.getValue()
//                .match(sf -> supplyFailure(SupplyTree.composite(sf.getTree(),
//                        vsB.getSupplyTree())),
//                        a -> {
//                            GeneratorOutput<B> rb = vsB.getNext(ra.getNextState());
//                            rb.getValue()
//                                    .match(sf -> GeneratorOutput.failure(rb.getNextState(),
//                                            supplyFailure(SupplyTree.composite(vsA.getSupplyTree(),
//                                            sf.getTree()))),
//                                            b -> GeneratorOutput.success(rb.getNextState(),
//                                                    fn.apply(a, b)));
//                            return match;
//                        });
//    }

    @Override
    public SupplyTree getSupplyTree() {
        return composite(supplyA.getSupplyTree(), supplyB.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        return threadSeed(0,
                supplyA.getNext(input), (a, s1) -> threadSeed(1, supplyB.getNext(s1),
                        (b, s2) -> GeneratorOutput.success(s2, fn.apply(a, b))));
    }
}
