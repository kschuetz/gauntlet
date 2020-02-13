package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.MappedValueSupplier.mappedValueSupplier;

public interface ValueSupplier<A> extends Functor<A, ValueSupplier<?>> {

    GeneratorOutput<A> getNext(Seed input);

    @Override
    default <B> ValueSupplier<B> fmap(Fn1<? super A, ? extends B> fn) {
        return mappedValueSupplier(fn, this);
    }
}
