package dev.marksman.gauntlet;

public final class PrettyPrintParameters {
    private static final PrettyPrintParameters DEFAULT = new PrettyPrintParameters(VerbosityLevel.NORMAL);

    private final VerbosityLevel verbosityLevel;

    private PrettyPrintParameters(VerbosityLevel verbosityLevel) {
        this.verbosityLevel = verbosityLevel;
    }

    public static PrettyPrintParameters prettyPrintParameters(VerbosityLevel verbosityLevel) {
        return new PrettyPrintParameters(verbosityLevel);
    }

    public static PrettyPrintParameters defaultPrettyPrintParameters() {
        return DEFAULT;
    }

    public VerbosityLevel getVerbosityLevel() {
        return verbosityLevel;
    }
}
