package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
class ConcreteReportSettings implements ReportSettings {
    VerbosityLevel failureVerbosity;
    VerbosityLevel successVerbosity;

    @Override
    public ReportSettings withSuccessVerbosity(VerbosityLevel successVerbosity) {
        return concreteReportSettings(failureVerbosity, successVerbosity);
    }

    @Override
    public ReportSettings withFailureVerbosity(VerbosityLevel failureVerbosity) {
        return concreteReportSettings(failureVerbosity, successVerbosity);
    }

    static ReportSettings concreteReportSettings(VerbosityLevel failureVerbosity, VerbosityLevel successVerbosity) {
        return new ConcreteReportSettings(failureVerbosity, successVerbosity);
    }

}
