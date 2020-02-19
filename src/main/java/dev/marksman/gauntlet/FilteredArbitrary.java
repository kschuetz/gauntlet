package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.util.FilterChain;
import dev.marksman.kraftwerk.Parameters;

final class FilteredArbitrary<A> implements Arbitrary<A> {
    private final Arbitrary<A> underlying;
    private final FilterChain<A> filter;
    private final int maxDiscards;
    private final Fn0<String> labelSupplier;

    private FilteredArbitrary(Arbitrary<A> underlying, FilterChain<A> filter, int maxDiscards, Fn0<String> labelSupplier) {
        this.underlying = underlying;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
        this.labelSupplier = labelSupplier;
    }

    @Override
    public final ValueSupplier<A> prepare(Parameters parameters) {
        return new FilteredValueSupplier<>(underlying.prepare(parameters),
                filter, maxDiscards, labelSupplier);
    }

    @Override
    public Maybe<Shrink<A>> getShrink() {
        return underlying.getShrink();
    }

    @Override
    public Fn1<A, String> getPrettyPrinter() {
        return underlying.getPrettyPrinter();
    }

    @Override
    public Arbitrary<A> withShrink(Shrink<A> shrink) {
        return new FilteredArbitrary<>(underlying.withShrink(shrink), filter, maxDiscards, labelSupplier);
    }

    @Override
    public Arbitrary<A> withNoShrink() {
        return new FilteredArbitrary<>(underlying.withNoShrink(), filter, maxDiscards, labelSupplier);
    }

    @Override
    public Arbitrary<A> suchThat(Fn1<A, Boolean> predicate) {
        return new FilteredArbitrary<>(underlying, filter.add(predicate), maxDiscards, labelSupplier);
    }

    @Override
    public Arbitrary<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new FilteredArbitrary<>(underlying, filter, maxDiscards, labelSupplier);
        } else {
            return this;
        }
    }

    @Override
    public Arbitrary<A> withPrettyPrinter(Fn1<A, String> prettyPrinter) {
        return new FilteredArbitrary<>(underlying.withPrettyPrinter(prettyPrinter),
                filter, maxDiscards, labelSupplier);
    }

    @Override
    public <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new FilteredArbitrary<>(underlying.convert(ab, ba),
                filter.contraMap(ba),
                maxDiscards, labelSupplier);
    }

    @Override
    public Arbitrary<A> modifyGeneratorParameters(Fn1<Parameters, Parameters> modifyFn) {
        return new FilteredArbitrary<>(underlying.modifyGeneratorParameters(modifyFn),
                filter, maxDiscards, labelSupplier);
    }

    static <A> FilteredArbitrary<A> filteredArbitrary(Arbitrary<A> underlying,
                                                      FilterChain<A> filter,
                                                      int maxDiscards,
                                                      Fn0<String> labelSupplier) {
        return new FilteredArbitrary<>(underlying, filter, maxDiscards, labelSupplier);
    }
}
