package dev.marksman.gauntlet;

public interface ReportRenderer {
    <A> String renderReport(ReportSettings settings, ReportData<A> reportData);
}
