package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkInt;
import static dev.marksman.kraftwerk.Generators.generateInt;

public final class Arbitraries {

    private Arbitraries() {

    }

    public static Arbitrary<Integer> arbitraryInt(Generator<Integer> generator) {
        return arbitrary(generator).withShrink(shrinkInt());
    }

    public static Arbitrary<Integer> arbitraryInt() {
        return arbitraryInt(generateInt());
    }

    public static Arbitrary<Integer> arbitraryInt(IntRange range) {
        return arbitrary(generateInt(range)).withShrink(shrinkInt(range));
    }

    public static Arbitrary<Integer> arbitraryInt(FrequencyMap<Integer> frequencyMap) {
        return arbitraryInt(frequencyMap.toGenerator());
    }

    public static <A, B> Arbitrary<Tuple2<A, B>> combine(Arbitrary<A> a,
                                                         Arbitrary<B> b) {
        return CompositeArbitraries.combine(a, b);
    }

    public static <A, B, C> Arbitrary<Tuple3<A, B, C>> combine(Arbitrary<A> a,
                                                               Arbitrary<B> b,
                                                               Arbitrary<C> c) {
        return CompositeArbitraries.combine(a, b, c);
    }

    public static <A, B, C, D> Arbitrary<Tuple4<A, B, C, D>> combine(Arbitrary<A> a,
                                                                     Arbitrary<B> b,
                                                                     Arbitrary<C> c,
                                                                     Arbitrary<D> d) {
        return CompositeArbitraries.combine(a, b, c, d);
    }

    public static <A, B, C, D, E> Arbitrary<Tuple5<A, B, C, D, E>> combine(Arbitrary<A> a,
                                                                           Arbitrary<B> b,
                                                                           Arbitrary<C> c,
                                                                           Arbitrary<D> d,
                                                                           Arbitrary<E> e) {
        return CompositeArbitraries.combine(a, b, c, d, e);
    }

    public static <A extends Comparable<A>> Arbitrary<Tuple2<A, A>> arbitraryOrderedPair(Arbitrary<A> a) {
        return a.pair().mapPreFilterOutput(t -> t._1().compareTo(t._2()) > 0 ? t.invert() : t);
    }

}
