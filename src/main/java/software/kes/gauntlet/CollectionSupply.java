package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;
import software.kes.kraftwerk.aggregator.Aggregator;

final class CollectionSupply<A, Builder, Out> implements Supply<Out> {
    private final Supply<A> elementSupply;
    private final Fn1<Seed, Result<? extends Seed, Integer>> sizeGenerator;
    private final Aggregator<A, Builder, Out> aggregator;

    CollectionSupply(Supply<A> elementSupply, Fn1<Seed, Result<? extends Seed, Integer>> sizeGenerator, Aggregator<A, Builder, Out> aggregator) {
        this.elementSupply = elementSupply;
        this.sizeGenerator = sizeGenerator;
        this.aggregator = aggregator;
    }


    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.collection(elementSupply.getSupplyTree());
    }

    @Override
    public GeneratorOutput<Out> getNext(Seed input) {
        Result<? extends Seed, Integer> sizeResult = sizeGenerator.apply(input);
        Seed state = sizeResult.getNextState();
        int size = sizeResult.getValue();
        Builder builder = aggregator.builder();

        while (size > 0) {
            GeneratorOutput<A> current = elementSupply.getNext(state);
            state = current.getNextState();
            if (current.isFailure()) {
                return GeneratorOutput.failure(state, current.getValue().projectA().orElseThrow(AssertionError::new)
                        .modifySupplyTree(SupplyTree::collection));
            }
            builder = aggregator.add(builder, current.getValue().orThrow(AssertionError::new));
            size--;
        }

        Out output = aggregator.build(builder);
        return GeneratorOutput.success(state, output);
    }
}
