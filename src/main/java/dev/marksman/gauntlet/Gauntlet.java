package dev.marksman.gauntlet;

import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;

public class Gauntlet {

    public static <A> void forAll(Generator<A> gen,
                                  Prop<A> prop) {

    }

    public static <A> void forAllShrink(Generator<A> gen,
                                        Shrink<A> shrinkStrategy,
                                        Prop<A> prop) {

    }


}
