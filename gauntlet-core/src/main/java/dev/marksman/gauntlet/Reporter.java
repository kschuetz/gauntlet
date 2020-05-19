package dev.marksman.gauntlet;

public interface Reporter {
    <A, P> void report(ReportSettings reportSettings, ReportRenderer renderer, ReportData<A, P> reportData);
}
