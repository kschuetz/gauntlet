package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

public abstract class Cause implements CoProduct2<Cause.Explanation, Cause.PropertyFailed, Cause> {

    public static Explanation explanation(Named propertyName, Reasons reasons) {
        return new Explanation(propertyName, reasons);
    }

    public static PropertyFailed propertyFailed(EvalFailure failure) {
        return new PropertyFailed(failure);
    }

    public static Explanation propertyPassed(Named propertyName) {
        return new Explanation(propertyName, Reasons.reasons("passed"));
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Explanation extends Cause {
        Named propertyName;
        Reasons reasons;

        @Override
        public <R> R match(Fn1<? super Explanation, ? extends R> aFn, Fn1<? super PropertyFailed, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class PropertyFailed extends Cause {
        EvalFailure failure;

        @Override
        public <R> R match(Fn1<? super Explanation, ? extends R> aFn, Fn1<? super PropertyFailed, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

}
