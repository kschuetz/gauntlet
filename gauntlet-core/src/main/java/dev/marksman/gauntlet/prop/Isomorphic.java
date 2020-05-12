package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.collectionviews.NonEmptyVectorBuilder;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import java.util.Objects;

import static dev.marksman.gauntlet.EvalFailure.evalFailure;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.PrettyPrinting.productStringFromList;
import static dev.marksman.gauntlet.Reasons.reasons;

public final class Isomorphic<A, B> implements Prop<A> {
    private final Fn2<? super B, ? super B, Boolean> equivalenceRelation;
    private final Fn1<? super B, String> prettyPrinter;
    private final ImmutableNonEmptyFiniteIterable<Fn1<A, B>> fns;

    private Isomorphic(Fn2<? super B, ? super B, Boolean> equivalenceRelation,
                       Fn1<? super B, String> prettyPrinter,
                       ImmutableNonEmptyFiniteIterable<Fn1<A, B>> fns) {
        this.equivalenceRelation = equivalenceRelation;
        this.prettyPrinter = prettyPrinter;
        this.fns = fns;
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Isomorphic<A, B> isomorphic(Fn1<? super A, ? extends B> f1,
                                                     Fn1<? super A, ? extends B> f2,
                                                     Fn1<? super A, ? extends B>... more) {
        NonEmptyVectorBuilder<Fn1<A, B>> builder = Vector.<Fn1<A, B>>builder().add((Fn1<A, B>) f1)
                .add((Fn1<A, B>) f2);
        for (Fn1<? super A, ? extends B> f : more) {
            builder = builder.add((Fn1<A, B>) f);
        }
        return new Isomorphic<>(Objects::equals, (Fn1<? super B, String>) Object::toString, builder.build());
    }

    /**
     * Updates the strategy for checking for equality for values of type {@code B}.
     *
     * @param equivalenceRelation a function that returns true iff both arguments are considered equal
     * @return a new {@code Isomorphic<A, B>} that is the same as this one, with the equivalence relation updated
     */
    public Isomorphic<A, B> withEquivalenceRelation(Fn2<? super B, ? super B, Boolean> equivalenceRelation) {
        return new Isomorphic<>(equivalenceRelation, prettyPrinter, fns);
    }

    /**
     * Updates the pretty printer for values of type {@code B}.  This affects how the values will appear in
     * error messages.
     *
     * @param prettyPrinter a function to convert a value of type {@code B} to a {@code String}
     * @return a new {@code Isomorphic<A, B>} that is the same as this one, with the pretty printer updated
     */
    public Isomorphic<A, B> withPrettyPrinter(Fn1<? super B, String> prettyPrinter) {
        return new Isomorphic<>(equivalenceRelation, prettyPrinter, fns);
    }

    @Override
    public EvalResult evaluate(A data) {
        boolean success = true;
        B target = fns.head().apply(data);
        for (Fn1<? super A, ? extends B> f : fns.tail()) {
            B result = f.apply(data);
            if (!equivalenceRelation.apply(target, result)) {
                success = false;
                break;
            }
        }
        if (success) {
            return evalSuccess();
        } else {
            String results = productStringFromList(prettyPrinter, fns.fmap(f -> f.apply(data)));
            return evalFailure(this, reasons("results are not equal: " + results));
        }
    }

    @Override
    public String getName() {
        return "isomorphic";
    }
}
