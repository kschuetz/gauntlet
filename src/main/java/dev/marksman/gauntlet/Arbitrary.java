package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.Weighted;
import dev.marksman.kraftwerk.weights.MaybeWeights;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.gauntlet.CompositeArbitraries.combine;
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
public final class Arbitrary<A> {
    private final Fn1<GeneratorParameters, SupplyStrategy<A>> generator;
    private final ImmutableFiniteIterable<Fn1<GeneratorParameters, GeneratorParameters>> parameterTransforms;
    private final Filter<A> filter;
    private final Maybe<ShrinkStrategy<A>> shrinkStrategy;
    private final PrettyPrinter<A> prettyPrinter;
    private final int maxDiscards;

    @SuppressWarnings("unchecked")
    private Arbitrary(Fn1<GeneratorParameters, SupplyStrategy<A>> generator,
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
        return arbitrary(p -> new UnfilteredSupplyStrategy<>(generator.prepare(p), labelSupplier),
                nothing(), defaultPrettyPrinter());
    }

    static <A> Arbitrary<A> arbitrary(Fn1<GeneratorParameters, SupplyStrategy<A>> generator,
                                      Maybe<ShrinkStrategy<A>> shrinkStrategy,
                                      PrettyPrinter<? super A> prettyPrinter) {
        return new Arbitrary<>(generator, emptyImmutableFiniteIterable(), Filter.emptyFilter(), shrinkStrategy,
                prettyPrinter, Gauntlet.DEFAULT_MAX_DISCARDS);
    }

    public SupplyStrategy<A> supplyStrategy(GeneratorParameters parameters) {
        GeneratorParameters transformedParameters = parameterTransforms.foldLeft((acc, f) -> f.apply(acc), parameters);
        SupplyStrategy<A> vs = generator.apply(transformedParameters);
        if (filter.isEmpty()) {
            return vs;
        } else {
            return new FilteredSupplyStrategy<>(vs, filter, maxDiscards);
        }
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

    public <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        return new Arbitrary<>(generator.fmap(vs -> vs.fmap(ab)),
                parameterTransforms,
                filter.contraMap(ba),
                shrinkStrategy.fmap(s -> s.convert(ab, ba)),
                prettyPrinter.contraMap(ba),
                maxDiscards);

    }

    public <B> Arbitrary<Tuple2<B, A>> addLayer(int sampleCount, Fn1<A, Arbitrary<B>> f) {
        throw new UnsupportedOperationException("TODO");
    }

    public Arbitrary<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> modifyFn) {
        return new Arbitrary<>(generator, parameterTransforms.append(modifyFn), filter, shrinkStrategy, prettyPrinter, maxDiscards);
    }

    public Arbitrary<ImmutableVector<A>> vector() {
        return CollectionArbitraries.vector(this);
    }

    public Arbitrary<ImmutableVector<A>> vectorOfN(int count) {
        return CollectionArbitraries.vectorOfN(count, this);
    }

    public Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return CollectionArbitraries.nonEmptyVector(this);
    }

    public Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count) {
        return CollectionArbitraries.nonEmptyVectorOfN(count, this);
    }

    public Arbitrary<ArrayList<A>> arrayList() {
        return CollectionArbitraries.arrayList(this);
    }

    public Arbitrary<ArrayList<A>> arrayListOfN(int count) {
        return CollectionArbitraries.arrayListOfN(count, this);
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

    public <B> Arbitrary<B> convert(Iso<A, A, B, B> iso) {
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
}
