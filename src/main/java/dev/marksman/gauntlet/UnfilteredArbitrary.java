package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Parameters;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.gauntlet.FilteredArbitrary.filteredArbitrary;
import static dev.marksman.gauntlet.util.FilterChain.filterChain;

final class UnfilteredArbitrary<A> implements Arbitrary<A> {
    private final Generator<A> generator;
    private final ImmutableFiniteIterable<Fn1<Parameters, Parameters>> parameterTransforms;
    private final Maybe<Shrink<A>> shrink;
    private final Fn1<A, String> prettyPrinter;
    private final int maxDiscards;

    private UnfilteredArbitrary(Generator<A> generator, ImmutableFiniteIterable<Fn1<Parameters, Parameters>> parameterTransforms, Maybe<Shrink<A>> shrink, Fn1<A, String> prettyPrinter, int maxDiscards) {
        this.generator = generator;
        this.parameterTransforms = parameterTransforms;
        this.shrink = shrink;
        this.prettyPrinter = prettyPrinter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public ValueSupplier<A> prepare(Parameters parameters) {
        Parameters transformedParameters = parameterTransforms.foldLeft((acc, f) -> f.apply(acc), parameters);
        return new UnfilteredValueSupplier<>(generator.prepare(transformedParameters));
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
        return new UnfilteredArbitrary<>(generator, parameterTransforms, just(shrink), prettyPrinter, maxDiscards);
    }

    @Override
    public Arbitrary<A> withNoShrink() {
        return shrink.match(__ -> this,
                __ -> new UnfilteredArbitrary<>(generator, parameterTransforms, nothing(), prettyPrinter, maxDiscards));
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
            return new UnfilteredArbitrary<>(generator, parameterTransforms, shrink, prettyPrinter, maxDiscards);
        } else {
            return this;
        }
    }

    @Override
    public Arbitrary<A> withPrettyPrinter(Fn1<A, String> prettyPrinter) {
        return new UnfilteredArbitrary<>(generator, parameterTransforms, shrink, prettyPrinter, maxDiscards);
    }

    @Override
    public <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new UnfilteredArbitrary<>(generator.fmap(ab),
                parameterTransforms, shrink.fmap(s -> s.convert(ab, ba)),
                prettyPrinter.contraMap(ba),
                maxDiscards);

    }

    @Override
    public Arbitrary<A> modifyGeneratorParameters(Fn1<Parameters, Parameters> modifyFn) {
        return new UnfilteredArbitrary<>(generator, parameterTransforms.append(modifyFn), shrink, prettyPrinter, maxDiscards);
    }

    private String getLabel() {
        return generator.getLabel().orElseGet(generator::toString);
    }

    static <A> UnfilteredArbitrary<A> unfilteredArbitrary(Generator<A> generator) {
        return new UnfilteredArbitrary<>(generator, emptyImmutableFiniteIterable(), nothing(), Object::toString, Gauntlet.DEFAULT_MAX_DISCARDS);
    }
}
