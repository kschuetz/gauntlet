package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.SupplyFailure.supplyFailure;
import static dev.marksman.gauntlet.SupplyTree.failedFilter;

final class FilteredValueSupplier<A> implements ValueSupplier<A> {
    private final ValueSupplier<A> underlying;
    private final Fn1<? super A, Boolean> filter;
    private final int maxDiscards;

    FilteredValueSupplier(ValueSupplier<A> underlying, Fn1<? super A, Boolean> filter, int maxDiscards) {
        this.underlying = underlying;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public SupplyTree getSupplyTree() {
        return SupplyTree.filter(underlying.getSupplyTree());
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

        return GeneratorOutput.failure(state, supplyFailure(failedFilter(maxDiscards, underlying.getSupplyTree())));
    }

    // TODO: use types to build a path rather than using labels

    // TODO: redesign ValueSupplier

    // TODO: replace Ascription with a tree representing pipeline and failure points

    // TODO: consider adding FilterGenerator to kraftwerk
}
