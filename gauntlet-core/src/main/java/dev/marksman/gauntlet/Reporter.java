package dev.marksman.gauntlet;

public interface Reporter {
    <A> void report(ReportData<A> reportData);
}
