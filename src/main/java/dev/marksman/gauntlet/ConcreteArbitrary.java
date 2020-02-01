package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.util.FilterChain;
import dev.marksman.kraftwerk.Generator;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.Gauntlet.DEFAULT_MAX_DISCARDS;
import static dev.marksman.gauntlet.util.FilterChain.filterChain;

@Value
class ConcreteArbitrary<A> implements Arbitrary<A> {
    private final Generator<A> generator;
    private final FilterChain<A> filter;
    private final Maybe<Shrink<A>> shrink;
    private final Fn1<A, String> prettyPrinter;
    private final int maxDiscards;

    @Override
    public Arbitrary<A> withShrink(Shrink<A> shrink) {
        return new ConcreteArbitrary<>(generator, filter, just(shrink), prettyPrinter, maxDiscards);
    }

    @Override
    public Arbitrary<A> withNoShrink() {
        return shrink.match(__ -> this,
                __ -> new ConcreteArbitrary<>(generator, filter, nothing(), prettyPrinter, maxDiscards));
    }

    @Override
    public Arbitrary<A> suchThat(Fn1<A, Boolean> predicate) {
        return new ConcreteArbitrary<>(generator, filter.add(predicate), shrink, prettyPrinter, maxDiscards);
    }

    @Override
    public Arbitrary<A> withMaxDiscards(int maxDiscards) {
        return maxDiscards != this.maxDiscards
                ? new ConcreteArbitrary<>(generator, filter, shrink, prettyPrinter, maxDiscards)
                : this;
    }

    @Override
    public Arbitrary<A> withPrettyPrinter(Fn1<A, String> prettyPrinter) {
        return new ConcreteArbitrary<>(generator, filter, shrink, prettyPrinter, maxDiscards);
    }

    @Override
    public <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new ConcreteArbitrary<>(generator.fmap(ab),
                filter.contraMap(ba),
                shrink.fmap(s -> s.convert(ab, ba)),
                prettyPrinter.contraMap(ba),
                maxDiscards);
    }

    static <A> ConcreteArbitrary<A> concreteArbitrary(Generator<A> generator) {
        return new ConcreteArbitrary<>(generator, filterChain(), nothing(), Object::toString, DEFAULT_MAX_DISCARDS);
    }
}
