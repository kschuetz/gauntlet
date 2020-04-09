package dev.marksman.gauntlet;

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
        throw new AssertionError("Failed property '" + reportData.getProp().getName() + "' " +
                "with value '" + reportData.getPrettyPrinter().apply(falsified.getCounterexample().getSample()) + "'. " +
                "reasons: " + falsified.getCounterexample().getFailure().getFailureReasons());
    }

    private <A> void handleUnproved(ReportData<A> reportData, TestResult.Unproved<A> proved) {
        throw new AssertionError("Property '" + reportData.getProp().getName() + "' " +
                " remains unproved with the given data");
    }

    private <A> void handleError(ReportData<A> reportData, TestResult.Error<A> error) {
        throw new AssertionError("Test threw an error");
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

    public static DefaultReporter defaultReporter() {
        return INSTANCE;
    }
}
