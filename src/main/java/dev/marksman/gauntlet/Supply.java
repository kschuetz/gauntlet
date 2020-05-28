package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.kraftwerk.Seed;

import static dev.marksman.gauntlet.MappedSupply.mappedSupply;

public interface Supply<A> extends Functor<A, Supply<?>> {
    GeneratorOutput<A> getNext(Seed input);

    SupplyTree getSupplyTree();

    @Override
    default <B> Supply<B> fmap(Fn1<? super A, ? extends B> fn) {
        return mappedSupply(fn, this);
    }

}
