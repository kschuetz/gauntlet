package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.gauntlet.util.FilterChain;
import dev.marksman.kraftwerk.Parameters;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

class OldCompositeArbitrary2<AA, AB, Out> implements Arbitrary<Out> {
    private final Arbitrary<AA> componentA;
    private final Arbitrary<AB> componentB;
    private final Maybe<Shrink<Out>> shrink;
    private final Fn1<Out, String> prettyPrinter;
    private final Fn1<Product2<AA, AB>, Out> toFn;
    private final Fn1<Out, Product2<AA, AB>> fromFn;
    private final FilterChain<Out> filter;
    private final int maxDiscards;

    OldCompositeArbitrary2(Arbitrary<AA> componentA, Arbitrary<AB> componentB, Maybe<Shrink<Out>> shrink, Fn1<Out, String> prettyPrinter, Fn1<Product2<AA, AB>, Out> toFn, Fn1<Out, Product2<AA, AB>> fromFn, FilterChain<Out> filter, int maxDiscards) {
        this.componentA = componentA;
        this.componentB = componentB;
        this.shrink = shrink;
        this.prettyPrinter = prettyPrinter;
        this.toFn = toFn;
        this.fromFn = fromFn;
        this.filter = filter;
        this.maxDiscards = maxDiscards;
    }

    @Override
    public ValueSupplier<Out> prepare(Parameters parameters) {
        return null;
    }

    @Override
    public Maybe<Shrink<Out>> getShrink() {
        return shrink;
    }

    @Override
    public Fn1<Out, String> getPrettyPrinter() {
        return prettyPrinter;
    }

    @Override
    public Arbitrary<Out> withShrink(Shrink<Out> shrink) {
        return new OldCompositeArbitrary2<>(componentA, componentB, just(shrink), prettyPrinter,
                toFn, fromFn, filter, maxDiscards);
    }

    @Override
    public Arbitrary<Out> withNoShrink() {
        return new OldCompositeArbitrary2<>(componentA, componentB, nothing(), prettyPrinter,
                toFn, fromFn, filter, maxDiscards);
    }

    @Override
    public Arbitrary<Out> suchThat(Fn1<Out, Boolean> predicate) {
        return new OldCompositeArbitrary2<>(componentA, componentB, shrink, prettyPrinter,
                toFn, fromFn, filter.add(predicate), maxDiscards);

    }

    @Override
    public Arbitrary<Out> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new OldCompositeArbitrary2<>(componentA, componentB, shrink, prettyPrinter,
                    toFn, fromFn, filter, maxDiscards);
        } else {
            return this;
        }
    }

    @Override
    public Arbitrary<Out> withPrettyPrinter(Fn1<Out, String> prettyPrinter) {
        return new OldCompositeArbitrary2<>(componentA, componentB, shrink, prettyPrinter,
                toFn, fromFn, filter, maxDiscards);
    }

    @Override
    public <C> Arbitrary<C> convert(Fn1<Out, C> ab, Fn1<C, Out> ba) {
        return null;
    }
}
