package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Contravariant;

import java.util.Objects;

@FunctionalInterface
public interface PrettyPrinter<A> extends Contravariant<A, PrettyPrinter<?>> {
    static <A> PrettyPrinter<A> defaultPrettyPrinter() {
        return prettyPrinter(Objects::toString);
    }

    /**
     * Creates a {@code PrettyPrinter} that ignores its {@link PrettyPrintParameters}
     *
     * @param f   a function from a value to its string representation
     * @param <A> the type to be pretty-printed
     * @return a {@code PrettyPrinter<A>}
     */
    static <A> PrettyPrinter<A> prettyPrinter(Fn1<? super A, String> f) {
        return (__, value) -> f.apply(value);
    }

    String prettyPrint(PrettyPrintParameters parameters, A value);

    default Fn1<? super A, String> prettyPrint(PrettyPrintParameters parameters) {
        return value -> prettyPrint(parameters, value);
    }

    @Override
    default <B> PrettyPrinter<B> contraMap(Fn1<? super B, ? extends A> fn1) {
        return null;
    }
}
