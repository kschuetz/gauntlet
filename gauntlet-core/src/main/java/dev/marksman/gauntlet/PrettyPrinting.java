package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.adt.product.Product6;
import com.jnape.palatable.lambda.adt.product.Product7;
import com.jnape.palatable.lambda.adt.product.Product8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;

import static java.util.Arrays.asList;

public final class PrettyPrinting {
    public static <A, B> Fn1<? super Product2<A, B>, String> productPrettyPrinter(Fn1<? super A, String> ppA,
                                                                                  Fn1<? super B, String> ppB) {
        return t -> makeProductString(ppA.apply(t._1()),
                ppB.apply(t._2()));
    }

    public static <A, B, C> Fn1<? super Product3<A, B, C>, String> productPrettyPrinter(Fn1<? super A, String> ppA,
                                                                                        Fn1<? super B, String> ppB,
                                                                                        Fn1<? super C, String> ppC) {
        return t -> makeProductString(ppA.apply(t._1()),
                ppB.apply(t._2()),
                ppC.apply(t._3()));
    }

    public static <A, B, C, D> Fn1<? super Product4<A, B, C, D>, String> productPrettyPrinter(Fn1<? super A, String> ppA,
                                                                                              Fn1<? super B, String> ppB,
                                                                                              Fn1<? super C, String> ppC,
                                                                                              Fn1<? super D, String> ppD) {
        return t -> makeProductString(ppA.apply(t._1()),
                ppB.apply(t._2()),
                ppC.apply(t._3()),
                ppD.apply(t._4()));
    }

    public static <A, B, C, D, E> Fn1<? super Product5<A, B, C, D, E>, String> productPrettyPrinter(Fn1<? super A, String> ppA,
                                                                                                    Fn1<? super B, String> ppB,
                                                                                                    Fn1<? super C, String> ppC,
                                                                                                    Fn1<? super D, String> ppD,
                                                                                                    Fn1<? super E, String> ppE) {
        return t -> makeProductString(ppA.apply(t._1()),
                ppB.apply(t._2()),
                ppC.apply(t._3()),
                ppD.apply(t._4()),
                ppE.apply(t._5()));
    }

    public static <A, B, C, D, E, F> Fn1<? super Product6<A, B, C, D, E, F>, String> productPrettyPrinter(Fn1<? super A, String> ppA,
                                                                                                          Fn1<? super B, String> ppB,
                                                                                                          Fn1<? super C, String> ppC,
                                                                                                          Fn1<? super D, String> ppD,
                                                                                                          Fn1<? super E, String> ppE,
                                                                                                          Fn1<? super F, String> ppF) {
        return t -> makeProductString(ppA.apply(t._1()),
                ppB.apply(t._2()),
                ppC.apply(t._3()),
                ppD.apply(t._4()),
                ppE.apply(t._5()),
                ppF.apply(t._6()));
    }

    public static <A, B, C, D, E, F, G> Fn1<? super Product7<A, B, C, D, E, F, G>, String> productPrettyPrinter(Fn1<? super A, String> ppA,
                                                                                                                Fn1<? super B, String> ppB,
                                                                                                                Fn1<? super C, String> ppC,
                                                                                                                Fn1<? super D, String> ppD,
                                                                                                                Fn1<? super E, String> ppE,
                                                                                                                Fn1<? super F, String> ppF,
                                                                                                                Fn1<? super G, String> ppG) {
        return t -> makeProductString(ppA.apply(t._1()),
                ppB.apply(t._2()),
                ppC.apply(t._3()),
                ppD.apply(t._4()),
                ppE.apply(t._5()),
                ppF.apply(t._6()),
                ppG.apply(t._7()));
    }

    public static <A, B, C, D, E, F, G, H> Fn1<? super Product8<A, B, C, D, E, F, G, H>, String> productPrettyPrinter(Fn1<? super A, String> ppA,
                                                                                                                      Fn1<? super B, String> ppB,
                                                                                                                      Fn1<? super C, String> ppC,
                                                                                                                      Fn1<? super D, String> ppD,
                                                                                                                      Fn1<? super E, String> ppE,
                                                                                                                      Fn1<? super F, String> ppF,
                                                                                                                      Fn1<? super G, String> ppG,
                                                                                                                      Fn1<? super H, String> ppH) {
        return t -> makeProductString(ppA.apply(t._1()),
                ppB.apply(t._2()),
                ppC.apply(t._3()),
                ppD.apply(t._4()),
                ppE.apply(t._5()),
                ppF.apply(t._6()),
                ppG.apply(t._7()),
                ppH.apply(t._8()));
    }

    public static <A> String productStringFromList(Fn1<? super A, String> pp, Iterable<A> elements) {
        return makeProductString(Map.map(pp, elements));
    }

    private static String makeProductString(Iterable<String> elements) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        boolean inner = false;
        for (String element : elements) {
            if (inner) {
                builder.append(", ");
            } else {
                inner = true;
            }
            builder.append(element);
        }
        builder.append(")");
        return builder.toString();
    }

    private static String makeProductString(String... elements) {
        return makeProductString(asList(elements));
    }

}
