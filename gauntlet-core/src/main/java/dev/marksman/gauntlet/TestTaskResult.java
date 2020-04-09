package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public final class TestTaskResult implements CoProduct3<EvalSuccess,
        EvalFailure, Throwable, TestTaskResult> {

    private static final TestTaskResult SUCCESS = new TestTaskResult(Choice3.a(evalSuccess()));

    private final Choice3<EvalSuccess,
            EvalFailure, Throwable> underlying;

    public boolean isFailure() {
        return match(__ -> false, __ -> true, __ -> false);
    }

    public boolean isError() {
        return match(__ -> false, __ -> false, __ -> true);
    }

    @Override
    public <R> R match(Fn1<? super EvalSuccess, ? extends R> aFn, Fn1<? super EvalFailure, ? extends R> bFn, Fn1<? super Throwable, ? extends R> cFn) {
        return underlying.match(aFn, bFn, cFn);
    }

    public static TestTaskResult success() {
        return SUCCESS;
    }

    public static TestTaskResult testTaskResult(EvalResult evalResult) {
        return new TestTaskResult(evalResult.match(Choice3::a, Choice3::b));
    }

    public static TestTaskResult error(Throwable error) {
        return new TestTaskResult(Choice3.c(error));
    }

}
