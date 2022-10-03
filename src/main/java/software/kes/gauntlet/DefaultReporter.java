package software.kes.gauntlet;

import static software.kes.gauntlet.VerbosityLevel.EXTRA;

public final class DefaultReporter implements Reporter {
    private static final DefaultReporter INSTANCE = new DefaultReporter();

    public static DefaultReporter defaultReporter() {
        return INSTANCE;
    }

    @Override
    public <A> void report(ReportSettings reportSettings, ReportRenderer renderer, ReportData<A> reportData) {
        if (reportData.getResult().isSuccess()) {
            if (reportSettings.getSuccessVerbosity() == EXTRA) {
                System.out.println(renderer.renderReport(reportSettings, reportData));
            }
        } else {
            throw new AssertionError(renderer.renderReport(reportSettings, reportData));
        }

    }
}
