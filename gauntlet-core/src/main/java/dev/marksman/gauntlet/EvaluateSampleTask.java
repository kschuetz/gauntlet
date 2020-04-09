package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.gauntlet.TestTaskResult.error;
import static dev.marksman.gauntlet.TestTaskResult.testTaskResult;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluateSampleTask<A> implements Runnable {
    private final ResultReceiver receiver;
    private final Prop<A> property;
    private final int sampleIndex;
    private final A sample;

    @Override
    public void run() {
        if (receiver.shouldRun(sampleIndex)) {
            try {
                EvalResult evalResult = property.test(sample);
                receiver.reportResult(sampleIndex, testTaskResult(evalResult));
            } catch (Exception error) {
                receiver.reportResult(sampleIndex, error(error));
            }
        }
    }

    public static <A> EvaluateSampleTask<A> testSampleTask(ResultReceiver receiver, Prop<A> property, int sampleIndex, A sample) {
        return new EvaluateSampleTask<>(receiver, property, sampleIndex, sample);
    }
}
