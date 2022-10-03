package software.kes.gauntlet;

public interface Reporter {
    <A> void report(ReportSettings reportSettings, ReportRenderer renderer, ReportData<A> reportData);
}
