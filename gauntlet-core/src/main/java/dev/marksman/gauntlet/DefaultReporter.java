package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

public final class DefaultReporter implements Reporter {

    private static final DefaultReporter INSTANCE = new DefaultReporter();

    @Override
    public <A> void report(ReportData<A> reportData) {
        reportData.getResult().match(
                passed -> {
                    handlePassed(reportData, passed);
                    return UNIT;
                },
                proved -> {
                    handleProved(reportData, proved);
                    return UNIT;
                },
                falsified -> {
                    handleFalsified(reportData, falsified);
                    return UNIT;
                },
                unproved -> {
                    handleUnproved(reportData, unproved);
                    return UNIT;
                },
                sf -> {
                    handleSupplyFailure(reportData, sf);
                    return UNIT;
                },
                error -> {
                    handleError(reportData, error);
                    return UNIT;
                },
                timeout -> {
                    handleTimeout(reportData, timeout);
                    return UNIT;
                },
                interrupted -> {
                    handleInterrupted(reportData, interrupted);
                    return UNIT;
                });
    }

    private <A> void handlePassed(ReportData<A> reportData, TestResult.Passed<A> passed) {

    }

    private <A> void handleProved(ReportData<A> reportData, TestResult.Proved<A> proved) {

    }

    private <A> void handleFalsified(ReportData<A> reportData, TestResult.Falsified<A> falsified) {
        String content = buildReportForFalsified(reportData, falsified);
        throw new AssertionError(content);
    }

    private <A> void handleUnproved(ReportData<A> reportData, TestResult.Unproved<A> proved) {
        throw new AssertionError("Property '" + reportData.getProp().getName() + "' " +
                " remains unproved with the given data");
    }

    private <A> void handleError(ReportData<A> reportData, TestResult.Error<A> error) {
        throw new AssertionError("Test threw an error: " + error.getError());
    }

    private <A> void handleSupplyFailure(ReportData<A> reportData, TestResult.SupplyFailed<A> supplyFailed) {
        throw new AssertionError("Supply failure");
    }

    private <A> void handleTimeout(ReportData<A> reportData, TestResult.TimedOut<A> timedOut) {
        throw new AssertionError("Timed out");
    }

    private <A> void handleInterrupted(ReportData<A> reportData, TestResult.Interrupted<A> interrupted) {
        throw new AssertionError("Interrupted");
    }

    // TODO: maybe use a state or writer monad for this
    private <A> String buildReportForFalsified(ReportData<A> reportData, TestResult.Falsified<A> result) {
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
        outputEvalFailure(output, counterexample.getFailure(), 0);
        return output.render();
    }

    private void outputEvalFailure(MutableReportBuilder output, EvalFailure failure, int depth) {
        if (depth > 0) {
            output.write("- ");
        }
        output.write("Property: \"");
        output.write(failure.getPropertyName().getName());
        output.write("\"");
        output.newLine();
        if (depth > 0) {
            output.write("  ");
        }
        ImmutableNonEmptyVector<String> reasons = failure.getFailureReasons().getItems();
        if (reasons.size() == 1) {
            output.write("Reason: ");
        } else {
            output.write("Reasons: ");
        }
        boolean inner = false;
        for (String reason : reasons) {
            if (inner) {
                output.write("; ");
            } else {
                inner = true;
            }
            output.write(reason);
        }
        output.newLine();
        output.indent();
        try {
            for (EvalFailure cause : failure.getCauses()) {
                outputEvalFailure(output, cause, depth + 1);
            }
        } finally {
            output.unindent();
        }
    }

    public static DefaultReporter defaultReporter() {
        return INSTANCE;
    }
}
