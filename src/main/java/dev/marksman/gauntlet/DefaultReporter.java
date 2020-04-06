package dev.marksman.gauntlet;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

public final class DefaultReporter implements Reporter {

    private static final DefaultReporter INSTANCE = new DefaultReporter();

    @Override
    public <A> void reportOutcome(ReportData<A> reportData) {
        reportData.getOutcome().match(__ -> {
                    handleSuccess();
                    return UNIT;
                },
                falsified -> {
                    handleFalsified(reportData, falsified);
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

    private void handleSuccess() {

    }

    private <A> void handleFalsified(ReportData<A> reportData, Outcome.Falsified<A> falsified) {
        throw new AssertionError("Failed property '" + reportData.getProp().getName().getValue() + "' " +
                " with value '" + reportData.getPrettyPrinter().apply(falsified.getFalsifiedSample()) + "'");
    }

    private <A> void handleError(ReportData<A> reportData, Outcome.Error<A> error) {
        throw new AssertionError("Test threw an error");
    }

    private <A> void handleSupplyFailure(ReportData<A> reportData, Outcome.SupplyFailed<A> supplyFailed) {
        throw new AssertionError("Supply failure");
    }

    private <A> void handleTimeout(ReportData<A> reportData, Outcome.TimedOut<A> timedOut) {
        throw new AssertionError("Timed out");
    }

    private <A> void handleInterrupted(ReportData<A> reportData, Outcome.Interrupted<A> interrupted) {
        throw new AssertionError("Interrupted");
    }

    public static DefaultReporter defaultReporter() {
        return INSTANCE;
    }
}
