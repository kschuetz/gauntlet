package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

public final class DefaultReportRenderer implements ReportRenderer {
    private DefaultReportRenderer() {
    }

    public static DefaultReportRenderer defaultReportRenderer() {
        return new DefaultReportRenderer();
    }

    @Override
    public <A, P> String renderReport(ReportSettings settings, ReportData<A, P> reportData) {
        return reportData.getResult()
                .match(utr ->
                                utr.match(unfalsified -> renderReportForUnfalsified(settings, reportData, unfalsified),
                                        falsified -> renderReportForFalsified(settings, reportData, falsified)),
                        etr ->
                                etr.match(unproved -> renderReportForUnproved(settings, reportData, unproved),
                                        proved -> renderReportForProved(settings, reportData, proved)),
                        abnormal ->
                                abnormal.match(error -> renderReportForError(settings, reportData, error),
                                        exhausted -> renderReportForExhausted(settings, reportData, exhausted),
                                        timedOut -> renderReportForTimedOut(settings, reportData, timedOut),
                                        interrupted -> renderReportForInterrupted(settings, reportData, interrupted)));

    }

    private <A, P> String renderReportForUnfalsified(ReportSettings settings, ReportData<A, P> reportData, UniversalTestResult.Unfalsified<A> result) {
        return "";
    }

    private <A, P> String renderReportForProved(ReportSettings settings, ReportData<A, P> reportData, ExistentialTestResult.Proved<A> result) {
        return "";
    }

    private <A, P> String renderReportForFalsified(ReportSettings settings, ReportData<A, P> reportData, UniversalTestResult.Falsified<A> result) {
        Fn1<? super A, String> prettyPrinter = reportData.getPrettyPrinter();
        MutableReportBuilder output = new MutableReportBuilder();
        output.write("Counterexample found after ");
        int successCount = result.getSuccessCount();
        output.write(successCount);
        output.write(successCount == 1 ? " success" : " successes");
        reportData.getInitialSeedValue().toOptional().ifPresent(seed -> {
            output.write(" using seed ");
            output.write(seed);
        });
        output.newLine();
        Counterexample<A> counterexample = result.getRefinedCounterexample()
                .match(__ -> result.getCounterexample(), RefinedCounterexample::getCounterexample);
        output.write("Counterexample: ");
        output.write(prettyPrinter.apply(counterexample.getSample()));
        output.newLine();
        result.getRefinedCounterexample().toOptional().ifPresent(rc -> {
            output.indent();
            output.write("(refined in ");
            output.write(rc.getShrinkCount());
            output.write(rc.getShrinkCount() == 1 ? " shrink" : " shrinks");
            output.write(" from original: ");
            output.write(prettyPrinter.apply(result.getCounterexample().getSample()));
            output.write(")");
            output.newLine();
        });
        writeEvalFailure(output, counterexample.getFailure(), 0);
        return output.render();
    }

    private <A, P> String renderReportForUnproved(ReportSettings settings, ReportData<A, P> reportData, ExistentialTestResult.Unproved<A> result) {
        return "Property '" + reportData.getProp().getName() + "' remains unproved with the given data";
    }

    private <A, P> String renderReportForExhausted(ReportSettings settings, ReportData<A, P> reportData, Abnormal.Exhausted<A> result) {
        return "Supply failure";
    }

    private <A, P> String renderReportForError(ReportSettings settings, ReportData<A, P> reportData, Abnormal.Error<A> result) {
        return "Test threw an error: " + result.getError();
    }

    private <A, P> String renderReportForTimedOut(ReportSettings settings, ReportData<A, P> reportData, Abnormal.TimedOut<A> result) {
        return "Timed out";
    }

    private <A, P> String renderReportForInterrupted(ReportSettings settings, ReportData<A, P> reportData, Abnormal.Interrupted<A> result) {
        return "Interrupted";
    }

    private void writeEvalFailure(MutableReportBuilder output, EvalFailure failure, int depth) {
        writeProperty(output, failure.getPropertyName(), depth);
        writeReasons(output, failure.getReasons(), depth);
        output.indent();
        try {
            for (Cause cause : failure.getCauses()) {
                cause.match(explanation -> {
                            writeExplanation(output, explanation, depth + 1);
                            return UNIT;
                        },
                        propertyFailed -> {
                            writeEvalFailure(output, propertyFailed.getFailure(), depth + 1);
                            return UNIT;
                        });
            }
        } finally {
            output.unindent();
        }
    }

    private void writeExplanation(MutableReportBuilder output, Cause.Explanation explanation, int depth) {
        writeProperty(output, explanation.getPropertyName(), depth);
        writeReasons(output, explanation.getReasons(), depth);
    }

    private void writeProperty(MutableReportBuilder output, Named property, int depth) {
        if (depth > 0) {
            output.write("- ");
        }
        output.write("Property: \"");
        output.write(property.getName());
        output.write("\"");
        output.newLine();
    }

    private void writeReasons(MutableReportBuilder output, Reasons reasons, int depth) {
        if (depth > 0) {
            output.write("  ");
        }
        ImmutableNonEmptyVector<String> items = reasons.getItems();
        if (items.size() == 1) {
            output.write("Reason: ");
        } else {
            output.write("Reasons: ");
        }
        boolean inner = false;
        for (String reason : items) {
            if (inner) {
                output.write("; ");
            } else {
                inner = true;
            }
            output.write(reason);
        }
        output.newLine();
    }
}
