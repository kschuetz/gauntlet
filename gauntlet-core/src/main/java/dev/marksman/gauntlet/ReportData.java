package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class ReportData<A> {
    @Getter
    private final Prop<A> prop;
    @Getter
    private final Outcome<A> outcome;
    @Getter
    private final Fn1<A, String> prettyPrinter;

    public static <A> ReportData<A> reportData(Prop<A> prop, Outcome<A> outcome, Fn1<A, String> prettyPrinter) {
        return new ReportData<>(prop, outcome, prettyPrinter);
    }
}
