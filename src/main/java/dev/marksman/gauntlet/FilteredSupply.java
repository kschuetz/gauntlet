package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.SupplyFailure.supplyFailure;

final class FilteredSupply<A> implements Supply<A> {
    private final Supply<A> underlying;
    private final Fn1<? super A, Boolean> filter;
    private final int maxDiscards;

    FilteredSupply(Supply<A> underlying, Fn1<? super A, Boolean> filter, int maxDiscards) {
        this.underlying = underlying;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return underlying.getSupplyTree();
    }

    @Override
    public GeneratorOutput<A> getNext(Seed input) {
        int discardsRemaining = maxDiscards;
        Seed state = input;
        while (discardsRemaining >= 0) {
            GeneratorOutput<A> current = underlying.getNext(state);
            if (current.isFailure()) {
                return current;
            }

            A value = current.getValue().orThrow(AssertionError::new);

            if (filter.apply(value)) {
                return current;
            } else {
                discardsRemaining -= 1;
                state = current.getNextState();
            }
        }

        return GeneratorOutput.failure(state, supplyFailure(maxDiscards, getSupplyTree()));
    }
}
