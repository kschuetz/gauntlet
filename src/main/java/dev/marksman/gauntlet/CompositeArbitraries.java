package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.gauntlet.shrink.Shrink;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProducts.shrink2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProducts.shrink3;

final class CompositeArbitraries {

    static <A, B> Arbitrary<Tuple2<A, B>> combine(Arbitrary<A> a,
                                                  Arbitrary<B> b) {

        Fn2<A, B, Tuple2<A, B>> toFn = HList::tuple;

        return concreteArbitrary(parameters -> new CompositeValueSupplier2<>(a.prepare(parameters),
                        b.prepare(parameters),
                        toFn),
                combineShrinks(a.getShrink(), b.getShrink(), toFn),
                combinePrettyPrinters(a.getPrettyPrinter(), b.getPrettyPrinter()),
                () -> combinedLabel(a.getLabel(),
                        b.getLabel()));
    }

    static <A, B, C> Arbitrary<Tuple3<A, B, C>> combine(Arbitrary<A> a,
                                                        Arbitrary<B> b,
                                                        Arbitrary<C> c) {

        Fn3<A, B, C, Tuple3<A, B, C>> toFn = HList::tuple;

        return concreteArbitrary(parameters -> new CompositeValueSupplier3<>(a.prepare(parameters),
                        b.prepare(parameters),
                        c.prepare(parameters),
                        toFn),
                combineShrinks(a.getShrink(), b.getShrink(), c.getShrink(), toFn),
                combinePrettyPrinters(a.getPrettyPrinter(), b.getPrettyPrinter(), c.getPrettyPrinter()),
                () -> combinedLabel(a.getLabel(),
                        b.getLabel(),
                        c.getLabel()));
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

    private static <A> boolean isNothing(Maybe<A> ma) {
        return ma.match(__ -> true, __ -> false);
    }

    private static <A, B> Fn1<Tuple2<A, B>, String> combinePrettyPrinters(Fn1<A, String> ppA,
                                                                          Fn1<B, String> ppB) {
        return t -> tuple(ppA.apply(t._1()), ppB.apply(t._2())).toString();
    }

    private static <A, B, C> Fn1<Tuple3<A, B, C>, String> combinePrettyPrinters(Fn1<A, String> ppA,
                                                                                Fn1<B, String> ppB,
                                                                                Fn1<C, String> ppC) {
        return t -> tuple(ppA.apply(t._1()), ppB.apply(t._2()), ppC.apply(t._3())).toString();
    }


    private static String combinedLabel(String... components) {
        return "(" + String.join(", ", components) + ")";
    }
}
