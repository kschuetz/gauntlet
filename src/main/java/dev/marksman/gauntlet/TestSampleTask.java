package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.TestTaskResult.error;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSampleTask<A> implements Runnable {
    private final Context context;
    private final TestResultReceiver receiver;
    private final Prop<A> property;
    private final int sampleIndex;
    private final A sample;

    @Override
    public void run() {
        if (receiver.shouldRun(sampleIndex)) {
            try {
                EvalResult evalResult = property.test(context, sample);
                receiver.reportResult(sampleIndex, TestTaskResult.testTaskResult(evalResult));
            } catch (Throwable error) {
                receiver.reportResult(sampleIndex, error(error));
            }
        }
    }

    public static <A> TestSampleTask<A> testSampleTask(Context context, TestResultReceiver receiver, Prop<A> property, int sampleIndex, A sample) {
        return new TestSampleTask<>(context, receiver, property, sampleIndex, sample);
    }
}
