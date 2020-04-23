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

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.CompositeArbitraries.combine;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;

public interface Arbitrary<A> {
    Supply<A> createSupply(GeneratorParameters parameters);

    Maybe<ShrinkStrategy<A>> getShrinkStrategy();

    Fn1<? super A, String> getPrettyPrinter();

    Arbitrary<A> withShrinkStrategy(ShrinkStrategy<A> shrinkStrategy);

    Arbitrary<A> withNoShrinkStrategy();

    Arbitrary<A> suchThat(Fn1<? super A, Boolean> predicate);

    Arbitrary<A> withMaxDiscards(int maxDiscards);

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

    static <A> Arbitrary<A> arbitrary(Generator<A> generator) {
        return concreteArbitrary(generator);
    }

}
