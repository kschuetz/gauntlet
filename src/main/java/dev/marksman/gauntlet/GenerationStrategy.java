package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;

public interface GenerationStrategy<A> {
    Generator<A> getGenerator();

    Fn1<A, Boolean> getFilter();

    Maybe<Shrink<A>> getShrink();

    int getMaxDiscardCount();

    Fn1<A, String> getPrettyPrinter();

    GenerationStrategy<A> withShrink(Shrink<A> shrink);

    GenerationStrategy<A> withNoShrink();

    GenerationStrategy<A> suchThat(Fn1<A, Boolean> predicate);

    GenerationStrategy<A> withMaxDiscards(int maxDiscards);

    GenerationStrategy<A> withPrettyPrinter(Fn1<A, String> prettyPrinter);

    static <A> GenerationStrategy<A> fromGenerator(Generator<A> generator) {
        return null;
    }

}
