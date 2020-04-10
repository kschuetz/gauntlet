package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.GeneratorParameters;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.CompositeArbitraries.combine;
import static dev.marksman.gauntlet.ConcreteArbitrary.concreteArbitrary;

public interface Arbitrary<A> {
    ValueSupplier<A> prepare(GeneratorParameters parameters);

    Maybe<Shrink<A>> getShrink();

    Fn1<? super A, String> getPrettyPrinter();

    Arbitrary<A> withShrink(Shrink<A> shrink);

    Arbitrary<A> withNoShrink();

    Arbitrary<A> suchThat(Fn1<? super A, Boolean> predicate);

    Arbitrary<A> withMaxDiscards(int maxDiscards);

    Arbitrary<A> withPrettyPrinter(Fn1<? super A, String> prettyPrinter);

    <B> Arbitrary<B> convert(Fn1<A, B> ab, Fn1<B, A> ba);

    Arbitrary<A> modifyGeneratorParameters(Fn1<GeneratorParameters, GeneratorParameters> modifyFn);

    Arbitrary<ImmutableVector<A>> vector();

    Arbitrary<ImmutableVector<A>> vectorOfN(int count);

    Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVector();

    Arbitrary<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count);

    Arbitrary<ArrayList<A>> arrayList();

    Arbitrary<ArrayList<A>> arrayListOfN(int count);

    Arbitrary<ArrayList<A>> nonEmptyArrayList();

    Arbitrary<HashSet<A>> hashSet();

    Arbitrary<HashSet<A>> nonEmptyHashSet();

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
