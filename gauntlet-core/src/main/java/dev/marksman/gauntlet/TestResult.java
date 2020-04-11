package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct8;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Duration;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
public abstract class TestResult<A> implements CoProduct8<TestResult.Passed<A>, TestResult.Proved<A>,
        TestResult.Falsified<A>, TestResult.Unproved<A>, TestResult.SupplyFailed<A>, TestResult.Error<A>,
        TestResult.TimedOut<A>, TestResult.Interrupted<A>, TestResult<A>> {

    // All cases succeeded
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Passed<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return aFn.apply(this);
        }

    }

    // A case was found that proved the property
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Proved<A> extends TestResult<A> {
        A passedSample;
        ImmutableVector<Counterexample<A>> counterexamples;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return bFn.apply(this);
        }

    }

    // A case was found that falsified the property
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Falsified<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;
        Counterexample<A> counterexample;
        Maybe<RefinedCounterexample<A>> refinedCounterexample;

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

    }

    // No cases were found that prove the property
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Unproved<A> extends TestResult<A> {
        ImmutableVector<Counterexample<A>> counterexamples;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return dFn.apply(this);
        }

    }

    // Generator encountered a supply failure before a case was falsified
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class SupplyFailed<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;
        SupplyFailure supplyFailure;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return eFn.apply(this);
        }

    }

    // An exception was thrown from a test
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Error<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;
        A errorSample;
        Throwable error;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return fFn.apply(this);
        }

    }

    // It took too long to run all samples
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class TimedOut<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;
        Duration duration;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return gFn.apply(this);
        }

    }

    // An InterruptedException was thrown while running the test
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Interrupted<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;
        Maybe<String> message;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn, Fn1<? super Falsified<A>, ? extends R> cFn, Fn1<? super Unproved<A>, ? extends R> dFn, Fn1<? super SupplyFailed<A>, ? extends R> eFn, Fn1<? super Error<A>, ? extends R> fFn, Fn1<? super TimedOut<A>, ? extends R> gFn, Fn1<? super Interrupted<A>, ? extends R> hFn) {
            return hFn.apply(this);
        }

    }

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

}
