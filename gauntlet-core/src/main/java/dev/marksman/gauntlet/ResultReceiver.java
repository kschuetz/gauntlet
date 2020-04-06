package dev.marksman.gauntlet;

public interface ResultReceiver {
    boolean shouldRun(int sampleIndex);

    void reportResult(int sampleIndex, TestTaskResult result);
}
