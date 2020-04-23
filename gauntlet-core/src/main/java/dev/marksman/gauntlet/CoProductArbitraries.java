package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.weights.EitherWeights;
import dev.marksman.kraftwerk.weights.MaybeWeights;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkChoice2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkMaybe;
import static dev.marksman.kraftwerk.Generators.generateUnit;
import static dev.marksman.kraftwerk.weights.MaybeWeights.justs;

final class CoProductArbitraries {

    private static final MaybeWeights DEFAULT_MAYBE_WEIGHTS = justs(9).toNothings(1);

    private static final Arbitrary<Unit> ARBITRARY_UNIT = arbitrary(generateUnit());

    static Arbitrary<Unit> arbitraryUnit() {
        return ARBITRARY_UNIT;
    }

    static <A, B> Arbitrary<Choice2<A, B>> arbitraryChoice2(Weighted<Arbitrary<A>> a,
                                                            Weighted<Arbitrary<B>> b) {
        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        ShrinkStrategy<Choice2<A, B>> shrinkStrategy = shrinkChoice2(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()));
        return arbitraryCoProduct2(a, b, just(shrinkStrategy));
    }

    static <A, B> Arbitrary<Choice2<A, B>> arbitraryChoice2(Arbitrary<A> a,
                                                            Arbitrary<B> b) {
        return arbitraryChoice2(a.weighted(), b.weighted());
    }

    static <A> Arbitrary<Maybe<A>> arbitraryMaybe(MaybeWeights weights,
                                                  Arbitrary<A> a) {
        return arbitraryCoProduct2(arbitraryUnit().weighted(weights.getNothingWeight()),
                a.weighted(weights.getJustWeight()),
                nothing())
                .<Maybe<A>>convert(c -> c.match(__ -> nothing(), Maybe::just),
                        maybeA -> maybeA.match(Choice2::a, Choice2::b))
                .withShrinkStrategy(shrinkMaybe(a.getShrinkStrategy().orElse(ShrinkStrategy.none())));
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

    private static Generator<Choice2<Unit, Unit>> generateWhich(int weightA, int weightB) {
        if (weightA + weightB < 1) {
            throw new IllegalStateException("total of weights must be >= 1");
        }
        return Generators.choiceBuilderValue(weightA, UNIT)
                .orValue(weightB, UNIT)
                .toGenerator();
    }

    private static <A, B> Arbitrary<Choice2<A, B>> arbitraryCoProduct2(Weighted<Arbitrary<A>> a,
                                                                       Weighted<Arbitrary<B>> b,
                                                                       Maybe<ShrinkStrategy<Choice2<A, B>>> shrinkStrategy) {
        Generator<Choice2<Unit, Unit>> generateWhich = generateWhich(a.getWeight(), b.getWeight());

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        return ConcreteArbitrary.concreteArbitrary(parameters ->
                        new ChoiceSupply2<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                generateWhich.prepare(parameters)),
                shrinkStrategy,
                // TODO: prettyPrinter
                Object::toString);
    }
}
