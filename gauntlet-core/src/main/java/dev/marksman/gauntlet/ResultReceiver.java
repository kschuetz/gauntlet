package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;

interface ResultReceiver {
    boolean shouldRun(int sampleIndex);

    void reportResult(int sampleIndex, Either<Throwable, EvalResult> result);
}
