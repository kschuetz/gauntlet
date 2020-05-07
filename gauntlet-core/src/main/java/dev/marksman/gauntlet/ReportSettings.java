package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.gauntlet.VerbosityLevel.NORMAL;
import static dev.marksman.gauntlet.VerbosityLevel.QUIET;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportSettings {
    VerbosityLevel failureVerbosity;
    VerbosityLevel successVerbosity;

    public static ReportSettings reportSettings(VerbosityLevel failureVerbosity, VerbosityLevel successVerbosity) {
        return new ReportSettings(failureVerbosity, successVerbosity);
    }

    public static ReportSettings defaultReportSettings() {
        return reportSettings(NORMAL, QUIET);
    }

    public ReportSettings withSuccessVerbosity(VerbosityLevel successVerbosity) {
        return reportSettings(failureVerbosity, successVerbosity);
    }

    public ReportSettings withFailureVerbosity(VerbosityLevel failureVerbosity) {
        return reportSettings(failureVerbosity, successVerbosity);
    }
}
