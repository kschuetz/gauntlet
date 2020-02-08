package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;

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


    public static <A, B, Out> Arbitrary<Out> combine(Fn1<Product2<A, B>, Out> to,
                                                     Fn1<Out, Product2<A, B>> from,
                                                     Arbitrary<A> a,
                                                     Arbitrary<B> b) {
        // TODO: replace WrappedGenerator abstraction;  filters should not be coupled with generators

        return null;
    }

}
