package software.kes.gauntlet;

public final class ReportSettings {
    private final VerbosityLevel failureVerbosity;
    private final VerbosityLevel successVerbosity;

    private ReportSettings(VerbosityLevel failureVerbosity, VerbosityLevel successVerbosity) {
        this.failureVerbosity = failureVerbosity;
        this.successVerbosity = successVerbosity;
    }

    public static ReportSettings reportSettings(VerbosityLevel failureVerbosity, VerbosityLevel successVerbosity) {
        return new ReportSettings(failureVerbosity, successVerbosity);
    }

    public static ReportSettings defaultReportSettings() {
        return reportSettings(VerbosityLevel.NORMAL, VerbosityLevel.QUIET);
    }

    public ReportSettings withSuccessVerbosity(VerbosityLevel successVerbosity) {
        return reportSettings(failureVerbosity, successVerbosity);
    }

    public ReportSettings withFailureVerbosity(VerbosityLevel failureVerbosity) {
        return reportSettings(failureVerbosity, successVerbosity);
    }

    public VerbosityLevel getFailureVerbosity() {
        return this.failureVerbosity;
    }

    public VerbosityLevel getSuccessVerbosity() {
        return this.successVerbosity;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReportSettings)) return false;
        final ReportSettings other = (ReportSettings) o;
        final Object this$failureVerbosity = this.getFailureVerbosity();
        final Object other$failureVerbosity = other.getFailureVerbosity();
        if (this$failureVerbosity == null ? other$failureVerbosity != null : !this$failureVerbosity.equals(other$failureVerbosity))
            return false;
        final Object this$successVerbosity = this.getSuccessVerbosity();
        final Object other$successVerbosity = other.getSuccessVerbosity();
        return this$successVerbosity == null ? other$successVerbosity == null : this$successVerbosity.equals(other$successVerbosity);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $failureVerbosity = this.getFailureVerbosity();
        result = result * PRIME + ($failureVerbosity == null ? 43 : $failureVerbosity.hashCode());
        final Object $successVerbosity = this.getSuccessVerbosity();
        result = result * PRIME + ($successVerbosity == null ? 43 : $successVerbosity.hashCode());
        return result;
    }

    public String toString() {
        return "ReportSettings(failureVerbosity=" + this.getFailureVerbosity() + ", successVerbosity=" + this.getSuccessVerbosity() + ")";
    }
}
