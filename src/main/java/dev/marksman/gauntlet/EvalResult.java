package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static lombok.AccessLevel.PRIVATE;

public abstract class EvalResult implements CoProduct3<EvalResult.Pass, EvalResult.Fail, EvalResult.Error, EvalResult> {

    public abstract boolean isSuccess();

    public boolean isFailure() {
        return !isSuccess();
    }

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
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn, Fn1<? super Error, ? extends R> cFn) {
            return aFn.apply(this);
        }
    }

    public static abstract class Failure extends EvalResult {
        @Override
        public boolean isSuccess() {
            return false;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor(access = PRIVATE)
    @Value
    public static class Fail extends Failure {
        private final ImmutableNonEmptyFiniteIterable<String> failureReasons;
        private final ImmutableFiniteIterable<Failure> causes;

        public Fail addCause(Failure failure) {
            return new Fail(failureReasons, causes.append(failure));
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn, Fn1<? super Error, ? extends R> cFn) {
            return bFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor(access = PRIVATE)
    @Value
    public static class Error extends Failure {
        private final ImmutableNonEmptyFiniteIterable<Throwable> errors;

        public Error addError(Throwable error) {
            return new Error(errors.append(error));
        }

        public Error addErrors(ImmutableFiniteIterable<Throwable> errors) {
            return new Error(this.errors.concat(errors));
        }

        @Override
        public <R> R match(Fn1<? super Pass, ? extends R> aFn, Fn1<? super Fail, ? extends R> bFn, Fn1<? super Error, ? extends R> cFn) {
            return cFn.apply(this);
        }
    }

    public static EvalResult pass() {
        return Pass.INSTANCE;
    }

    public static EvalResult fail(ImmutableNonEmptyFiniteIterable<String> reasons,
                                  ImmutableFiniteIterable<Failure> causes) {
        return new Fail(reasons, causes);
    }

    public static EvalResult fail(String reason) {
        return new Fail(Vector.of(reason), emptyImmutableFiniteIterable());
    }

    public static EvalResult error(Throwable error) {
        return new Error(Vector.of(error));
    }
}
