package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode(callSuper = true)
@Value
@AllArgsConstructor(access = PRIVATE)
public class EvalSuccess extends EvalResult {
    private static EvalSuccess INSTANCE = new EvalSuccess();

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public <R> R match(Fn1<? super EvalSuccess, ? extends R> aFn, Fn1<? super EvalFailure, ? extends R> bFn) {
        return aFn.apply(this);
    }

    public static EvalSuccess evalSuccess() {
        return INSTANCE;
    }

}
