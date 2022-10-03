package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import software.kes.gauntlet.shrink.ShrinkStrategy;
import software.kes.gauntlet.shrink.builtins.ShrinkStrategies;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Generators;
import software.kes.kraftwerk.Weighted;
import software.kes.kraftwerk.weights.EitherWeights;
import software.kes.kraftwerk.weights.MaybeWeights;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static software.kes.gauntlet.PrettyPrinter.defaultPrettyPrinter;
import static software.kes.kraftwerk.Generators.generateUnit;
import static software.kes.kraftwerk.Weighted.weighted;
import static software.kes.kraftwerk.weights.MaybeWeights.justs;

final class CoProductArbitraries {
    private static final MaybeWeights DEFAULT_MAYBE_WEIGHTS = justs(9).toNothings(1);

    private static final Arbitrary<Unit> ARBITRARY_UNIT = Arbitrary.arbitrary(generateUnit());

    static Arbitrary<Unit> arbitraryUnit() {
        return ARBITRARY_UNIT;
    }

    static <A, B> Arbitrary<Choice2<A, B>> arbitraryChoice2(Weighted<Arbitrary<A>> a,
                                                            Weighted<Arbitrary<B>> b) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        ShrinkStrategy<Choice2<A, B>> shrinkStrategy = ShrinkStrategies.shrinkChoice2(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct2(a, b, just(shrinkStrategy));
    }

    static <A, B> Arbitrary<Choice2<A, B>> arbitraryChoice2(Arbitrary<A> a,
                                                            Arbitrary<B> b) {
        return arbitraryChoice2(a.weighted(), b.weighted());
    }

    static <A, B, C> Arbitrary<Choice3<A, B, C>> arbitraryChoice3(Weighted<Arbitrary<A>> a,
                                                                  Weighted<Arbitrary<B>> b,
                                                                  Weighted<Arbitrary<C>> c) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        ShrinkStrategy<Choice3<A, B, C>> shrinkStrategy = ShrinkStrategies.shrinkChoice3(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryC.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct3(a, b, c, just(shrinkStrategy));
    }

    static <A, B, C> Arbitrary<Choice3<A, B, C>> arbitraryChoice3(Arbitrary<A> a,
                                                                  Arbitrary<B> b,
                                                                  Arbitrary<C> c) {
        return arbitraryChoice3(a.weighted(), b.weighted(), c.weighted());
    }

    static <A, B, C, D> Arbitrary<Choice4<A, B, C, D>> arbitraryChoice4(Weighted<Arbitrary<A>> a,
                                                                        Weighted<Arbitrary<B>> b,
                                                                        Weighted<Arbitrary<C>> c,
                                                                        Weighted<Arbitrary<D>> d) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        ShrinkStrategy<Choice4<A, B, C, D>> shrinkStrategy = ShrinkStrategies.shrinkChoice4(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryC.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryD.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct4(a, b, c, d, just(shrinkStrategy));
    }

    static <A, B, C, D> Arbitrary<Choice4<A, B, C, D>> arbitraryChoice4(Arbitrary<A> a,
                                                                        Arbitrary<B> b,
                                                                        Arbitrary<C> c,
                                                                        Arbitrary<D> d) {
        return arbitraryChoice4(a.weighted(), b.weighted(), c.weighted(), d.weighted());
    }

    static <A, B, C, D, E> Arbitrary<Choice5<A, B, C, D, E>> arbitraryChoice5(Weighted<Arbitrary<A>> a,
                                                                              Weighted<Arbitrary<B>> b,
                                                                              Weighted<Arbitrary<C>> c,
                                                                              Weighted<Arbitrary<D>> d,
                                                                              Weighted<Arbitrary<E>> e) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        ShrinkStrategy<Choice5<A, B, C, D, E>> shrinkStrategy = ShrinkStrategies.shrinkChoice5(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryC.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryD.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryE.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct5(a, b, c, d, e, just(shrinkStrategy));
    }

    static <A, B, C, D, E> Arbitrary<Choice5<A, B, C, D, E>> arbitraryChoice5(Arbitrary<A> a,
                                                                              Arbitrary<B> b,
                                                                              Arbitrary<C> c,
                                                                              Arbitrary<D> d,
                                                                              Arbitrary<E> e) {
        return arbitraryChoice5(a.weighted(), b.weighted(), c.weighted(), d.weighted(), e.weighted());
    }

    static <A, B, C, D, E, F> Arbitrary<Choice6<A, B, C, D, E, F>> arbitraryChoice6(Weighted<Arbitrary<A>> a,
                                                                                    Weighted<Arbitrary<B>> b,
                                                                                    Weighted<Arbitrary<C>> c,
                                                                                    Weighted<Arbitrary<D>> d,
                                                                                    Weighted<Arbitrary<E>> e,
                                                                                    Weighted<Arbitrary<F>> f) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        Arbitrary<F> arbitraryF = f.getValue();
        ShrinkStrategy<Choice6<A, B, C, D, E, F>> shrinkStrategy = ShrinkStrategies.shrinkChoice6(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryC.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryD.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryE.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryF.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct6(a, b, c, d, e, f, just(shrinkStrategy));
    }

    static <A, B, C, D, E, F> Arbitrary<Choice6<A, B, C, D, E, F>> arbitraryChoice6(Arbitrary<A> a,
                                                                                    Arbitrary<B> b,
                                                                                    Arbitrary<C> c,
                                                                                    Arbitrary<D> d,
                                                                                    Arbitrary<E> e,
                                                                                    Arbitrary<F> f) {
        return arbitraryChoice6(a.weighted(), b.weighted(), c.weighted(), d.weighted(), e.weighted(), f.weighted());
    }

    static <A, B, C, D, E, F, G> Arbitrary<Choice7<A, B, C, D, E, F, G>> arbitraryChoice7(Weighted<Arbitrary<A>> a,
                                                                                          Weighted<Arbitrary<B>> b,
                                                                                          Weighted<Arbitrary<C>> c,
                                                                                          Weighted<Arbitrary<D>> d,
                                                                                          Weighted<Arbitrary<E>> e,
                                                                                          Weighted<Arbitrary<F>> f,
                                                                                          Weighted<Arbitrary<G>> g) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        Arbitrary<F> arbitraryF = f.getValue();
        Arbitrary<G> arbitraryG = g.getValue();
        ShrinkStrategy<Choice7<A, B, C, D, E, F, G>> shrinkStrategy = ShrinkStrategies.shrinkChoice7(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryC.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryD.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryE.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryF.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryG.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct7(a, b, c, d, e, f, g, just(shrinkStrategy));
    }

    static <A, B, C, D, E, F, G> Arbitrary<Choice7<A, B, C, D, E, F, G>> arbitraryChoice7(Arbitrary<A> a,
                                                                                          Arbitrary<B> b,
                                                                                          Arbitrary<C> c,
                                                                                          Arbitrary<D> d,
                                                                                          Arbitrary<E> e,
                                                                                          Arbitrary<F> f,
                                                                                          Arbitrary<G> g) {
        return arbitraryChoice7(a.weighted(), b.weighted(), c.weighted(), d.weighted(), e.weighted(), f.weighted(), g.weighted());
    }

    static <A, B, C, D, E, F, G, H> Arbitrary<Choice8<A, B, C, D, E, F, G, H>> arbitraryChoice8(Weighted<Arbitrary<A>> a,
                                                                                                Weighted<Arbitrary<B>> b,
                                                                                                Weighted<Arbitrary<C>> c,
                                                                                                Weighted<Arbitrary<D>> d,
                                                                                                Weighted<Arbitrary<E>> e,
                                                                                                Weighted<Arbitrary<F>> f,
                                                                                                Weighted<Arbitrary<G>> g,
                                                                                                Weighted<Arbitrary<H>> h) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        Arbitrary<F> arbitraryF = f.getValue();
        Arbitrary<G> arbitraryG = g.getValue();
        Arbitrary<H> arbitraryH = h.getValue();
        ShrinkStrategy<Choice8<A, B, C, D, E, F, G, H>> shrinkStrategy = ShrinkStrategies.shrinkChoice8(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryC.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryD.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryE.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryF.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryG.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryH.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct8(a, b, c, d, e, f, g, h, just(shrinkStrategy));
    }

    static <A, B, C, D, E, F, G, H> Arbitrary<Choice8<A, B, C, D, E, F, G, H>> arbitraryChoice8(Arbitrary<A> a,
                                                                                                Arbitrary<B> b,
                                                                                                Arbitrary<C> c,
                                                                                                Arbitrary<D> d,
                                                                                                Arbitrary<E> e,
                                                                                                Arbitrary<F> f,
                                                                                                Arbitrary<G> g,
                                                                                                Arbitrary<H> h) {
        return arbitraryChoice8(a.weighted(), b.weighted(), c.weighted(), d.weighted(), e.weighted(), f.weighted(), g.weighted(), h.weighted());
    }

    static <A> Arbitrary<Maybe<A>> arbitraryMaybe(MaybeWeights weights,
                                                  Arbitrary<A> a) {
        return arbitraryCoProduct2(arbitraryUnit().weighted(weights.getNothingWeight()),
                a.weighted(weights.getJustWeight()),
                nothing())
                .<Maybe<A>>convert(c -> c.match(__ -> nothing(), Maybe::just),
                        maybeA -> maybeA.match(Choice2::a, Choice2::b))
                .withShrinkStrategy(ShrinkStrategies.shrinkMaybe(a.getShrinkStrategy().orElse(ShrinkStrategy.none())));
    }

    static <A> Arbitrary<Maybe<A>> arbitraryMaybe(Arbitrary<A> a) {
        return arbitraryMaybe(DEFAULT_MAYBE_WEIGHTS, a);
    }

    static <L, R> Arbitrary<Either<L, R>> arbitraryEither(Weighted<Arbitrary<L>> left,
                                                          Weighted<Arbitrary<R>> right) {
        return arbitraryChoice2(left, right)
                .convert(c -> c.match(Either::left, Either::right),
                        either -> either.match(Choice2::a, Choice2::b));
    }

    static <L, R> Arbitrary<Either<L, R>> arbitraryEither(Arbitrary<L> left,
                                                          Arbitrary<R> right) {
        return arbitraryEither(left.weighted(), right.weighted());
    }

    static <L, R> Arbitrary<Either<L, R>> arbitraryEither(EitherWeights weights,
                                                          Arbitrary<L> left,
                                                          Arbitrary<R> right) {
        return arbitraryEither(left.weighted(weights.getLeftWeight()), right.weighted(weights.getRightWeight()));
    }

    static <A, B> Arbitrary<These<A, B>> arbitraryThese(Arbitrary<A> a,
                                                        Arbitrary<B> b) {
        Arbitrary<Tuple2<A, B>> both = CompositeArbitraries.combine(a, b);
        ShrinkStrategy<These<A, B>> shrinkStrategy = ShrinkStrategies.shrinkThese(a.getShrinkStrategy().orElse(ShrinkStrategy.none()), b.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        Arbitrary<Choice3<A, B, Tuple2<A, B>>> choice3Arbitrary = arbitraryCoProduct3(a.weighted(), b.weighted(), both.weighted(), nothing());
        Arbitrary<These<A, B>> result = choice3Arbitrary.convert((Choice3<A, B, Tuple2<A, B>> c3) -> c3.match(These::a,
                These::b,
                xy -> These.both(xy._1(), xy._2())),
                (These<A, B> t) -> t.match(Choice3::a, Choice3::b, Choice3::c));
        return result.withShrinkStrategy(shrinkStrategy);
    }

    private static Generator<Choice2<Unit, Unit>> generateWhich(int weightA, int weightB) {
        validateWeights(weightA + weightB);
        return Generators.choiceBuilderValue(weighted(weightA, UNIT))
                .orValue(weighted(weightB, UNIT))
                .toGenerator();
    }

    private static Generator<Choice3<Unit, Unit, Unit>> generateWhich(int weightA, int weightB, int weightC) {
        validateWeights(weightA + weightB + weightC);
        return Generators.choiceBuilderValue(weighted(weightA, UNIT))
                .orValue(weighted(weightB, UNIT))
                .orValue(weighted(weightC, UNIT))
                .toGenerator();
    }

    private static Generator<Choice4<Unit, Unit, Unit, Unit>> generateWhich(int weightA, int weightB, int weightC, int weightD) {
        validateWeights(weightA + weightB + weightC + weightD);
        return Generators.choiceBuilderValue(weighted(weightA, UNIT))
                .orValue(weighted(weightB, UNIT))
                .orValue(weighted(weightC, UNIT))
                .orValue(weighted(weightD, UNIT))
                .toGenerator();
    }

    private static Generator<Choice5<Unit, Unit, Unit, Unit, Unit>> generateWhich(int weightA, int weightB, int weightC, int weightD,
                                                                                  int weightE) {
        validateWeights(weightA + weightB + weightC + weightD + weightE);
        return Generators.choiceBuilderValue(weighted(weightA, UNIT))
                .orValue(weighted(weightB, UNIT))
                .orValue(weighted(weightC, UNIT))
                .orValue(weighted(weightD, UNIT))
                .orValue(weighted(weightE, UNIT))
                .toGenerator();
    }

    private static Generator<Choice6<Unit, Unit, Unit, Unit, Unit, Unit>> generateWhich(int weightA, int weightB, int weightC, int weightD,
                                                                                        int weightE, int weightF) {
        validateWeights(weightA + weightB + weightC + weightD + weightE + weightF);
        return Generators.choiceBuilderValue(weighted(weightA, UNIT))
                .orValue(weighted(weightB, UNIT))
                .orValue(weighted(weightC, UNIT))
                .orValue(weighted(weightD, UNIT))
                .orValue(weighted(weightE, UNIT))
                .orValue(weighted(weightF, UNIT))
                .toGenerator();
    }

    private static Generator<Choice7<Unit, Unit, Unit, Unit, Unit, Unit, Unit>> generateWhich(int weightA, int weightB, int weightC, int weightD,
                                                                                              int weightE, int weightF, int weightG) {
        validateWeights(weightA + weightB + weightC + weightD + weightE + weightF + weightG);
        return Generators.choiceBuilderValue(weighted(weightA, UNIT))
                .orValue(weighted(weightB, UNIT))
                .orValue(weighted(weightC, UNIT))
                .orValue(weighted(weightD, UNIT))
                .orValue(weighted(weightE, UNIT))
                .orValue(weighted(weightF, UNIT))
                .orValue(weighted(weightG, UNIT))
                .toGenerator();
    }

    private static Generator<Choice8<Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit>> generateWhich(int weightA, int weightB, int weightC, int weightD,
                                                                                                    int weightE, int weightF, int weightG, int weightH) {
        validateWeights(weightA + weightB + weightC + weightD + weightE + weightF + weightG + weightH);
        return Generators.choiceBuilderValue(weighted(weightA, UNIT))
                .orValue(weighted(weightB, UNIT))
                .orValue(weighted(weightC, UNIT))
                .orValue(weighted(weightD, UNIT))
                .orValue(weighted(weightE, UNIT))
                .orValue(weighted(weightF, UNIT))
                .orValue(weighted(weightG, UNIT))
                .orValue(weighted(weightH, UNIT))
                .toGenerator();
    }

    private static <A, B> Arbitrary<Choice2<A, B>> arbitraryCoProduct2(Weighted<Arbitrary<A>> a,
                                                                       Weighted<Arbitrary<B>> b,
                                                                       Maybe<ShrinkStrategy<Choice2<A, B>>> shrinkStrategy) {
        Generator<Choice2<Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        return Arbitrary.arbitrary(parameters ->
                        new ChoiceSupply2<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                generateWhich.createGenerateFn(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    private static <A, B, C> Arbitrary<Choice3<A, B, C>> arbitraryCoProduct3(Weighted<Arbitrary<A>> a,
                                                                             Weighted<Arbitrary<B>> b,
                                                                             Weighted<Arbitrary<C>> c,
                                                                             Maybe<ShrinkStrategy<Choice3<A, B, C>>> shrinkStrategy) {
        Generator<Choice3<Unit, Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight(), c.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        return Arbitrary.arbitrary(parameters ->
                        new ChoiceSupply3<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                arbitraryC.createSupply(parameters),
                                generateWhich.createGenerateFn(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    private static <A, B, C, D> Arbitrary<Choice4<A, B, C, D>> arbitraryCoProduct4(Weighted<Arbitrary<A>> a,
                                                                                   Weighted<Arbitrary<B>> b,
                                                                                   Weighted<Arbitrary<C>> c,
                                                                                   Weighted<Arbitrary<D>> d,
                                                                                   Maybe<ShrinkStrategy<Choice4<A, B, C, D>>> shrinkStrategy) {
        Generator<Choice4<Unit, Unit, Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight(), c.getWeight(), d.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        return Arbitrary.arbitrary(parameters ->
                        new ChoiceSupply4<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                arbitraryC.createSupply(parameters),
                                arbitraryD.createSupply(parameters),
                                generateWhich.createGenerateFn(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    private static <A, B, C, D, E> Arbitrary<Choice5<A, B, C, D, E>> arbitraryCoProduct5(Weighted<Arbitrary<A>> a,
                                                                                         Weighted<Arbitrary<B>> b,
                                                                                         Weighted<Arbitrary<C>> c,
                                                                                         Weighted<Arbitrary<D>> d,
                                                                                         Weighted<Arbitrary<E>> e,
                                                                                         Maybe<ShrinkStrategy<Choice5<A, B, C, D, E>>> shrinkStrategy) {
        Generator<Choice5<Unit, Unit, Unit, Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight(), c.getWeight(),
                d.getWeight(), e.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        return Arbitrary.arbitrary(parameters ->
                        new ChoiceSupply5<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                arbitraryC.createSupply(parameters),
                                arbitraryD.createSupply(parameters),
                                arbitraryE.createSupply(parameters),
                                generateWhich.createGenerateFn(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    private static <A, B, C, D, E, F> Arbitrary<Choice6<A, B, C, D, E, F>> arbitraryCoProduct6(Weighted<Arbitrary<A>> a,
                                                                                               Weighted<Arbitrary<B>> b,
                                                                                               Weighted<Arbitrary<C>> c,
                                                                                               Weighted<Arbitrary<D>> d,
                                                                                               Weighted<Arbitrary<E>> e,
                                                                                               Weighted<Arbitrary<F>> f,
                                                                                               Maybe<ShrinkStrategy<Choice6<A, B, C, D, E, F>>> shrinkStrategy) {
        Generator<Choice6<Unit, Unit, Unit, Unit, Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight(), c.getWeight(),
                d.getWeight(), e.getWeight(), f.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        Arbitrary<F> arbitraryF = f.getValue();
        return Arbitrary.arbitrary(parameters ->
                        new ChoiceSupply6<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                arbitraryC.createSupply(parameters),
                                arbitraryD.createSupply(parameters),
                                arbitraryE.createSupply(parameters),
                                arbitraryF.createSupply(parameters),
                                generateWhich.createGenerateFn(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    private static <A, B, C, D, E, F, G> Arbitrary<Choice7<A, B, C, D, E, F, G>> arbitraryCoProduct7(Weighted<Arbitrary<A>> a,
                                                                                                     Weighted<Arbitrary<B>> b,
                                                                                                     Weighted<Arbitrary<C>> c,
                                                                                                     Weighted<Arbitrary<D>> d,
                                                                                                     Weighted<Arbitrary<E>> e,
                                                                                                     Weighted<Arbitrary<F>> f,
                                                                                                     Weighted<Arbitrary<G>> g,
                                                                                                     Maybe<ShrinkStrategy<Choice7<A, B, C, D, E, F, G>>> shrinkStrategy) {
        Generator<Choice7<Unit, Unit, Unit, Unit, Unit, Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight(), c.getWeight(),
                d.getWeight(), e.getWeight(), f.getWeight(), g.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        Arbitrary<F> arbitraryF = f.getValue();
        Arbitrary<G> arbitraryG = g.getValue();
        return Arbitrary.arbitrary(parameters ->
                        new ChoiceSupply7<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                arbitraryC.createSupply(parameters),
                                arbitraryD.createSupply(parameters),
                                arbitraryE.createSupply(parameters),
                                arbitraryF.createSupply(parameters),
                                arbitraryG.createSupply(parameters),
                                generateWhich.createGenerateFn(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    private static <A, B, C, D, E, F, G, H> Arbitrary<Choice8<A, B, C, D, E, F, G, H>> arbitraryCoProduct8(Weighted<Arbitrary<A>> a,
                                                                                                           Weighted<Arbitrary<B>> b,
                                                                                                           Weighted<Arbitrary<C>> c,
                                                                                                           Weighted<Arbitrary<D>> d,
                                                                                                           Weighted<Arbitrary<E>> e,
                                                                                                           Weighted<Arbitrary<F>> f,
                                                                                                           Weighted<Arbitrary<G>> g,
                                                                                                           Weighted<Arbitrary<H>> h,
                                                                                                           Maybe<ShrinkStrategy<Choice8<A, B, C, D, E, F, G, H>>> shrinkStrategy) {
        Generator<Choice8<Unit, Unit, Unit, Unit, Unit, Unit, Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight(), c.getWeight(),
                d.getWeight(), e.getWeight(), f.getWeight(), g.getWeight(), h.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        Arbitrary<C> arbitraryC = c.getValue();
        Arbitrary<D> arbitraryD = d.getValue();
        Arbitrary<E> arbitraryE = e.getValue();
        Arbitrary<F> arbitraryF = f.getValue();
        Arbitrary<G> arbitraryG = g.getValue();
        Arbitrary<H> arbitraryH = h.getValue();
        return Arbitrary.arbitrary(parameters ->
                        new ChoiceSupply8<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                arbitraryC.createSupply(parameters),
                                arbitraryD.createSupply(parameters),
                                arbitraryE.createSupply(parameters),
                                arbitraryF.createSupply(parameters),
                                arbitraryG.createSupply(parameters),
                                arbitraryH.createSupply(parameters),
                                generateWhich.createGenerateFn(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                defaultPrettyPrinter());
    }

    private static void validateWeights(int sum) {
        if (sum < 1) {
            throw new IllegalStateException("total of weights must be >= 1");
        }
    }
}
