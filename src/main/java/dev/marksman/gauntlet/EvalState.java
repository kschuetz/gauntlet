package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public final class EvalState implements CoProduct3<Success, Failure, Errors, EvalState> {
    private static final EvalState SUCCESS = new EvalState(Choice3.a(Success.success()));

    private final Choice3<Success, Failure, Errors> underlying;

    @Override
    public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super Failure, ? extends R> bFn, Fn1<? super Errors, ? extends R> cFn) {
        return underlying.match(aFn, bFn, cFn);
    }

    public static EvalState success() {
        return SUCCESS;
    }

    public static EvalState failure(Failure failure) {
        return new EvalState(Choice3.b(failure));
    }

    public static EvalState errors(Errors errors) {
        return new EvalState(Choice3.c(errors));
    }
}
