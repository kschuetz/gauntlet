package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct8;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;

import java.time.Duration;
import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public abstract class TestResult<A> implements CoProduct8<TestResult.Passed<A>, TestResult.Proved<A>,
        TestResult.Falsified<A>, TestResult.Unproved<A>, TestResult.SupplyFailed<A>, TestResult.Error<A>,
        TestResult.TimedOut<A>, TestResult.Interrupted<A>, TestResult<A>> {

    public static <A> Passed<A> passed(ImmutableVector<A> passedSamples) {
        return new Passed<>(passedSamples);
    }

    public static <A> Proved<A> proved(A passedSample, ImmutableVector<Counterexample<A>> counterexamples) {
        return new Proved<>(passedSample, counterexamples);
    }

    public static <A> Falsified<A> falsified(ImmutableVector<A> passedSamples,
                                             Counterexample<A> counterexample) {
        return new Falsified<>(passedSamples, counterexample, nothing());
    }

    public static <A> Falsified<A> falsified(ImmutableVector<A> passedSamples,
                                             Counterexample<A> counterexample,
                                             Maybe<RefinedCounterexample<A>> refinedCounterexample) {
        return new Falsified<>(passedSamples, counterexample, refinedCounterexample);
    }

    public static <A> Unproved<A> unproved(ImmutableVector<Counterexample<A>> counterexamples) {
        return new Unproved<>(counterexamples);
    }

    public static <A> SupplyFailed<A> supplyFailed(ImmutableVector<A> passedSamples, SupplyFailure supplyFailure) {
        return new SupplyFailed<>(passedSamples, supplyFailure);
    }

    public static <A> Error<A> error(ImmutableVector<A> passedSamples, A errorSample, Throwable error) {
        return new Error<>(passedSamples, errorSample, error);
    }

    public static <A> TimedOut<A> timedOut(ImmutableVector<A> passedSamples, Duration duration) {
        return new TimedOut<>(passedSamples, duration);
    }

    public static <A> Interrupted<A> interrupted(ImmutableVector<A> passedSamples, Maybe<String> message) {
        return new Interrupted<>(passedSamples, message);
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
        private final ImmutableVector<A> passedSamples;

        private Passed(ImmutableVector<A> passedSamples) {
            this.passedSamples = passedSamples;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        public int getSuccessCount() {
            return passedSamples.size();
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return aFn.apply(this);
        }

        public ImmutableVector<A> getPassedSamples() {
            return this.passedSamples;
        }

        public String toString() {
            return "TestResult.Passed(passedSamples=" + this.getPassedSamples() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.Passed)) return false;
            final Passed<?> other = (Passed<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$passedSamples = this.getPassedSamples();
            final Object other$passedSamples = other.getPassedSamples();
            return Objects.equals(this$passedSamples, other$passedSamples);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.Passed;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $passedSamples = this.getPassedSamples();
            result = result * PRIME + ($passedSamples == null ? 43 : $passedSamples.hashCode());
            return result;
        }
    }

    // A case was found that proved the property
    public static final class Proved<A> extends TestResult<A> {
        private final A passedSample;
        private final ImmutableVector<Counterexample<A>> counterexamples;

        private Proved(A passedSample, ImmutableVector<Counterexample<A>> counterexamples) {
            this.passedSample = passedSample;
            this.counterexamples = counterexamples;
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

        public ImmutableVector<Counterexample<A>> getCounterexamples() {
            return this.counterexamples;
        }

        public String toString() {
            return "TestResult.Proved(passedSample=" + this.getPassedSample() + ", counterexamples=" + this.getCounterexamples() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.Proved)) return false;
            final Proved<?> other = (Proved<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$passedSample = this.getPassedSample();
            final Object other$passedSample = other.getPassedSample();
            if (!Objects.equals(this$passedSample, other$passedSample))
                return false;
            final Object this$counterexamples = this.getCounterexamples();
            final Object other$counterexamples = other.getCounterexamples();
            return Objects.equals(this$counterexamples, other$counterexamples);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.Proved;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $passedSample = this.getPassedSample();
            result = result * PRIME + ($passedSample == null ? 43 : $passedSample.hashCode());
            final Object $counterexamples = this.getCounterexamples();
            result = result * PRIME + ($counterexamples == null ? 43 : $counterexamples.hashCode());
            return result;
        }
    }

    // A case was found that falsified the property
    public static final class Falsified<A> extends TestResult<A> {
        private final ImmutableVector<A> passedSamples;
        private final Counterexample<A> counterexample;
        private final Maybe<RefinedCounterexample<A>> refinedCounterexample;

        private Falsified(ImmutableVector<A> passedSamples, Counterexample<A> counterexample, Maybe<RefinedCounterexample<A>> refinedCounterexample) {
            this.passedSamples = passedSamples;
            this.counterexample = counterexample;
            this.refinedCounterexample = refinedCounterexample;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        public int getSuccessCount() {
            return passedSamples.size();
        }

        public Falsified<A> withRefinedCounterexample(RefinedCounterexample<A> refinedCounterexample) {
            return new Falsified<>(passedSamples, counterexample, just(refinedCounterexample));
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return cFn.apply(this);
        }

        public ImmutableVector<A> getPassedSamples() {
            return this.passedSamples;
        }

        public Counterexample<A> getCounterexample() {
            return this.counterexample;
        }

        public Maybe<RefinedCounterexample<A>> getRefinedCounterexample() {
            return this.refinedCounterexample;
        }

        public String toString() {
            return "TestResult.Falsified(passedSamples=" + this.getPassedSamples() + ", counterexample=" + this.getCounterexample() + ", refinedCounterexample=" + this.getRefinedCounterexample() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.Falsified)) return false;
            final Falsified<?> other = (Falsified<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$passedSamples = this.getPassedSamples();
            final Object other$passedSamples = other.getPassedSamples();
            if (!Objects.equals(this$passedSamples, other$passedSamples))
                return false;
            final Object this$counterexample = this.getCounterexample();
            final Object other$counterexample = other.getCounterexample();
            if (!Objects.equals(this$counterexample, other$counterexample))
                return false;
            final Object this$refinedCounterexample = this.getRefinedCounterexample();
            final Object other$refinedCounterexample = other.getRefinedCounterexample();
            return Objects.equals(this$refinedCounterexample, other$refinedCounterexample);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.Falsified;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $passedSamples = this.getPassedSamples();
            result = result * PRIME + ($passedSamples == null ? 43 : $passedSamples.hashCode());
            final Object $counterexample = this.getCounterexample();
            result = result * PRIME + ($counterexample == null ? 43 : $counterexample.hashCode());
            final Object $refinedCounterexample = this.getRefinedCounterexample();
            result = result * PRIME + ($refinedCounterexample == null ? 43 : $refinedCounterexample.hashCode());
            return result;
        }
    }

    // No cases were found that prove the property
    public static final class Unproved<A> extends TestResult<A> {
        private final ImmutableVector<Counterexample<A>> counterexamples;

        private Unproved(ImmutableVector<Counterexample<A>> counterexamples) {
            this.counterexamples = counterexamples;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return dFn.apply(this);
        }

        public ImmutableVector<Counterexample<A>> getCounterexamples() {
            return this.counterexamples;
        }

        public String toString() {
            return "TestResult.Unproved(counterexamples=" + this.getCounterexamples() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.Unproved)) return false;
            final Unproved<?> other = (Unproved<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$counterexamples = this.getCounterexamples();
            final Object other$counterexamples = other.getCounterexamples();
            return Objects.equals(this$counterexamples, other$counterexamples);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.Unproved;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $counterexamples = this.getCounterexamples();
            result = result * PRIME + ($counterexamples == null ? 43 : $counterexamples.hashCode());
            return result;
        }
    }

    // Generator encountered a supply failure before a case was falsified
    public static final class SupplyFailed<A> extends TestResult<A> {
        private final ImmutableVector<A> passedSamples;
        private final SupplyFailure supplyFailure;

        private SupplyFailed(ImmutableVector<A> passedSamples, SupplyFailure supplyFailure) {
            this.passedSamples = passedSamples;
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

        public ImmutableVector<A> getPassedSamples() {
            return this.passedSamples;
        }

        public SupplyFailure getSupplyFailure() {
            return this.supplyFailure;
        }

        public String toString() {
            return "TestResult.SupplyFailed(passedSamples=" + this.getPassedSamples() + ", supplyFailure=" + this.getSupplyFailure() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.SupplyFailed)) return false;
            final SupplyFailed<?> other = (SupplyFailed<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$passedSamples = this.getPassedSamples();
            final Object other$passedSamples = other.getPassedSamples();
            if (!Objects.equals(this$passedSamples, other$passedSamples))
                return false;
            final Object this$supplyFailure = this.getSupplyFailure();
            final Object other$supplyFailure = other.getSupplyFailure();
            return Objects.equals(this$supplyFailure, other$supplyFailure);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.SupplyFailed;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $passedSamples = this.getPassedSamples();
            result = result * PRIME + ($passedSamples == null ? 43 : $passedSamples.hashCode());
            final Object $supplyFailure = this.getSupplyFailure();
            result = result * PRIME + ($supplyFailure == null ? 43 : $supplyFailure.hashCode());
            return result;
        }
    }

    // An exception was thrown from a test
    public static final class Error<A> extends TestResult<A> {
        private final ImmutableVector<A> passedSamples;
        private final A errorSample;
        private final Throwable error;

        private Error(ImmutableVector<A> passedSamples, A errorSample, Throwable error) {
            this.passedSamples = passedSamples;
            this.errorSample = errorSample;
            this.error = error;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return fFn.apply(this);
        }

        public ImmutableVector<A> getPassedSamples() {
            return this.passedSamples;
        }

        public A getErrorSample() {
            return this.errorSample;
        }

        public Throwable getError() {
            return this.error;
        }

        public String toString() {
            return "TestResult.Error(passedSamples=" + this.getPassedSamples() + ", errorSample=" + this.getErrorSample() + ", error=" + this.getError() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.Error)) return false;
            final Error<?> other = (Error<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$passedSamples = this.getPassedSamples();
            final Object other$passedSamples = other.getPassedSamples();
            if (!Objects.equals(this$passedSamples, other$passedSamples))
                return false;
            final Object this$errorSample = this.getErrorSample();
            final Object other$errorSample = other.getErrorSample();
            if (!Objects.equals(this$errorSample, other$errorSample))
                return false;
            final Object this$error = this.getError();
            final Object other$error = other.getError();
            return Objects.equals(this$error, other$error);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.Error;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $passedSamples = this.getPassedSamples();
            result = result * PRIME + ($passedSamples == null ? 43 : $passedSamples.hashCode());
            final Object $errorSample = this.getErrorSample();
            result = result * PRIME + ($errorSample == null ? 43 : $errorSample.hashCode());
            final Object $error = this.getError();
            result = result * PRIME + ($error == null ? 43 : $error.hashCode());
            return result;
        }
    }

    // It took too long to run all samples
    public static final class TimedOut<A> extends TestResult<A> {
        private final ImmutableVector<A> passedSamples;
        private final Duration duration;

        private TimedOut(ImmutableVector<A> passedSamples, Duration duration) {
            this.passedSamples = passedSamples;
            this.duration = duration;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return gFn.apply(this);
        }

        public ImmutableVector<A> getPassedSamples() {
            return this.passedSamples;
        }

        public Duration getDuration() {
            return this.duration;
        }

        public String toString() {
            return "TestResult.TimedOut(passedSamples=" + this.getPassedSamples() + ", duration=" + this.getDuration() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.TimedOut)) return false;
            final TimedOut<?> other = (TimedOut<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$passedSamples = this.getPassedSamples();
            final Object other$passedSamples = other.getPassedSamples();
            if (!Objects.equals(this$passedSamples, other$passedSamples))
                return false;
            final Object this$duration = this.getDuration();
            final Object other$duration = other.getDuration();
            return Objects.equals(this$duration, other$duration);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.TimedOut;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $passedSamples = this.getPassedSamples();
            result = result * PRIME + ($passedSamples == null ? 43 : $passedSamples.hashCode());
            final Object $duration = this.getDuration();
            result = result * PRIME + ($duration == null ? 43 : $duration.hashCode());
            return result;
        }
    }

    // An InterruptedException was thrown while running the test
    public static final class Interrupted<A> extends TestResult<A> {
        private final ImmutableVector<A> passedSamples;
        private final Maybe<String> message;

        private Interrupted(ImmutableVector<A> passedSamples, Maybe<String> message) {
            this.passedSamples = passedSamples;
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

        public ImmutableVector<A> getPassedSamples() {
            return this.passedSamples;
        }

        public Maybe<String> getMessage() {
            return this.message;
        }

        public String toString() {
            return "TestResult.Interrupted(passedSamples=" + this.getPassedSamples() + ", message=" + this.getMessage() + ")";
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof TestResult.Interrupted)) return false;
            final Interrupted<?> other = (Interrupted<?>) o;
            if (!other.canEqual(this)) return false;
            if (!super.equals(o)) return false;
            final Object this$passedSamples = this.getPassedSamples();
            final Object other$passedSamples = other.getPassedSamples();
            if (!Objects.equals(this$passedSamples, other$passedSamples))
                return false;
            final Object this$message = this.getMessage();
            final Object other$message = other.getMessage();
            return Objects.equals(this$message, other$message);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof TestResult.Interrupted;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final Object $passedSamples = this.getPassedSamples();
            result = result * PRIME + ($passedSamples == null ? 43 : $passedSamples.hashCode());
            final Object $message = this.getMessage();
            result = result * PRIME + ($message == null ? 43 : $message.hashCode());
            return result;
        }
    }
}
