package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Wither;

import static lombok.AccessLevel.PRIVATE;

public abstract class EvalResult implements CoProduct2<EvalResult.Pass, EvalResult.Fail, EvalResult> {

    public abstract boolean isSuccess();

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor(access = PRIVATE)
    @Value
    public static class Pass extends EvalResult {
        private static final Pass INSTANCE = new Pass();

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor(access = PRIVATE)
    @Value
    @Wither
    public static class Fail extends EvalResult {
        private final ImmutableNonEmptyFiniteIterable<String> failureReasons;

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    public static EvalResult pass() {
        return Pass.INSTANCE;
    }

    public static EvalResult fail(ImmutableNonEmptyFiniteIterable<String> reasons) {
        return new Fail(reasons);
    }

    public static EvalResult fail(String reason) {
        return new Fail(Vector.of(reason));
    }
}
