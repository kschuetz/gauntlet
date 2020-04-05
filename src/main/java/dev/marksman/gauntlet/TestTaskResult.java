package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public final class TestTaskResult implements CoProduct3<Success, Failure, Throwable, TestTaskResult> {
    private static final TestTaskResult SUCCESS = new TestTaskResult(Choice3.a(Success.success()));

    private final Choice3<Success, Failure, Throwable> underlying;

    public boolean isFailure() {
        return match(__ -> false, __ -> true, __ -> false);
    }

    public boolean isError() {
        return match(__ -> false, __ -> false, __ -> true);
    }

    @Override
    public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super Failure, ? extends R> bFn, Fn1<? super Throwable, ? extends R> cFn) {
        return underlying.match(aFn, bFn, cFn);
    }

    public static TestTaskResult success() {
        return SUCCESS;
    }

    public static TestTaskResult testTaskResult(EvalResult evalResult) {
        return evalResult.match(__ -> SUCCESS, TestTaskResult::failure);
    }

    public static TestTaskResult failure(Failure failure) {
        return new TestTaskResult(Choice3.b(failure));
    }

    public static TestTaskResult error(Throwable error) {
        return new TestTaskResult(Choice3.c(error));
    }

}
