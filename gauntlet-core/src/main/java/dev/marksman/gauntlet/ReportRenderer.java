package dev.marksman.gauntlet;

public interface ReportRenderer {
    <A, P> String renderReport(ReportSettings settings, ReportData<A, P> reportData);
}
