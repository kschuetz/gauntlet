package dev.marksman.gauntlet;

public interface ReportRenderer {
    <A> String renderForFalsified(ReportSettings settings, ReportData<A> reportData, TestResult.Falsified<A> result);
}
