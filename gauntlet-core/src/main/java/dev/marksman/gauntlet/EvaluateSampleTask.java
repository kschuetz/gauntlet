package dev.marksman.gauntlet;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;

public final class EvaluateSampleTask<A> implements Runnable {
    private final ResultReceiver receiver;
    private final Prop<A> property;
    private final int sampleIndex;
    private final A sample;

    private EvaluateSampleTask(ResultReceiver receiver, Prop<A> property, int sampleIndex, A sample) {
        this.receiver = receiver;
        this.property = property;
        this.sampleIndex = sampleIndex;
        this.sample = sample;
    }

    public static <A> EvaluateSampleTask<A> evaluateSampleTask(ResultReceiver receiver, Prop<A> property, int sampleIndex, A sample) {
        return new EvaluateSampleTask<>(receiver, property, sampleIndex, sample);
    }

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
}
