package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Parameters;

interface WrappedGenerator<A> {
    ValueSupplier<A> prepare(Parameters parameters);

    WrappedGenerator<A> suchThat(Fn1<A, Boolean> predicate);

    WrappedGenerator<A> withMaxDiscards(int maxDiscards);

    <B> WrappedGenerator<B> convert(Fn1<A, B> ab, Fn1<B, A> ba);
}
