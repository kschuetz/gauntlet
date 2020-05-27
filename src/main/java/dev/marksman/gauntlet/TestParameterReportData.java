package dev.marksman.gauntlet;

public final class TestParameterReportData {
    private final String testParameterValue;
    private final IndexInGroup indexInGroup;

    static TestParameterReportData testParameterReportData(String testParameterValue, IndexInGroup indexInGroup) {
        return new TestParameterReportData(testParameterValue, indexInGroup);
    }

    private TestParameterReportData(String testParameterValue, IndexInGroup indexInGroup) {
        this.testParameterValue = testParameterValue;
        this.indexInGroup = indexInGroup;
    }

    public String getTestParameterValue() {
        return testParameterValue;
    }

    public IndexInGroup getIndexInGroup() {
        return indexInGroup;
    }
}
