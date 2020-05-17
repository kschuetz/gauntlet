package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct8;
import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public abstract class TestResult<A> implements CoProduct8<TestResult.Passed<A>, TestResult.Proved<A>,
        TestResult.Falsified<A>, TestResult.Unproved<A>, TestResult.SupplyFailed<A>, TestResult.Error<A>,
        TestResult.TimedOut<A>, TestResult.Interrupted<A>, TestResult<A>> {

    public static <A> Passed<A> passed(int successCount) {
        return new Passed<>(successCount);
    }

    public static <A> Proved<A> proved(A passedSample, int counterexampleCount) {
        return new Proved<>(passedSample, counterexampleCount);
    }

    public static <A> Falsified<A> falsified(Counterexample<A> counterexample, int successCount) {
        return new Falsified<>(counterexample, successCount, nothing());
    }

    public static <A> Falsified<A> falsified(Counterexample<A> counterexample, int successCount,
                                             Maybe<RefinedCounterexample<A>> refinedCounterexample) {
        return new Falsified<>(counterexample, successCount, refinedCounterexample);
    }

    public static <A> Unproved<A> unproved(int counterexampleCount) {
        return new Unproved<>(counterexampleCount);
    }

    public static <A> SupplyFailed<A> supplyFailed(SupplyFailure supplyFailure, int successCount) {
        return new SupplyFailed<>(supplyFailure, successCount);
    }

    public static <A> Error<A> error(A errorSample, Throwable error, int successCount) {
        return new Error<>(errorSample, error, successCount);
    }

    public static <A> TimedOut<A> timedOut(Duration duration, int successCount) {
        return new TimedOut<>(duration, successCount);
    }

    public static <A> Interrupted<A> interrupted(Maybe<String> message, int successCount) {
        return new Interrupted<>(message, successCount);
    }

    public abstract boolean isSuccess();

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TestResult)) return false;
        final TestResult<?> other = (TestResult<?>) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TestResult;
    }

    public int hashCode() {
        return 1;
    }

    // All cases succeeded
    public static final class Passed<A> extends TestResult<A> {
        private final int successCount;

        private Passed(int successCount) {
            this.successCount = successCount;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        public int getSuccessCount() {
            return successCount;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return aFn.apply(this);
        }

        public Passed<A> combine(Passed<A> other) {
            return new Passed<>(successCount + other.getSuccessCount());
        }

        public Falsified<A> combine(Falsified<A> other) {
            return new Falsified<>(other.getCounterexample(), other.getSuccessCount() + successCount, other.getRefinedCounterexample());
        }

        public Error<A> combine(Error<A> other) {
            return new Error<>(other.getErrorSample(), other.getError(), other.getSuccessCount() + successCount);
        }

        public TimedOut<A> combine(TimedOut<A> other) {
            return new TimedOut<>(other.getDuration(), other.getSuccessCount() + successCount);
        }

        public Interrupted<A> combine(Interrupted<A> other) {
            return new Interrupted<>(other.getMessage(), other.getSuccessCount() + successCount);
        }

        @Override
        public String toString() {
            return "Passed{" +
                    "successCount=" + successCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Passed<?> passed = (Passed<?>) o;

            return successCount == passed.successCount;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + successCount;
            return result;
        }
    }

    // A case was found that proved the property
    public static final class Proved<A> extends TestResult<A> {
        private final A passedSample;
        private final int counterexampleCount;

        private Proved(A passedSample, int counterexampleCount) {
            this.passedSample = passedSample;
            this.counterexampleCount = counterexampleCount;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return bFn.apply(this);
        }

        public A getPassedSample() {
            return this.passedSample;
        }

        public int getCounterexampleCount() {
            return this.counterexampleCount;
        }

        @Override
        public String toString() {
            return "Proved{" +
                    "passedSample=" + passedSample +
                    ", counterexampleCount=" + counterexampleCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Proved<?> proved = (Proved<?>) o;

            if (counterexampleCount != proved.counterexampleCount) return false;
            return passedSample.equals(proved.passedSample);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + passedSample.hashCode();
            result = 31 * result + counterexampleCount;
            return result;
        }
    }

    // A case was found that falsified the property
    public static final class Falsified<A> extends TestResult<A> {
        private final Counterexample<A> counterexample;
        private final int successCount;
        private final Maybe<RefinedCounterexample<A>> refinedCounterexample;

        private Falsified(Counterexample<A> counterexample, int successCount, Maybe<RefinedCounterexample<A>> refinedCounterexample) {
            this.successCount = successCount;
            this.counterexample = counterexample;
            this.refinedCounterexample = refinedCounterexample;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        public Falsified<A> withRefinedCounterexample(RefinedCounterexample<A> refinedCounterexample) {
            return new Falsified<>(counterexample, successCount, just(refinedCounterexample));
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return cFn.apply(this);
        }

        public int getSuccessCount() {
            return this.successCount;
        }

        public Counterexample<A> getCounterexample() {
            return this.counterexample;
        }

        public Maybe<RefinedCounterexample<A>> getRefinedCounterexample() {
            return this.refinedCounterexample;
        }

        @Override
        public String toString() {
            return "Falsified{" +
                    "counterexample=" + counterexample +
                    ", successCount=" + successCount +
                    ", refinedCounterexample=" + refinedCounterexample +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Falsified<?> falsified = (Falsified<?>) o;

            if (successCount != falsified.successCount) return false;
            if (!counterexample.equals(falsified.counterexample)) return false;
            return refinedCounterexample.equals(falsified.refinedCounterexample);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + successCount;
            result = 31 * result + counterexample.hashCode();
            result = 31 * result + refinedCounterexample.hashCode();
            return result;
        }
    }

    // No cases were found that prove the property
    public static final class Unproved<A> extends TestResult<A> {
        private final int counterexampleCount;

        private Unproved(int counterexampleCount) {
            this.counterexampleCount = counterexampleCount;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return dFn.apply(this);
        }

        public int getCounterexampleCount() {
            return this.counterexampleCount;
        }

        public Unproved<A> combine(Unproved<A> other) {
            return new Unproved<>(counterexampleCount + other.getCounterexampleCount());
        }

        public Proved<A> combine(Proved<A> other) {
            return new Proved<>(other.getPassedSample(), counterexampleCount + other.getCounterexampleCount());
        }

        public Error<A> combine(Error<A> other) {
            return other;
        }

        public TimedOut<A> combine(TimedOut<A> other) {
            return other;
        }

        public Interrupted<A> combine(Interrupted<A> other) {
            return other;
        }

        @Override
        public String toString() {
            return "Unproved{" +
                    "counterexampleCount=" + counterexampleCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Unproved<?> unproved = (Unproved<?>) o;

            return counterexampleCount == unproved.counterexampleCount;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + counterexampleCount;
            return result;
        }
    }

    // Generator encountered a supply failure before a case was falsified
    public static final class SupplyFailed<A> extends TestResult<A> {
        private final SupplyFailure supplyFailure;
        private final int successCount;

        private SupplyFailed(SupplyFailure supplyFailure, int successCount) {
            this.successCount = successCount;
            this.supplyFailure = supplyFailure;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return eFn.apply(this);
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

            SupplyFailed<?> that = (SupplyFailed<?>) o;

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

    // An exception was thrown from a test
    public static final class Error<A> extends TestResult<A> {
        private final A errorSample;
        private final Throwable error;
        private final int successCount;

        private Error(A errorSample, Throwable error, int successCount) {
            this.errorSample = errorSample;
            this.error = error;
            this.successCount = successCount;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return fFn.apply(this);
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

    // It took too long to run all samples
    public static final class TimedOut<A> extends TestResult<A> {
        private final Duration duration;
        private final int successCount;

        private TimedOut(Duration duration, int successCount) {
            this.duration = duration;
            this.successCount = successCount;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return gFn.apply(this);
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
    public static final class Interrupted<A> extends TestResult<A> {
        private final Maybe<String> message;
        private final int successCount;

        private Interrupted(Maybe<String> message, int successCount) {
            this.successCount = successCount;
            this.message = message;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return hFn.apply(this);
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
