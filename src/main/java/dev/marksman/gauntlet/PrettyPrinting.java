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
    public static <A, B> PrettyPrinter<? super Product2<A, B>> productPrettyPrinter(PrettyPrinter<? super A> ppA,
                                                                                    PrettyPrinter<? super B> ppB) {
        return (params, t) -> makeProductString(ppA.prettyPrint(params, t._1()),
                ppB.prettyPrint(params, t._2()));
    }

    public static <A, B, C> PrettyPrinter<? super Product3<A, B, C>> productPrettyPrinter(PrettyPrinter<? super A> ppA,
                                                                                          PrettyPrinter<? super B> ppB,
                                                                                          PrettyPrinter<? super C> ppC) {
        return (params, t) -> makeProductString(ppA.prettyPrint(params, t._1()),
                ppB.prettyPrint(params, t._2()),
                ppC.prettyPrint(params, t._3()));
    }

    public static <A, B, C, D> PrettyPrinter<? super Product4<A, B, C, D>> productPrettyPrinter(PrettyPrinter<? super A> ppA,
                                                                                                PrettyPrinter<? super B> ppB,
                                                                                                PrettyPrinter<? super C> ppC,
                                                                                                PrettyPrinter<? super D> ppD) {
        return (params, t) -> makeProductString(ppA.prettyPrint(params, t._1()),
                ppB.prettyPrint(params, t._2()),
                ppC.prettyPrint(params, t._3()),
                ppD.prettyPrint(params, t._4()));
    }

    public static <A, B, C, D, E> PrettyPrinter<? super Product5<A, B, C, D, E>> productPrettyPrinter(PrettyPrinter<? super A> ppA,
                                                                                                      PrettyPrinter<? super B> ppB,
                                                                                                      PrettyPrinter<? super C> ppC,
                                                                                                      PrettyPrinter<? super D> ppD,
                                                                                                      PrettyPrinter<? super E> ppE) {
        return (params, t) -> makeProductString(ppA.prettyPrint(params, t._1()),
                ppB.prettyPrint(params, t._2()),
                ppC.prettyPrint(params, t._3()),
                ppD.prettyPrint(params, t._4()),
                ppE.prettyPrint(params, t._5()));
    }

    public static <A, B, C, D, E, F> PrettyPrinter<? super Product6<A, B, C, D, E, F>> productPrettyPrinter(PrettyPrinter<? super A> ppA,
                                                                                                            PrettyPrinter<? super B> ppB,
                                                                                                            PrettyPrinter<? super C> ppC,
                                                                                                            PrettyPrinter<? super D> ppD,
                                                                                                            PrettyPrinter<? super E> ppE,
                                                                                                            PrettyPrinter<? super F> ppF) {
        return (params, t) -> makeProductString(ppA.prettyPrint(params, t._1()),
                ppB.prettyPrint(params, t._2()),
                ppC.prettyPrint(params, t._3()),
                ppD.prettyPrint(params, t._4()),
                ppE.prettyPrint(params, t._5()),
                ppF.prettyPrint(params, t._6()));
    }

    public static <A, B, C, D, E, F, G> PrettyPrinter<? super Product7<A, B, C, D, E, F, G>> productPrettyPrinter(PrettyPrinter<? super A> ppA,
                                                                                                                  PrettyPrinter<? super B> ppB,
                                                                                                                  PrettyPrinter<? super C> ppC,
                                                                                                                  PrettyPrinter<? super D> ppD,
                                                                                                                  PrettyPrinter<? super E> ppE,
                                                                                                                  PrettyPrinter<? super F> ppF,
                                                                                                                  PrettyPrinter<? super G> ppG) {
        return (params, t) -> makeProductString(ppA.prettyPrint(params, t._1()),
                ppB.prettyPrint(params, t._2()),
                ppC.prettyPrint(params, t._3()),
                ppD.prettyPrint(params, t._4()),
                ppE.prettyPrint(params, t._5()),
                ppF.prettyPrint(params, t._6()),
                ppG.prettyPrint(params, t._7()));
    }

    public static <A, B, C, D, E, F, G, H> PrettyPrinter<? super Product8<A, B, C, D, E, F, G, H>> productPrettyPrinter(PrettyPrinter<? super A> ppA,
                                                                                                                        PrettyPrinter<? super B> ppB,
                                                                                                                        PrettyPrinter<? super C> ppC,
                                                                                                                        PrettyPrinter<? super D> ppD,
                                                                                                                        PrettyPrinter<? super E> ppE,
                                                                                                                        PrettyPrinter<? super F> ppF,
                                                                                                                        PrettyPrinter<? super G> ppG,
                                                                                                                        PrettyPrinter<? super H> ppH) {
        return (params, t) -> makeProductString(ppA.prettyPrint(params, t._1()),
                ppB.prettyPrint(params, t._2()),
                ppC.prettyPrint(params, t._3()),
                ppD.prettyPrint(params, t._4()),
                ppE.prettyPrint(params, t._5()),
                ppF.prettyPrint(params, t._6()),
                ppG.prettyPrint(params, t._7()),
                ppH.prettyPrint(params, t._8()));
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
