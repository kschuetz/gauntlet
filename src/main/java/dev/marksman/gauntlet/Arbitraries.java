package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.kraftwerk.Generator;
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

    public static Arbitrary<Integer> arbitraryInt(int min, int max) {
        return arbitraryInt(generateInt(min, max));
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


}
