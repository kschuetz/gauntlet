package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Parameters;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.FilteredArbitrary.filteredArbitrary;
import static dev.marksman.gauntlet.util.FilterChain.filterChain;

final class UnfilteredArbitrary<A> implements Arbitrary<A> {
    private final Generator<A> generator;
    private final Maybe<Shrink<A>> shrink;
    private final Fn1<A, String> prettyPrinter;
    private final int maxDiscards;

    private UnfilteredArbitrary(Generator<A> generator, Maybe<Shrink<A>> shrink, Fn1<A, String> prettyPrinter, int maxDiscards) {
        this.generator = generator;
        this.shrink = shrink;
        this.prettyPrinter = prettyPrinter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public ValueSupplier<A> prepare(Parameters parameters) {
        return new UnfilteredValueSupplier<>(generator.prepare(parameters));
    }

    @Override
    public Maybe<Shrink<A>> getShrink() {
        return shrink;
    }

    @Override
    public Fn1<A, String> getPrettyPrinter() {
        return prettyPrinter;
    }

    @Override
    public Arbitrary<A> withShrink(Shrink<A> shrink) {
        return new UnfilteredArbitrary<>(generator, just(shrink), prettyPrinter, maxDiscards);
    }

    @Override
    public Arbitrary<A> withNoShrink() {
        return shrink.match(__ -> this,
                __ -> new UnfilteredArbitrary<>(generator, nothing(), prettyPrinter, maxDiscards));
    }

    @Override
    public Arbitrary<A> suchThat(Fn1<A, Boolean> predicate) {
        return filteredArbitrary(this, filterChain(predicate), maxDiscards, this::getLabel);
    }

    @Override
    public Arbitrary<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new UnfilteredArbitrary<>(generator, shrink, prettyPrinter, maxDiscards);
        } else {
            return this;
        }
    }

    @Override
    public Arbitrary<A> withPrettyPrinter(Fn1<A, String> prettyPrinter) {
        return new UnfilteredArbitrary<>(generator, shrink, prettyPrinter, maxDiscards);
    }

    @Override
    public <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new UnfilteredArbitrary<>(generator.fmap(ab),
                shrink.fmap(s -> s.convert(ab, ba)),
                prettyPrinter.contraMap(ba),
                maxDiscards);

    }

    private String getLabel() {
        return generator.getLabel().orElseGet(generator::toString);
    }

    static <A> UnfilteredArbitrary<A> unfilteredArbitrary(Generator<A> generator) {
        return new UnfilteredArbitrary<>(generator, nothing(), Object::toString, Gauntlet.DEFAULT_MAX_DISCARDS);
    }
}
