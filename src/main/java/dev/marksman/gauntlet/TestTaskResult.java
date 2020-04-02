package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct4;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public final class TestTaskResult implements CoProduct4<Success, Failure, Skip, Throwable, TestTaskResult> {
    private static final TestTaskResult SUCCESS = new TestTaskResult(Choice4.a(Success.success()));
    private static final TestTaskResult SKIP = new TestTaskResult(Choice4.c(Skip.skip()));

    private final Choice4<Success, Failure, Skip, Throwable> underlying;

    public boolean isFailure() {
        return match(__ -> false, __ -> true, __ -> false, __ -> false);
    }

    @Override
    public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super Failure, ? extends R> bFn, Fn1<? super Skip, ? extends R> cFn, Fn1<? super Throwable, ? extends R> dFn) {
        return underlying.match(aFn, bFn, cFn, dFn);
    }

    public static TestTaskResult success() {
        return SUCCESS;
    }

    public static TestTaskResult testTaskResult(EvalResult evalResult) {
        return evalResult.match(__ -> SUCCESS, TestTaskResult::failure);
    }

    public static TestTaskResult failure(Failure failure) {
        return new TestTaskResult(Choice4.b(failure));
    }

    public static TestTaskResult skip() {
        return SKIP;
    }

    public static TestTaskResult error(Throwable error) {
        return new TestTaskResult(Choice4.d(error));
    }

}
