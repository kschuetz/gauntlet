package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;

import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkInt;
import static dev.marksman.kraftwerk.Generators.generateInt;

public final class Arbitraries {

    private Arbitraries() {

    }

    public static Arbitrary<Integer> arbitraryInt() {
        return arbitrary(generateInt()).withShrink(shrinkInt());
    }

    public static Arbitrary<Integer> arbitraryInt(int min, int max) {
        return arbitrary(generateInt(min, max)).withShrink(shrinkInt());
    }


    public static <A, B> Arbitrary<Tuple2<A, B>> combine(Arbitrary<A> a,
                                                         Arbitrary<B> b) {
        return CompositeArbitraries.combine2(a, b);
    }

    public static <A, B, C> Arbitrary<Tuple3<A, B, C>> combine(Arbitrary<A> a,
                                                               Arbitrary<B> b,
                                                               Arbitrary<C> c) {
        return CompositeArbitraries.combine3(a, b, c);
    }


}
