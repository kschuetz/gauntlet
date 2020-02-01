package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;

import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;

public interface Arbitrary<A> {
    Generator<A> getGenerator();

    Fn1<A, Boolean> getFilter();

    Maybe<Shrink<A>> getShrink();

    int getMaxDiscards();

    Fn1<A, String> getPrettyPrinter();

    Arbitrary<A> withShrink(Shrink<A> shrink);

    Arbitrary<A> withNoShrink();

    Arbitrary<A> suchThat(Fn1<A, Boolean> predicate);

    Arbitrary<A> withMaxDiscards(int maxDiscards);

    Arbitrary<A> withPrettyPrinter(Fn1<A, String> prettyPrinter);

    <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba);

    default <B> Arbitrary<B> convert(Iso<A, A, B, B> iso) {
        return convert(view(iso), view(iso.mirror()));
    }

    static <A> Arbitrary<A> arbitrary(Generator<A> generator) {
        return concreteArbitrary(generator);
    }

}
