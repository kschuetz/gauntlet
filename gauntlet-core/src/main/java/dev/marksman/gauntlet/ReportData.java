package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class ReportData<A> {
    @Getter
    private final Prop<A> prop;
    @Getter
    private final TestResult<A> result;
    @Getter
    private final Fn1<A, String> prettyPrinter;
    @Getter
    private final Maybe<Long> initialSeedValue;

    public static <A> ReportData<A> reportData(Prop<A> prop, TestResult<A> testResult, Fn1<A, String> prettyPrinter, Maybe<Long> initialSeedValue) {
        return new ReportData<>(prop, testResult, prettyPrinter, initialSeedValue);
    }

}
