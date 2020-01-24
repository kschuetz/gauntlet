package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static dev.marksman.gauntlet.Errors.errors;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public final class EvalResult implements CoProduct3<Success, Failure, Errors, EvalResult> {
    private static final EvalResult SUCCESS = new EvalResult(Choice3.a(Success.success()));

    private final Choice3<Success, Failure, Errors> underlying;

    public boolean isSuccess() {
        return match(__ -> true, __ -> false, __ -> false);
    }

    public boolean isFailure() {
        return match(__ -> false, __ -> true, __ -> false);
    }

    public boolean isError() {
        return match(__ -> false, __ -> false, __ -> true);
    }

    @Override
    public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super Failure, ? extends R> bFn, Fn1<? super Errors, ? extends R> cFn) {
        return underlying.match(aFn, bFn, cFn);
    }

    public static EvalResult success() {
        return SUCCESS;
    }

    public static EvalResult evalResult(Success success) {
        return SUCCESS;
    }

    public static EvalResult evalResult(Failure failure) {
        return new EvalResult(Choice3.b(failure));
    }

    public static EvalResult evalResult(Errors errors) {
        return new EvalResult(Choice3.c(errors));
    }

    public static EvalResult evalResult(Error error) {
        return evalResult(errors(error));
    }

}
