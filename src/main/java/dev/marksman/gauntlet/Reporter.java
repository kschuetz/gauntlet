package dev.marksman.gauntlet;

public interface Reporter {
    <A> void reportOutcome(ReportData<A> reportData);
}
