package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Either;

interface ResultReceiver {
    boolean shouldRun(int sampleIndex);

    void reportResult(int sampleIndex, Either<Throwable, EvalResult> result);
}
