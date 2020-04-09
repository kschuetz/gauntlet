package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;

public abstract class EvalResult implements CoProduct2<EvalSuccess, EvalFailure, EvalResult> {

    public abstract boolean isSuccess();

    public abstract boolean isFailure();

}
