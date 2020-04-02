package dev.marksman.gauntlet;

public interface TestResultReceiver {
    boolean shouldRun(int sampleIndex);

    void reportResult(int sampleIndex, TestTaskResult result);
}
