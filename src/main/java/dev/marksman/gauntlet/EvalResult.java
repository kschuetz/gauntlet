package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static lombok.AccessLevel.PRIVATE;

public abstract class EvalResult implements CoProduct2<EvalResult.Success, EvalResult.Failure, EvalResult> {

    public abstract boolean isSuccess();

    public boolean isFailure() {
        return !isSuccess();
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor(access = PRIVATE)
    @Value
    public static class Success extends EvalResult {
        private static final Success INSTANCE = new Success();

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super Failure, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor(access = PRIVATE)
    @Value
    public static class Failure extends EvalResult {
        private final Name propertyName;
        private final ImmutableNonEmptyFiniteIterable<String> failureReasons;
        private final ImmutableFiniteIterable<Failure> causes;

        public Failure addCause(Name propertyName, Failure failure) {
            return new Failure(propertyName, failureReasons, causes.append(failure));
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Success, ? extends R> aFn, Fn1<? super Failure, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    public static EvalResult pass() {
        return Success.INSTANCE;
    }

    public static EvalResult fail(Name propertyName,
                                  ImmutableNonEmptyFiniteIterable<String> reasons,
                                  ImmutableFiniteIterable<Failure> causes) {
        return new Failure(propertyName, reasons, causes);
    }

    public static EvalResult fail(Name propertyName, String reason) {
        return new Failure(propertyName, Vector.of(reason), emptyImmutableFiniteIterable());
    }

}
