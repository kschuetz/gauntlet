package dev.marksman.gauntlet.filter;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;


public abstract class FilterResult implements CoProduct2<FilterResult.Pass, FilterResult.Fail, FilterResult> {

    public abstract boolean isSuccess();

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class Pass extends FilterResult {
        private static Pass INSTANCE = new Pass();

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
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Fail extends FilterResult {
        private final Maybe<String> filterName;

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    public static FilterResult pass() {
        return Pass.INSTANCE;
    }

    public static FilterResult fail(Maybe<String> filterName) {
        return new Fail(filterName);
    }

}
