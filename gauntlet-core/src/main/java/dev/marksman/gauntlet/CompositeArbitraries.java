package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import dev.marksman.gauntlet.shrink.Shrink;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProducts.shrink2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProducts.shrink3;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProducts.shrink4;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProducts.shrink5;

final class CompositeArbitraries {

    static <A, B> Arbitrary<Tuple2<A, B>> combine(Arbitrary<A> a,
                                                  Arbitrary<B> b) {

        Fn2<A, B, Tuple2<A, B>> toFn = HList::tuple;

        return concreteArbitrary(parameters -> new CompositeSupply2<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        toFn),
                combineShrinks(a.getShrink(), b.getShrink(), toFn),
                PrettyPrinting.<A, B>product2PrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter()));
    }

    static <A, B, C> Arbitrary<Tuple3<A, B, C>> combine(Arbitrary<A> a,
                                                        Arbitrary<B> b,
                                                        Arbitrary<C> c) {

        Fn3<A, B, C, Tuple3<A, B, C>> toFn = HList::tuple;

        return concreteArbitrary(parameters -> new CompositeSupply3<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        toFn),
                combineShrinks(a.getShrink(), b.getShrink(), c.getShrink(), toFn),
                PrettyPrinting.<A, B, C>product3PrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(), c.getPrettyPrinter()));
    }

    static <A, B, C, D> Arbitrary<Tuple4<A, B, C, D>> combine(Arbitrary<A> a,
                                                              Arbitrary<B> b,
                                                              Arbitrary<C> c,
                                                              Arbitrary<D> d) {

        Fn4<A, B, C, D, Tuple4<A, B, C, D>> toFn = HList::tuple;

        return concreteArbitrary(parameters -> new CompositeSupply4<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        d.createSupply(parameters),
                        toFn),
                combineShrinks(a.getShrink(), b.getShrink(), c.getShrink(), d.getShrink(), toFn),
                PrettyPrinting.<A, B, C, D>product4PrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(),
                        c.getPrettyPrinter(), d.getPrettyPrinter()));
    }

    static <A, B, C, D, E> Arbitrary<Tuple5<A, B, C, D, E>> combine(Arbitrary<A> a,
                                                                    Arbitrary<B> b,
                                                                    Arbitrary<C> c,
                                                                    Arbitrary<D> d,
                                                                    Arbitrary<E> e) {

        Fn5<A, B, C, D, E, Tuple5<A, B, C, D, E>> toFn = HList::tuple;

        return concreteArbitrary(parameters -> new CompositeSupply5<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        d.createSupply(parameters),
                        e.createSupply(parameters),
                        toFn),
                combineShrinks(a.getShrink(), b.getShrink(), c.getShrink(), d.getShrink(), e.getShrink(), toFn),
                PrettyPrinting.<A, B, C, D, E>product5PrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(),
                        c.getPrettyPrinter(), d.getPrettyPrinter(), e.getPrettyPrinter()));
    }


    private static <A, B> Maybe<Shrink<Tuple2<A, B>>> combineShrinks(Maybe<Shrink<A>> shrinkA,
                                                                     Maybe<Shrink<B>> shrinkB,
                                                                     Fn2<A, B, Tuple2<A, B>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB)) {
            return nothing();
        } else {
            return just(shrink2(shrinkA.orElse(Shrink.none()),
                    shrinkB.orElse(Shrink.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C> Maybe<Shrink<Tuple3<A, B, C>>> combineShrinks(Maybe<Shrink<A>> shrinkA,
                                                                           Maybe<Shrink<B>> shrinkB,
                                                                           Maybe<Shrink<C>> shrinkC,
                                                                           Fn3<A, B, C, Tuple3<A, B, C>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC)) {
            return nothing();
        } else {
            return just(shrink3(shrinkA.orElse(Shrink.none()),
                    shrinkB.orElse(Shrink.none()),
                    shrinkC.orElse(Shrink.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C, D> Maybe<Shrink<Tuple4<A, B, C, D>>> combineShrinks(Maybe<Shrink<A>> shrinkA,
                                                                                 Maybe<Shrink<B>> shrinkB,
                                                                                 Maybe<Shrink<C>> shrinkC,
                                                                                 Maybe<Shrink<D>> shrinkD,
                                                                                 Fn4<A, B, C, D, Tuple4<A, B, C, D>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC) && isNothing(shrinkD)) {
            return nothing();
        } else {
            return just(shrink4(shrinkA.orElse(Shrink.none()),
                    shrinkB.orElse(Shrink.none()),
                    shrinkC.orElse(Shrink.none()),
                    shrinkD.orElse(Shrink.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C, D, E> Maybe<Shrink<Tuple5<A, B, C, D, E>>> combineShrinks(Maybe<Shrink<A>> shrinkA,
                                                                                       Maybe<Shrink<B>> shrinkB,
                                                                                       Maybe<Shrink<C>> shrinkC,
                                                                                       Maybe<Shrink<D>> shrinkD,
                                                                                       Maybe<Shrink<E>> shrinkE,
                                                                                       Fn5<A, B, C, D, E, Tuple5<A, B, C, D, E>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC) && isNothing(shrinkD) && isNothing(shrinkE)) {
            return nothing();
        } else {
            return just(shrink5(shrinkA.orElse(Shrink.none()),
                    shrinkB.orElse(Shrink.none()),
                    shrinkC.orElse(Shrink.none()),
                    shrinkD.orElse(Shrink.none()),
                    shrinkE.orElse(Shrink.none()),
                    toFn, t -> t));
        }
    }

    private static <A> boolean isNothing(Maybe<A> ma) {
        return ma.match(__ -> true, __ -> false);
    }

}
