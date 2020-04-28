package dev.marksman.gauntlet;

import static dev.marksman.gauntlet.ConcreteReportSettings.concreteReportSettings;
import static dev.marksman.gauntlet.VerbosityLevel.NORMAL;
import static dev.marksman.gauntlet.VerbosityLevel.QUIET;

public interface ReportSettings {
    VerbosityLevel getSuccessVerbosity();

    VerbosityLevel getFailureVerbosity();

    ReportSettings withSuccessVerbosity(VerbosityLevel verbosityLevel);

    ReportSettings withFailureVerbosity(VerbosityLevel verbosityLevel);

    static ReportSettings defaultReportSettings() {
        return concreteReportSettings(NORMAL, QUIET);
    }
}