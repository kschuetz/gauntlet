package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.GeneratorParameters;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

final class HigherOrderSupply<A> implements Supply<A> {
    private final GeneratorParameters generatorParameters;
    private final Fn1<Seed, Result<? extends Seed, Object>> arbitraryGenerator;
    private final Fn1<Object, Arbitrary<? extends A>> transformFn;

    static <A> HigherOrderSupply<A> higherOrderSupply(GeneratorParameters generatorParameters,
                                                      Fn1<Seed, Result<? extends Seed, Object>> arbitraryGenerator,
                                                      Fn1<Object, Arbitrary<? extends A>> transformFn) {
        return new HigherOrderSupply<>(generatorParameters, arbitraryGenerator, transformFn);
    }

    private HigherOrderSupply(GeneratorParameters generatorParameters,
                              Fn1<Seed, Result<? extends Seed, Object>> arbitraryGenerator,
                              Fn1<Object, Arbitrary<? extends A>> transformFn) {
        this.arbitraryGenerator = arbitraryGenerator;
        this.generatorParameters = generatorParameters;
        this.transformFn = transformFn;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        Result<? extends Seed, Object> aResult = arbitraryGenerator.apply(input);
        Arbitrary<A> arbitrary = (Arbitrary<A>) transformFn.apply(aResult.getValue());
        Supply<A> innerSupply = arbitrary.createSupply(generatorParameters);
        return innerSupply.getNext(aResult.getNextState());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.leaf("HigherOrderSupply");
    }
}
