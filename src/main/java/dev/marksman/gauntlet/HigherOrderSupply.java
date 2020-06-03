package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.SupplyTree.leaf;

final class HigherOrderSupply<A> implements Supply<A> {
    private final GeneratorParameters generatorParameters;
    private final Generate<Arbitrary<?>> arbitraryGenerator;
    private final Fn1<Arbitrary<?>, Arbitrary<? extends A>> transformFn;

    static <A> HigherOrderSupply<A> higherOrderSupply(GeneratorParameters generatorParameters,
                                                      Generate<Arbitrary<?>> arbitraryGenerator,
                                                      Fn1<Arbitrary<?>, Arbitrary<? extends A>> transformFn) {
        return new HigherOrderSupply<A>(generatorParameters, arbitraryGenerator, transformFn);
    }

    private HigherOrderSupply(GeneratorParameters generatorParameters,
                              Generate<Arbitrary<?>> arbitraryGenerator,
                              Fn1<Arbitrary<?>, Arbitrary<? extends A>> transformFn) {
        this.arbitraryGenerator = arbitraryGenerator;
        this.generatorParameters = generatorParameters;
        this.transformFn = transformFn;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        Result<? extends Seed, Arbitrary<?>> aResult = arbitraryGenerator.apply(input);
        Arbitrary<A> arbitrary = (Arbitrary<A>) transformFn.apply(aResult.getValue());
        Supply<A> innerSupply = arbitrary.createSupply(generatorParameters);
        return innerSupply.getNext(aResult.getNextState());
    }

    @Override
    public SupplyTree getSupplyTree() {
        return leaf("TODO");
    }
}
