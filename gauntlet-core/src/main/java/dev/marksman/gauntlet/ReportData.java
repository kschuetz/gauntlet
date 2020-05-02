package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

public final class ReportData<A> {
    private final Prop<A> prop;
    private final TestResult<A> result;
    private final Fn1<? super A, String> prettyPrinter;
    private final Maybe<Long> initialSeedValue;

    private ReportData(Prop<A> prop, TestResult<A> result, Fn1<? super A, String> prettyPrinter, Maybe<Long> initialSeedValue) {
        this.prop = prop;
        this.result = result;
        this.prettyPrinter = prettyPrinter;
        this.initialSeedValue = initialSeedValue;
    }

    public static <A> ReportData<A> reportData(Prop<A> prop, TestResult<A> testResult, Fn1<? super A, String> prettyPrinter, Maybe<Long> initialSeedValue) {
        return new ReportData<>(prop, testResult, prettyPrinter, initialSeedValue);
    }

    public Prop<A> getProp() {
        return this.prop;
    }

    public TestResult<A> getResult() {
        return this.result;
    }

    public Fn1<? super A, String> getPrettyPrinter() {
        return this.prettyPrinter;
    }

    public Maybe<Long> getInitialSeedValue() {
        return this.initialSeedValue;
    }
}
