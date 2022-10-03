package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

final class FlattenedSupply<A> implements Supply<A> {
    private final Supply<Maybe<? extends A>> underlying;
    private final int maxDiscards;

    static <A> FlattenedSupply<A> flattenedSupply(Supply<Maybe<? extends A>> underlying, int maxDiscards) {
        return new FlattenedSupply<>(underlying, maxDiscards);
    }

    private FlattenedSupply(Supply<Maybe<? extends A>> underlying, int maxDiscards) {
        this.underlying = underlying;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        int discardsRemaining = maxDiscards;
        Seed state = input;
        while (discardsRemaining >= 0) {
            GeneratorOutput<Maybe<? extends A>> current = underlying.getNext(state);
            if (current.isFailure()) {
                SupplyFailure supplyFailure = current.getFailureOrThrow();
                return GeneratorOutput.generatorOutput(current.getNextState(), left(supplyFailure.modifySupplyTree(SupplyTree::filter)));
            }

            Maybe<? extends A> value = current.getValue().orThrow(AssertionError::new);

            if (value == nothing()) {
                discardsRemaining -= 1;
                state = current.getNextState();
            } else {
                return GeneratorOutput.generatorOutput(current.getNextState(), right(value.orElseThrow(AssertionError::new)));
            }
        }

        return GeneratorOutput.failure(state, SupplyFailure.supplyFailure(maxDiscards, SupplyTree.exhausted(underlying.getSupplyTree(), maxDiscards)));
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.filter(underlying.getSupplyTree());
    }
}
