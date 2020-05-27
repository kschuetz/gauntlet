package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

import static dev.marksman.gauntlet.MappedSupplyStrategy.mappedSupply;

public interface SupplyStrategy<A> extends Functor<A, SupplyStrategy<?>> {
//    GeneratorOutput<A> getNext(Seed input);

    StatefulSupply<A> createSupply();

    SupplyTree getSupplyTree();

    @Override
    default <B> SupplyStrategy<B> fmap(Fn1<? super A, ? extends B> fn) {
        return mappedSupply(fn, this);
    }

}
