package dev.marksman.gauntlet;

class MutableReportBuilder {
    private final StringBuilder output;
    private int indentLevel;
    private boolean wasCurrentLineStarted;

    MutableReportBuilder() {
        indentLevel = 0;
        wasCurrentLineStarted = false;
        output = new StringBuilder();
    }

    public String render() {
        return output.toString();
    }

    public void indent() {
        indentLevel += 1;
    }

    public void unindent() {
        if (indentLevel <= 0) {
            throw new IllegalStateException("cannot unindent");
        }
        indentLevel -= 1;
    }

    public void newLine() {
        output.append('\n');
        wasCurrentLineStarted = false;
    }

    public void write(String s) {
        if (!wasCurrentLineStarted) {
            for (int i = 0; i < indentLevel; i++) {
                output.append('\t');
            }
            wasCurrentLineStarted = true;
        }
        output.append(s);
    }

    public void write(Object obj) {
        write(String.valueOf(obj));
    }

    void writeLine(String line) {
        for (int i = 0; i < indentLevel; i++) {
            output.append('\t');
        }
        output.append(line);
        output.append('\n');
    }

}
