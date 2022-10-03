package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;

public final class ReportData<A> {
    private final Prop<A> prop;
    private final TestResult<A> result;
    private final PrettyPrinter<A> prettyPrinter;
    private final Maybe<Long> initialSeedValue;
    private final Maybe<TestParameterReportData> testParameterData;

    @SuppressWarnings("unchecked")
    private ReportData(Prop<A> prop, TestResult<A> result, PrettyPrinter<? super A> prettyPrinter,
                       Maybe<Long> initialSeedValue, Maybe<TestParameterReportData> testParameterData) {
        this.prop = prop;
        this.result = result;
        this.prettyPrinter = (PrettyPrinter<A>) prettyPrinter;
        this.initialSeedValue = initialSeedValue;
        this.testParameterData = testParameterData;
    }

    public static <A> ReportData<A> reportData(Prop<A> prop, TestResult<A> testResult, PrettyPrinter<? super A> prettyPrinter,
                                               Maybe<Long> initialSeedValue, Maybe<TestParameterReportData> testParameterData) {
        return new ReportData<>(prop, testResult, prettyPrinter, initialSeedValue, testParameterData);
    }

    public Prop<A> getProp() {
        return this.prop;
    }

    public TestResult<A> getResult() {
        return this.result;
    }

    public PrettyPrinter<A> getPrettyPrinter() {
        return this.prettyPrinter;
    }

    public Maybe<Long> getInitialSeedValue() {
        return this.initialSeedValue;
    }

    public Maybe<TestParameterReportData> getTestParameterData() {
        return testParameterData;
    }
}
