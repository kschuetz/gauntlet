package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.weights.MaybeWeights;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.gauntlet.CompositeArbitraries.combine;
import static dev.marksman.gauntlet.HigherOrderSupply.higherOrderSupply;
import static dev.marksman.gauntlet.PrettyPrinter.defaultPrettyPrinter;

/**
 * An {@code Arbitrary} differs from a {@code Generator} in that an {@code Arbitrary} adds the following capabilities:
 * <ul>
 *     <li>Can be filtered
 *     <li>Can carry a shrinking strategy
 *     <li>Can carry a custom pretty-printer
 * </ul>
 * <p>
 * When composing {@code Arbitrary}s, the underlying filters, shrinking strategies, and pretty-printers will be composed
 * as well.
 * <p>
 * An {@code Arbitrary} cannot be fmapped or flatMapped like a Generator can be.  However, if you have an {@code Iso<A, B>}, you
 * can convert an {@code Arbitrary<A>} to an {@code Arbitrary<B>}.
 * <p>
 * Several built-in {@code Arbitrary}s can be found in the {@link Arbitraries} class.
 * <p>
 * You can always create an {@code Arbitrary} from any {@code Generator}, no matter how complex, but you will have to provide your own
 * shrink strategy (if shrinking is desired).
 *
 * @param <A>
 */
public final class Arbitrary<A> implements SampleTypeMetadata<A> {
    private final Choice2<SimpleArbitrary<A>, HigherOrderArbitrary<? extends A>> generator;
    private final ImmutableFiniteIterable<Fn1<GeneratorParameters, GeneratorParameters>> parameterTransforms;
    private final Filter<A> filter;
    private final Maybe<ShrinkStrategy<A>> shrinkStrategy;
    private final PrettyPrinter<A> prettyPrinter;
    private final int maxDiscards;

    @SuppressWarnings("unchecked")
    private Arbitrary(Choice2<SimpleArbitrary<A>, HigherOrderArbitrary<? extends A>> generator,
                      ImmutableFiniteIterable<Fn1<GeneratorParameters, GeneratorParameters>> parameterTransforms,
                      Filter<A> filter, Maybe<ShrinkStrategy<A>> shrinkStrategy,
                      PrettyPrinter<? super A> prettyPrinter,
                      int maxDiscards) {
        this.generator = generator;
        this.parameterTransforms = parameterTransforms;
        this.filter = filter;
        this.shrinkStrategy = shrinkStrategy;
        this.prettyPrinter = (PrettyPrinter<A>) prettyPrinter;
        this.maxDiscards = maxDiscards;
    }

    /**
     * Create an {@code Arbitrary<A>} from a {@code Generator<A>}.
     * <p>
     * The resulting {@code Arbitrary} will not have a shrink strategy.  You will need to provided one
     * yourself (using {@link Arbitrary#withShrinkStrategy}).
     */
    public static <A> Arbitrary<A> arbitrary(Generator<A> generator) {
        Fn0<String> labelSupplier = () -> generator.getLabel().orElseGet(generator::toString);
        return arbitrary(p -> new UnfilteredSupply<>(generator.prepare(p), labelSupplier),
                nothing(), defaultPrettyPrinter());
    }

    static <A> Arbitrary<A> arbitrary(Fn1<GeneratorParameters, Supply<A>> generator,
                                      Maybe<ShrinkStrategy<A>> shrinkStrategy,
                                      PrettyPrinter<? super A> prettyPrinter) {
        return new Arbitrary<>(Choice2.a(new SimpleArbitrary<>(generator)),
                emptyImmutableFiniteIterable(), Filter.emptyFilter(), shrinkStrategy,
                prettyPrinter, Gauntlet.DEFAULT_MAX_DISCARDS);
    }

    @SuppressWarnings("unchecked")
    static <Z, A> Arbitrary<A> higherOrderArbitrary(Generator<Z> generator,
                                                    Fn1<Z, Arbitrary<? extends A>> transformFn) {
        HigherOrderArbitrary<A> higherOrder = new HigherOrderArbitrary<>((Generator<Object>) generator,
                (Fn1<Object, Arbitrary<? extends A>>) transformFn);
        return new Arbitrary<>(Choice2.b(higherOrder), emptyImmutableFiniteIterable(), Filter.emptyFilter(), nothing(),
                defaultPrettyPrinter(), Gauntlet.DEFAULT_MAX_DISCARDS);
    }

    @SuppressWarnings("unchecked")
    public Supply<A> createSupply(GeneratorParameters parameters) {
        GeneratorParameters transformedParameters = transformGeneratorParameters(parameters);
        Supply<A> supply = generator.match(simple -> simple.createSupply(transformedParameters),
                higherOrder -> higherOrderSupply(transformedParameters, higherOrder.getGenerator().prepare(transformedParameters),
                        ((HigherOrderArbitrary<A>) higherOrder).getTransformFn()));

        if (filter.isEmpty()) {
            return supply;
        } else {
            return new FilteredSupply<>(supply, filter, maxDiscards);
        }
    }

    @SuppressWarnings("unchecked")
    public SampleTypeMetadata<A> getSampleTypeMetadata(GeneratorParameters generatorParameters, Seed inputSeed) {
        return (SampleTypeMetadata<A>) generator.match(__ -> this,
                higherOrder -> {
                    GeneratorParameters transformedParameters = transformGeneratorParameters(generatorParameters);
                    Generate<?> prepare = higherOrder.getGenerator().prepare(transformedParameters);
                    Result<? extends Seed, ?> aResult = prepare.apply(inputSeed);
                    Arbitrary<? extends A> arbitrary = higherOrder.getTransformFn().apply(aResult.getValue());
                    return arbitrary.getSampleTypeMetadata(transformedParameters, aResult.getNextState());
                });
    }


    public Maybe<ShrinkStrategy<A>> getShrinkStrategy() {
        return shrinkStrategy;
    }

    public PrettyPrinter<A> getPrettyPrinter() {
        return prettyPrinter;
    }

    /**
     * @return a new {@code Arbitrary} that is the same as this one, with the shrink strategy changed to the one provided.
     */
    public Arbitrary<A> withShrinkStrategy(ShrinkStrategy<A> shrinkStrategy) {
        return new Arbitrary<>(generator, parameterTransforms, filter, just(shrinkStrategy), prettyPrinter, maxDiscards);
    }

    /**
     * @return a new {@code Arbitrary} that is the same as this one, with the shrink strategy removed.
     */
    public Arbitrary<A> withNoShrinkStrategy() {
        return shrinkStrategy.match(__ -> this,
                __ -> new Arbitrary<>(generator, parameterTransforms, filter, nothing(), prettyPrinter, maxDiscards));
    }

    /**
     * Creates a new {@code Arbitrary} that filters its output.  Note that a filtered {@code Arbitrary} is not guaranteed
     * to ever return a value that satisfies its predicate, so some safety mechanisms are in place to
     * prevent it from running infinitely.
     * <p>
     * If after a number of tries, the {@code Arbitrary} cannot produce a value, it will fail with a {@code SupplyFailure} and
     * the test will fail abnormally.  The maximum number of tries can be controlled using {@link Arbitrary#withMaxDiscards}.
     *
     * @return a new {@code Arbitrary} that is the same as this one, with the added filter
     */
    public Arbitrary<A> suchThat(Fn1<? super A, Boolean> predicate) {
        return new Arbitrary<>(generator, parameterTransforms, filter.add(predicate), shrinkStrategy.fmap(s -> s.filter(predicate)),
                prettyPrinter, maxDiscards);
    }

    /**
     * Sets the maximum number of successive tries that a filtered Arbitrary will make in order to produce a
     * single value before failing with a SupplyFailure.  Once a value is produced, the counter will be reset.
     * <p>
     * This value will be respected even if you set it before any calls to {@link Arbitrary#suchThat}.
     *
     * @return a new {@code Arbitrary} that is the same as this one, with the max discards set to the provided value
     */
    public Arbitrary<A> withMaxDiscards(int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        if (maxDiscards != this.maxDiscards) {
            return new Arbitrary<>(generator, parameterTransforms, filter, shrinkStrategy, prettyPrinter, maxDiscards);
        } else {
            return this;
        }
    }

    /**
     * @return a new {@code Arbitrary} that is the same as this one, with the pretty-printer changed to the one provided.
     */
    public Arbitrary<A> withPrettyPrinter(PrettyPrinter<? super A> prettyPrinter) {
        return new Arbitrary<>(generator, parameterTransforms, filter, shrinkStrategy, prettyPrinter, maxDiscards);
    }

    @SuppressWarnings("unchecked")
    public <B> Arbitrary<B> convert(Fn1<? super A, ? extends B> ab, Fn1<? super B, ? extends A> ba) {
        Choice2<SimpleArbitrary<B>, HigherOrderArbitrary<? extends B>> newGenerator =
                (Choice2<SimpleArbitrary<B>, HigherOrderArbitrary<? extends B>>)
                        generator.match(simple -> Choice2.a((SimpleArbitrary<A>) simple.convert(ab, ba)),
                                higherOrder -> Choice2.b(((HigherOrderArbitrary<A>) higherOrder).convert(ab, ba)));

        return new Arbitrary<>(newGenerator,
                parameterTransforms,
                filter.contraMap(ba),
                shrinkStrategy.fmap(s -> s.convert(ab, ba)),
                prettyPrinter.contraMap(ba),
                maxDiscards);
    }

    public Arbitrary<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> modifyFn) {
        return new Arbitrary<>(generator, parameterTransforms.append(modifyFn), filter, shrinkStrategy, prettyPrinter, maxDiscards);
    }

    // TODO: respect higher-order arbitraries
    public Arbitrary<Vector<A>> vector() {
        return CollectionArbitraries.vector(this);
    }

    public Arbitrary<Vector<A>> vectorOfSize(int size) {
        return CollectionArbitraries.vectorOfSize(size, this);
    }

    public Arbitrary<Vector<A>> vectorOfSize(IntRange sizeRange) {
        return CollectionArbitraries.vectorOfSize(sizeRange, this);
    }

    public Arbitrary<NonEmptyVector<A>> nonEmptyVector() {
        return CollectionArbitraries.nonEmptyVector(this);
    }

    public Arbitrary<NonEmptyVector<A>> nonEmptyVectorOfSize(int size) {
        return CollectionArbitraries.nonEmptyVectorOfSize(size, this);
    }

    public Arbitrary<NonEmptyVector<A>> nonEmptyVectorOfSize(IntRange sizeRange) {
        return CollectionArbitraries.nonEmptyVectorOfSize(sizeRange, this);
    }

    public Arbitrary<ArrayList<A>> arrayList() {
        return CollectionArbitraries.arrayList(this);
    }

    public Arbitrary<ArrayList<A>> arrayListOfSize(int count) {
        return CollectionArbitraries.arrayListOfSize(count, this);
    }

    public Arbitrary<ArrayList<A>> nonEmptyArrayList() {
        return CollectionArbitraries.nonEmptyArrayList(this);
    }

    public Arbitrary<HashSet<A>> hashSet() {
        return CollectionArbitraries.hashSet(this);
    }

    public Arbitrary<HashSet<A>> nonEmptyHashSet() {
        return CollectionArbitraries.nonEmptyHashSet(this);
    }

    public Weighted<Arbitrary<A>> weighted() {
        return Weighted.weighted(1, this);
    }

    public Weighted<Arbitrary<A>> weighted(int weight) {
        return Weighted.weighted(weight, this);
    }

    public <B> Arbitrary<B> convert(Iso<? super A, ? extends A, ? extends B, ? super B> iso) {
        return convert(view(iso), view(iso.mirror()));
    }

    public Arbitrary<Tuple2<A, A>> pair() {
        return combine(this, this);
    }

    public Arbitrary<Tuple3<A, A, A>> triple() {
        return CompositeArbitraries.combine(this, this, this);
    }

    public Arbitrary<Maybe<A>> maybe() {
        return CoProductArbitraries.arbitraryMaybe(this);
    }

    public Arbitrary<Maybe<A>> maybe(MaybeWeights weights) {
        return CoProductArbitraries.arbitraryMaybe(weights, this);
    }

    private GeneratorParameters transformGeneratorParameters(GeneratorParameters parameters) {
        return parameterTransforms.foldLeft((acc, f) -> f.apply(acc), parameters);
    }

    static final class SimpleArbitrary<A> {
        private final Fn1<GeneratorParameters, Supply<A>> createSupplyFn;

        SimpleArbitrary(Fn1<GeneratorParameters, Supply<A>> createSupplyFn) {
            this.createSupplyFn = createSupplyFn;
        }

        Supply<A> createSupply(GeneratorParameters generatorParameters) {
            return createSupplyFn.apply(generatorParameters);
        }

        <B> SimpleArbitrary<B> convert(Fn1<? super A, ? extends B> ab, Fn1<? super B, ? extends A> ba) {
            return new SimpleArbitrary<>(createSupplyFn.fmap(supply -> supply.fmap(ab)));
        }
    }


    static final class HigherOrderArbitrary<A> {
        private final Generator<Object> generator;
        private final Fn1<Object, Arbitrary<? extends A>> transformFn;

        HigherOrderArbitrary(Generator<Object> generator, Fn1<Object, Arbitrary<? extends A>> transformFn) {
            this.generator = generator;
            this.transformFn = transformFn;
        }

        Generator<Object> getGenerator() {
            return generator;
        }

        @SuppressWarnings("unchecked")
        <B> HigherOrderArbitrary<B> convert(Fn1<? super A, ? extends B> ab, Fn1<? super B, ? extends A> ba) {
            return new HigherOrderArbitrary<>(generator, transformFn.fmap(arbitrary -> ((Arbitrary<A>) arbitrary).convert(ab, ba)));
        }

        Fn1<Object, Arbitrary<? extends A>> getTransformFn() {
            return transformFn;
        }

    }
}
