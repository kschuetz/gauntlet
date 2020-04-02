package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.TestTaskResult.error;
import static dev.marksman.gauntlet.TestTaskResult.skip;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSampleTask<A> implements Runnable {
    private final Context context;
    private final TestResultReceiver receiver;
    private final Prop<A> property;
    private final int sampleIndex;
    private final A input;

    @Override
    public void run() {
        if (receiver.shouldRun(sampleIndex)) {
            try {
                EvalResult evalResult = property.test(context, input);
                receiver.reportResult(sampleIndex, TestTaskResult.testTaskResult(evalResult));
            } catch (Throwable error) {
                receiver.reportResult(sampleIndex, error(error));
            }
        } else {
            receiver.reportResult(sampleIndex, skip());
        }
    }

    public static <A> TestSampleTask<A> testSampleTask(Context context, TestResultReceiver receiver, Prop<A> property, int sampleIndex, A input) {
        return new TestSampleTask<>(context, receiver, property, sampleIndex, input);
    }
}
