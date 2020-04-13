package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static dev.marksman.gauntlet.Reasons.reasons;
import static lombok.AccessLevel.PRIVATE;

public abstract class SimpleResult implements CoProduct2<SimpleResult.Pass, SimpleResult.Fail, SimpleResult> {

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Pass extends SimpleResult {
        private static Pass INSTANCE = new Pass();

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Fail extends SimpleResult {
        Reasons reasons;

        public String getPrimaryReason() {
            return reasons.getPrimary();
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    public static Pass pass() {
        return Pass.INSTANCE;
    }

    public static Fail fail(String primaryReason) {
        return new Fail(reasons(primaryReason));
    }

    public static Fail fail(Reasons reasons) {
        return new Fail(reasons);
    }

}
