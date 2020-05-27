package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct4;
import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;

public abstract class Abnormal<A> implements CoProduct4<Abnormal.Error<A>, Abnormal.Exhausted<A>,
        Abnormal.TimedOut<A>, Abnormal.Interrupted<A>, Abnormal<A>> {

    public static <A> Error<A> error(A errorSample, Throwable error, int successCount) {
        return new Error<>(errorSample, error, successCount);
    }

    public static <A> Exhausted<A> exhausted(SupplyFailure supplyFailure, int successCount) {
        return new Exhausted<>(supplyFailure, successCount);
    }

    public static <A> TimedOut<A> timedOut(Duration duration, int successCount) {
        return new TimedOut<>(duration, successCount);
    }

    public static <A> Interrupted<A> interrupted(Maybe<String> message, int successCount) {
        return new Interrupted<>(message, successCount);
    }

    public Abnormal<A> addToSuccessCount(int n) {
        // TODO
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Abnormal)) return false;
        final Abnormal<?> other = (Abnormal<?>) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Abnormal;
    }

    public int hashCode() {
        return 1;
    }

    // An exception was thrown from a test
    public static final class Error<A> extends Abnormal<A> {
        private final A errorSample;
        private final Throwable error;
        private final int successCount;

        private Error(A errorSample, Throwable error, int successCount) {
            this.errorSample = errorSample;
            this.error = error;
            this.successCount = successCount;
        }

        @Override
        public <R> R match(Fn1<? super Error<A>, ? extends R> aFn, Fn1<? super Exhausted<A>, ? extends R> bFn, Fn1<? super TimedOut<A>, ? extends R> cFn, Fn1<? super Interrupted<A>, ? extends R> dFn) {
            return aFn.apply(this);
        }

        public int getSuccessCount() {
            return this.successCount;
        }

        public A getErrorSample() {
            return this.errorSample;
        }

        public Throwable getError() {
            return this.error;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "errorSample=" + errorSample +
                    ", error=" + error +
                    ", successCount=" + successCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Error<?> error1 = (Error<?>) o;

            if (successCount != error1.successCount) return false;
            if (errorSample != null ? !errorSample.equals(error1.errorSample) : error1.errorSample != null)
                return false;
            return error.equals(error1.error);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (errorSample != null ? errorSample.hashCode() : 0);
            result = 31 * result + error.hashCode();
            result = 31 * result + successCount;
            return result;
        }
    }

    // Generator encountered a supply failure before a case was falsified
    public static final class Exhausted<A> extends Abnormal<A> {
        private final SupplyFailure supplyFailure;
        private final int successCount;

        private Exhausted(SupplyFailure supplyFailure, int successCount) {
            this.successCount = successCount;
            this.supplyFailure = supplyFailure;
        }

        @Override
        public <R> R match(Fn1<? super Error<A>, ? extends R> aFn, Fn1<? super Exhausted<A>, ? extends R> bFn, Fn1<? super TimedOut<A>, ? extends R> cFn, Fn1<? super Interrupted<A>, ? extends R> dFn) {
            return bFn.apply(this);
        }

        public SupplyFailure getSupplyFailure() {
            return this.supplyFailure;
        }

        public int getSuccessCount() {
            return successCount;
        }

        @Override
        public String toString() {
            return "SupplyFailed{" +
                    "supplyFailure=" + supplyFailure +
                    ", successCount=" + successCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Exhausted<?> that = (Exhausted<?>) o;

            if (successCount != that.successCount) return false;
            return supplyFailure.equals(that.supplyFailure);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + supplyFailure.hashCode();
            result = 31 * result + successCount;
            return result;
        }
    }

    // It took too long to run all samples
    public static final class TimedOut<A> extends Abnormal<A> {
        private final Duration duration;
        private final int successCount;

        private TimedOut(Duration duration, int successCount) {
            this.duration = duration;
            this.successCount = successCount;
        }

        @Override
        public <R> R match(Fn1<? super Error<A>, ? extends R> aFn, Fn1<? super Exhausted<A>, ? extends R> bFn, Fn1<? super TimedOut<A>, ? extends R> cFn, Fn1<? super Interrupted<A>, ? extends R> dFn) {
            return cFn.apply(this);
        }

        public int getSuccessCount() {
            return this.successCount;
        }

        public Duration getDuration() {
            return this.duration;
        }

        @Override
        public String toString() {
            return "TimedOut{" +
                    "duration=" + duration +
                    ", successCount=" + successCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            TimedOut<?> timedOut = (TimedOut<?>) o;

            if (successCount != timedOut.successCount) return false;
            return duration.equals(timedOut.duration);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + duration.hashCode();
            result = 31 * result + successCount;
            return result;
        }
    }

    // An InterruptedException was thrown while running the test
    public static final class Interrupted<A> extends Abnormal<A> {
        private final Maybe<String> message;
        private final int successCount;

        private Interrupted(Maybe<String> message, int successCount) {
            this.successCount = successCount;
            this.message = message;
        }

        @Override
        public <R> R match(Fn1<? super Error<A>, ? extends R> aFn, Fn1<? super Exhausted<A>, ? extends R> bFn, Fn1<? super TimedOut<A>, ? extends R> cFn, Fn1<? super Interrupted<A>, ? extends R> dFn) {
            return dFn.apply(this);
        }

        public int getSuccessCount() {
            return this.successCount;
        }

        public Maybe<String> getMessage() {
            return this.message;
        }

        @Override
        public String toString() {
            return "Interrupted{" +
                    "message=" + message +
                    ", successCount=" + successCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Interrupted<?> that = (Interrupted<?>) o;

            if (successCount != that.successCount) return false;
            return message.equals(that.message);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + message.hashCode();
            result = 31 * result + successCount;
            return result;
        }
    }
}
