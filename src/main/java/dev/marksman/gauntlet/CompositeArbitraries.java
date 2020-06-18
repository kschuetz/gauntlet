package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functions.Fn8;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.Arbitrary.arbitrary;

final class CompositeArbitraries {
    static <A, B> Arbitrary<Tuple2<A, B>> combine(Arbitrary<A> a,
                                                  Arbitrary<B> b) {

        Fn2<A, B, Tuple2<A, B>> toFn = HList::tuple;

        return arbitrary(parameters -> new CompositeSupply2<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        toFn),
                combineShrinkStrategies(a.getShrinkStrategy(), b.getShrinkStrategy(), toFn),
                PrettyPrinting.<A, B>productPrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter()));
    }

    static <A, B, C> Arbitrary<Tuple3<A, B, C>> combine(Arbitrary<A> a,
                                                        Arbitrary<B> b,
                                                        Arbitrary<C> c) {

        Fn3<A, B, C, Tuple3<A, B, C>> toFn = HList::tuple;

        return arbitrary(parameters -> new CompositeSupply3<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        toFn),
                combineShrinkStrategies(a.getShrinkStrategy(), b.getShrinkStrategy(), c.getShrinkStrategy(), toFn),
                PrettyPrinting.<A, B, C>productPrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(), c.getPrettyPrinter()));
    }

    static <A, B, C, D> Arbitrary<Tuple4<A, B, C, D>> combine(Arbitrary<A> a,
                                                              Arbitrary<B> b,
                                                              Arbitrary<C> c,
                                                              Arbitrary<D> d) {

        Fn4<A, B, C, D, Tuple4<A, B, C, D>> toFn = HList::tuple;

        return arbitrary(parameters -> new CompositeSupply4<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        d.createSupply(parameters),
                        toFn),
                combineShrinkStrategies(a.getShrinkStrategy(), b.getShrinkStrategy(), c.getShrinkStrategy(), d.getShrinkStrategy(), toFn),
                PrettyPrinting.<A, B, C, D>productPrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(),
                        c.getPrettyPrinter(), d.getPrettyPrinter()));
    }

    static <A, B, C, D, E> Arbitrary<Tuple5<A, B, C, D, E>> combine(Arbitrary<A> a,
                                                                    Arbitrary<B> b,
                                                                    Arbitrary<C> c,
                                                                    Arbitrary<D> d,
                                                                    Arbitrary<E> e) {

        Fn5<A, B, C, D, E, Tuple5<A, B, C, D, E>> toFn = HList::tuple;

        return arbitrary(parameters -> new CompositeSupply5<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        d.createSupply(parameters),
                        e.createSupply(parameters),
                        toFn),
                combineShrinkStrategies(a.getShrinkStrategy(), b.getShrinkStrategy(), c.getShrinkStrategy(),
                        d.getShrinkStrategy(), e.getShrinkStrategy(), toFn),
                PrettyPrinting.<A, B, C, D, E>productPrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(),
                        c.getPrettyPrinter(), d.getPrettyPrinter(), e.getPrettyPrinter()));
    }

    static <A, B, C, D, E, F> Arbitrary<Tuple6<A, B, C, D, E, F>> combine(Arbitrary<A> a,
                                                                          Arbitrary<B> b,
                                                                          Arbitrary<C> c,
                                                                          Arbitrary<D> d,
                                                                          Arbitrary<E> e,
                                                                          Arbitrary<F> f) {

        Fn6<A, B, C, D, E, F, Tuple6<A, B, C, D, E, F>> toFn = HList::tuple;

        return arbitrary(parameters -> new CompositeSupply6<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        d.createSupply(parameters),
                        e.createSupply(parameters),
                        f.createSupply(parameters),
                        toFn),
                combineShrinkStrategies(a.getShrinkStrategy(), b.getShrinkStrategy(), c.getShrinkStrategy(),
                        d.getShrinkStrategy(), e.getShrinkStrategy(), f.getShrinkStrategy(), toFn),
                PrettyPrinting.<A, B, C, D, E, F>productPrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(),
                        c.getPrettyPrinter(), d.getPrettyPrinter(), e.getPrettyPrinter(), f.getPrettyPrinter()));
    }

    static <A, B, C, D, E, F, G> Arbitrary<Tuple7<A, B, C, D, E, F, G>> combine(Arbitrary<A> a,
                                                                                Arbitrary<B> b,
                                                                                Arbitrary<C> c,
                                                                                Arbitrary<D> d,
                                                                                Arbitrary<E> e,
                                                                                Arbitrary<F> f,
                                                                                Arbitrary<G> g) {

        Fn7<A, B, C, D, E, F, G, Tuple7<A, B, C, D, E, F, G>> toFn = HList::tuple;

        return arbitrary(parameters -> new CompositeSupply7<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        d.createSupply(parameters),
                        e.createSupply(parameters),
                        f.createSupply(parameters),
                        g.createSupply(parameters),
                        toFn),
                combineShrinkStrategies(a.getShrinkStrategy(), b.getShrinkStrategy(), c.getShrinkStrategy(),
                        d.getShrinkStrategy(), e.getShrinkStrategy(), f.getShrinkStrategy(), g.getShrinkStrategy(), toFn),
                PrettyPrinting.<A, B, C, D, E, F, G>productPrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(),
                        c.getPrettyPrinter(), d.getPrettyPrinter(), e.getPrettyPrinter(), f.getPrettyPrinter(),
                        g.getPrettyPrinter()));
    }

    static <A, B, C, D, E, F, G, H> Arbitrary<Tuple8<A, B, C, D, E, F, G, H>> combine(Arbitrary<A> a,
                                                                                      Arbitrary<B> b,
                                                                                      Arbitrary<C> c,
                                                                                      Arbitrary<D> d,
                                                                                      Arbitrary<E> e,
                                                                                      Arbitrary<F> f,
                                                                                      Arbitrary<G> g,
                                                                                      Arbitrary<H> h) {

        Fn8<A, B, C, D, E, F, G, H, Tuple8<A, B, C, D, E, F, G, H>> toFn = HList::tuple;

        return arbitrary(parameters -> new CompositeSupply8<>(a.createSupply(parameters),
                        b.createSupply(parameters),
                        c.createSupply(parameters),
                        d.createSupply(parameters),
                        e.createSupply(parameters),
                        f.createSupply(parameters),
                        g.createSupply(parameters),
                        h.createSupply(parameters),
                        toFn),
                combineShrinkStrategies(a.getShrinkStrategy(), b.getShrinkStrategy(), c.getShrinkStrategy(),
                        d.getShrinkStrategy(), e.getShrinkStrategy(), f.getShrinkStrategy(), g.getShrinkStrategy(),
                        h.getShrinkStrategy(), toFn),
                PrettyPrinting.<A, B, C, D, E, F, G, H>productPrettyPrinter(a.getPrettyPrinter(), b.getPrettyPrinter(),
                        c.getPrettyPrinter(), d.getPrettyPrinter(), e.getPrettyPrinter(), f.getPrettyPrinter(),
                        g.getPrettyPrinter(), h.getPrettyPrinter()));
    }

    static <A, B> Arbitrary<Choice2<A, B>> arbitraryChoice2(Arbitrary<A> a,
                                                            Arbitrary<B> b) {
        return null;
    }


    private static <A, B> Maybe<ShrinkStrategy<Tuple2<A, B>>> combineShrinkStrategies(Maybe<ShrinkStrategy<A>> shrinkA,
                                                                                      Maybe<ShrinkStrategy<B>> shrinkB,
                                                                                      Fn2<A, B, Tuple2<A, B>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB)) {
            return nothing();
        } else {
            return just(ShrinkStrategies.shrinkProduct(shrinkA.orElse(ShrinkStrategy.none()),
                    shrinkB.orElse(ShrinkStrategy.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C> Maybe<ShrinkStrategy<Tuple3<A, B, C>>> combineShrinkStrategies(Maybe<ShrinkStrategy<A>> shrinkA,
                                                                                            Maybe<ShrinkStrategy<B>> shrinkB,
                                                                                            Maybe<ShrinkStrategy<C>> shrinkC,
                                                                                            Fn3<A, B, C, Tuple3<A, B, C>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC)) {
            return nothing();
        } else {
            return just(ShrinkStrategies.shrinkProduct(shrinkA.orElse(ShrinkStrategy.none()),
                    shrinkB.orElse(ShrinkStrategy.none()),
                    shrinkC.orElse(ShrinkStrategy.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C, D> Maybe<ShrinkStrategy<Tuple4<A, B, C, D>>> combineShrinkStrategies(Maybe<ShrinkStrategy<A>> shrinkA,
                                                                                                  Maybe<ShrinkStrategy<B>> shrinkB,
                                                                                                  Maybe<ShrinkStrategy<C>> shrinkC,
                                                                                                  Maybe<ShrinkStrategy<D>> shrinkD,
                                                                                                  Fn4<A, B, C, D, Tuple4<A, B, C, D>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC) && isNothing(shrinkD)) {
            return nothing();
        } else {
            return just(ShrinkStrategies.shrinkProduct(shrinkA.orElse(ShrinkStrategy.none()),
                    shrinkB.orElse(ShrinkStrategy.none()),
                    shrinkC.orElse(ShrinkStrategy.none()),
                    shrinkD.orElse(ShrinkStrategy.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C, D, E> Maybe<ShrinkStrategy<Tuple5<A, B, C, D, E>>> combineShrinkStrategies(Maybe<ShrinkStrategy<A>> shrinkA,
                                                                                                        Maybe<ShrinkStrategy<B>> shrinkB,
                                                                                                        Maybe<ShrinkStrategy<C>> shrinkC,
                                                                                                        Maybe<ShrinkStrategy<D>> shrinkD,
                                                                                                        Maybe<ShrinkStrategy<E>> shrinkE,
                                                                                                        Fn5<A, B, C, D, E, Tuple5<A, B, C, D, E>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC) && isNothing(shrinkD) && isNothing(shrinkE)) {
            return nothing();
        } else {
            return just(ShrinkStrategies.shrinkProduct(shrinkA.orElse(ShrinkStrategy.none()),
                    shrinkB.orElse(ShrinkStrategy.none()),
                    shrinkC.orElse(ShrinkStrategy.none()),
                    shrinkD.orElse(ShrinkStrategy.none()),
                    shrinkE.orElse(ShrinkStrategy.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C, D, E, F> Maybe<ShrinkStrategy<Tuple6<A, B, C, D, E, F>>> combineShrinkStrategies(Maybe<ShrinkStrategy<A>> shrinkA,
                                                                                                              Maybe<ShrinkStrategy<B>> shrinkB,
                                                                                                              Maybe<ShrinkStrategy<C>> shrinkC,
                                                                                                              Maybe<ShrinkStrategy<D>> shrinkD,
                                                                                                              Maybe<ShrinkStrategy<E>> shrinkE,
                                                                                                              Maybe<ShrinkStrategy<F>> shrinkF,
                                                                                                              Fn6<A, B, C, D, E, F, Tuple6<A, B, C, D, E, F>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC) && isNothing(shrinkD) && isNothing(shrinkE) && isNothing(shrinkF)) {
            return nothing();
        } else {
            return just(ShrinkStrategies.shrinkProduct(shrinkA.orElse(ShrinkStrategy.none()),
                    shrinkB.orElse(ShrinkStrategy.none()),
                    shrinkC.orElse(ShrinkStrategy.none()),
                    shrinkD.orElse(ShrinkStrategy.none()),
                    shrinkE.orElse(ShrinkStrategy.none()),
                    shrinkF.orElse(ShrinkStrategy.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C, D, E, F, G> Maybe<ShrinkStrategy<Tuple7<A, B, C, D, E, F, G>>> combineShrinkStrategies(Maybe<ShrinkStrategy<A>> shrinkA,
                                                                                                                    Maybe<ShrinkStrategy<B>> shrinkB,
                                                                                                                    Maybe<ShrinkStrategy<C>> shrinkC,
                                                                                                                    Maybe<ShrinkStrategy<D>> shrinkD,
                                                                                                                    Maybe<ShrinkStrategy<E>> shrinkE,
                                                                                                                    Maybe<ShrinkStrategy<F>> shrinkF,
                                                                                                                    Maybe<ShrinkStrategy<G>> shrinkG,
                                                                                                                    Fn7<A, B, C, D, E, F, G, Tuple7<A, B, C, D, E, F, G>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC) && isNothing(shrinkD) && isNothing(shrinkE) && isNothing(shrinkF) && isNothing(shrinkG)) {
            return nothing();
        } else {
            return just(ShrinkStrategies.shrinkProduct(shrinkA.orElse(ShrinkStrategy.none()),
                    shrinkB.orElse(ShrinkStrategy.none()),
                    shrinkC.orElse(ShrinkStrategy.none()),
                    shrinkD.orElse(ShrinkStrategy.none()),
                    shrinkE.orElse(ShrinkStrategy.none()),
                    shrinkF.orElse(ShrinkStrategy.none()),
                    shrinkG.orElse(ShrinkStrategy.none()),
                    toFn, t -> t));
        }
    }

    private static <A, B, C, D, E, F, G, H> Maybe<ShrinkStrategy<Tuple8<A, B, C, D, E, F, G, H>>> combineShrinkStrategies(Maybe<ShrinkStrategy<A>> shrinkA,
                                                                                                                          Maybe<ShrinkStrategy<B>> shrinkB,
                                                                                                                          Maybe<ShrinkStrategy<C>> shrinkC,
                                                                                                                          Maybe<ShrinkStrategy<D>> shrinkD,
                                                                                                                          Maybe<ShrinkStrategy<E>> shrinkE,
                                                                                                                          Maybe<ShrinkStrategy<F>> shrinkF,
                                                                                                                          Maybe<ShrinkStrategy<G>> shrinkG,
                                                                                                                          Maybe<ShrinkStrategy<H>> shrinkH,
                                                                                                                          Fn8<A, B, C, D, E, F, G, H, Tuple8<A, B, C, D, E, F, G, H>> toFn) {
        if (isNothing(shrinkA) && isNothing(shrinkB) && isNothing(shrinkC) && isNothing(shrinkD) && isNothing(shrinkE) && isNothing(shrinkF)
                && isNothing(shrinkG) && isNothing(shrinkH)) {
            return nothing();
        } else {
            return just(ShrinkStrategies.shrinkProduct(shrinkA.orElse(ShrinkStrategy.none()),
                    shrinkB.orElse(ShrinkStrategy.none()),
                    shrinkC.orElse(ShrinkStrategy.none()),
                    shrinkD.orElse(ShrinkStrategy.none()),
                    shrinkE.orElse(ShrinkStrategy.none()),
                    shrinkF.orElse(ShrinkStrategy.none()),
                    shrinkG.orElse(ShrinkStrategy.none()),
                    shrinkH.orElse(ShrinkStrategy.none()),
                    toFn, t -> t));
        }
    }

    private static <A> boolean isNothing(Maybe<A> ma) {
        return ma.match(__ -> true, __ -> false);
    }
}
