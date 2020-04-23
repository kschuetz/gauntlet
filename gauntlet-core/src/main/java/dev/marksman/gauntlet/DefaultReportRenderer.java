package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class DefaultReportRenderer implements ReportRenderer {
    @Override
    public <A> String renderForFalsified(ReportSettings settings, ReportData<A> reportData, TestResult.Falsified<A> result) {
        return "TODO";
    }

    public static DefaultReportRenderer defaultReportRenderer() {
        return new DefaultReportRenderer();
    }

}
