package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Seed;

final class FilteredValueSupplier<A> implements ValueSupplier<A> {
    private final ValueSupplier<A> underlying;
    private final Fn1<A, Boolean> filter;
    private final int maxDiscards;
    private final Fn0<String> labelSupplier;

    FilteredValueSupplier(ValueSupplier<A> underlying, Fn1<A, Boolean> filter, int maxDiscards, Fn0<String> labelSupplier) {
        this.underlying = underlying;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
        this.labelSupplier = labelSupplier;
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

        return GeneratorOutput.failure(state, SupplyFailure.supplyFailure(null));
    }

    // TODO: use types to build a path rather than using labels

    // TODO: redesign ValueSupplier

    // TODO: replace Ascription with a tree representing pipeline and failure points
}
