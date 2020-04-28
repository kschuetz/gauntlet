package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.weights.MaybeWeights;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.CompositeArbitraries.combine;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;

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
public interface Arbitrary<A> {
    Supply<A> createSupply(GeneratorParameters parameters);

    Maybe<ShrinkStrategy<A>> getShrinkStrategy();

    Fn1<? super A, String> getPrettyPrinter();

    /**
     * @return a new {@code Arbitrary} that is the same as this one, with the shrink strategy changed to the one provided.
     */
    Arbitrary<A> withShrinkStrategy(ShrinkStrategy<A> shrinkStrategy);

    /**
     * @return a new {@code Arbitrary} that is the same as this one, with the shrink strategy removed.
     */
    Arbitrary<A> withNoShrinkStrategy();

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
    Arbitrary<A> suchThat(Fn1<? super A, Boolean> predicate);

    /**
     * Sets the maximum number of successive tries that a filtered Arbitrary will make in order to produce a
     * single value before failing with a SupplyFailure.  Once a value is produced, the counter will be reset.
     * <p>
     * This value will be respected even if you set it before any calls to {@link Arbitrary#suchThat}.
     *
     * @return a new {@code Arbitrary} that is the same as this one, with the max discards set to the provided value
     */
    Arbitrary<A> withMaxDiscards(int maxDiscards);

    /**
     * @return a new {@code Arbitrary} that is the same as this one, with the pretty-printer changed to the one provided.
     */
    Arbitrary<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter);

    <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba);

    Arbitrary<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> modifyFn);

    default Arbitrary<ImmutableVector<A>> vector() {
        return CollectionArbitraries.vector(this);
    }

    default Arbitrary<ImmutableVector<A>> vectorOfN(int count) {
        return CollectionArbitraries.vectorOfN(count, this);
    }

    default Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return CollectionArbitraries.nonEmptyVector(this);
    }

    default Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count) {
        return CollectionArbitraries.nonEmptyVectorOfN(count, this);
    }

    default Arbitrary<ArrayList<A>> arrayList() {
        return CollectionArbitraries.arrayList(this);
    }

    default Arbitrary<ArrayList<A>> arrayListOfN(int count) {
        return CollectionArbitraries.arrayListOfN(count, this);
    }

    default Arbitrary<ArrayList<A>> nonEmptyArrayList() {
        return CollectionArbitraries.nonEmptyArrayList(this);
    }

    default Arbitrary<HashSet<A>> hashSet() {
        return CollectionArbitraries.hashSet(this);
    }

    default Arbitrary<HashSet<A>> nonEmptyHashSet() {
        return CollectionArbitraries.nonEmptyHashSet(this);
    }

    default Weighted<Arbitrary<A>> weighted() {
        return Weighted.weighted(1, this);
    }

    default Weighted<Arbitrary<A>> weighted(int weight) {
        return Weighted.weighted(weight, this);
    }

    default <B> Arbitrary<B> convert(Iso<A, A, B, B> iso) {
        return convert(view(iso), view(iso.mirror()));
    }

    default Arbitrary<Tuple2<A, A>> pair() {
        return combine(this, this);
    }

    default Arbitrary<Tuple3<A, A, A>> triple() {
        return CompositeArbitraries.combine(this, this, this);
    }

    default Arbitrary<Maybe<A>> maybe() {
        return CoProductArbitraries.arbitraryMaybe(this);
    }

    default Arbitrary<Maybe<A>> maybe(MaybeWeights weights) {
        return CoProductArbitraries.arbitraryMaybe(weights, this);
    }

    /**
     * Create an {@code Arbitrary<A>} from a {@code Generator<A>}.
     * <p>
     * The resulting {@code Arbitrary} will not have a shrink strategy.  You will need to provided one
     * yourself (using {@link Arbitrary#withShrinkStrategy}).
     */
    static <A> Arbitrary<A> arbitrary(Generator<A> generator) {
        return concreteArbitrary(generator);
    }

}
