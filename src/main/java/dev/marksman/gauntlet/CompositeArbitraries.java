package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Parameters;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;

final class CompositeArbitraries {

    static <A, B> Arbitrary<Tuple2<A, B>> combine2(Arbitrary<A> a,
                                                   Arbitrary<B> b) {

        Fn2<A, B, Tuple2<A, B>> toFn = HList::tuple;
        Fn1<Tuple2<A, B>, Product2<A, B>> fromFn = t -> t;

        Maybe<Shrink<Tuple2<A, B>>> newShrink = combineShrinks(a.getShrink(), b.getShrink(), toFn, fromFn);
        Fn1<Parameters, ValueSupplier<Tuple2<A, B>>> prepare = parameters -> new CompositeValueSupplier2<>(a.prepare(parameters),
                b.prepare(parameters),
                toFn);

        return concreteArbitrary(prepare,
                newShrink,
                combinePrettyPrinters(a.getPrettyPrinter(), b.getPrettyPrinter()));
    }


    private static <A, B, Out> Maybe<Shrink<Out>> combineShrinks(Maybe<Shrink<A>> shrinkA,
                                                                 Maybe<Shrink<B>> shrinkB,
                                                                 Fn2<A, B, Tuple2<A, B>> toFn,
                                                                 Fn1<Tuple2<A, B>, Product2<A, B>> fromFn) {
        return nothing();
    }

    private static <A, B> Fn1<Tuple2<A, B>, String> combinePrettyPrinters(Fn1<A, String> ppA,
                                                                          Fn1<B, String> ppB) {
        return Object::toString;   // TODO
    }

}
