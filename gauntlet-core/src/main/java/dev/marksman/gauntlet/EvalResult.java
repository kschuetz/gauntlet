package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = PRIVATE)
public final class EvalResult implements CoProduct2<Success, Failure, EvalResult> {
    private static final EvalResult SUCCESS = new EvalResult(Choice2.a(Success.success()));

    private final Choice2<Success, Failure> underlying;

    public boolean isSuccess() {
        return match(__ -> true, __ -> false);
    }

    public boolean isFailure() {
        return match(__ -> false, __ -> true);
    }

    @Override
    public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super Failure, ? extends R> bFn) {
        return underlying.match(aFn, bFn);
    }

    public static EvalResult success() {
        return SUCCESS;
    }

    public static EvalResult evalResult(Success success) {
        return SUCCESS;
    }

    public static EvalResult evalResult(Failure failure) {
        return new EvalResult(Choice2.b(failure));
    }

}
