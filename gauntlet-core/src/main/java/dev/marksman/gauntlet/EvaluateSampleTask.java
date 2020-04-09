package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;

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
                EvalResult evalResult = property.evaluate(sample);
                receiver.reportResult(sampleIndex, right(evalResult));
            } catch (Exception error) {
                receiver.reportResult(sampleIndex, left(error));
            }
        }
    }

    public static <A> EvaluateSampleTask<A> evaluateSampleTask(ResultReceiver receiver, Prop<A> property, int sampleIndex, A sample) {
        return new EvaluateSampleTask<>(receiver, property, sampleIndex, sample);
    }
}
