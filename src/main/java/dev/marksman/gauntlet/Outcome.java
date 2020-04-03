package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct6;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

import static lombok.AccessLevel.PRIVATE;

public abstract class Outcome<A> implements CoProduct6<Outcome.Passed<A>,
        Outcome.Falsified<A>, Outcome.SupplyFailed<A>, Outcome.Error<A>, Outcome.TimedOut<A>,
        Outcome.Interrupted<A>, Outcome<A>> {

    public abstract ImmutableVector<Classified<A>> getPassedSamples();

    public abstract boolean isPassed();

    // All cases succeeded
    @AllArgsConstructor(access = PRIVATE)
    public static class Passed<A> extends Outcome<A> {
        @Getter
        private final ImmutableVector<Classified<A>> passedSamples;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return aFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return true;
        }
    }

    // A case was found that falsified the property
    @AllArgsConstructor(access = PRIVATE)
    public static class Falsified<A> extends Outcome<A> {
        @Getter
        private final ImmutableVector<Classified<A>> passedSamples;
        @Getter
        private final Classified<A> falsifiedSample;
        @Getter
        private final Failure failure;
        @Getter
        private final ImmutableVector<Classified<A>> shrinks;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return bFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }
    }

    // Generator encountered a supply failure before a case was falsified
    @AllArgsConstructor(access = PRIVATE)
    public static class SupplyFailed<A> extends Outcome<A> {
        @Getter
        private final ImmutableVector<Classified<A>> passedSamples;
        @Getter
        private final SupplyFailure supplyFailure;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return cFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }
    }

    // An exception was thrown from a test
    @AllArgsConstructor(access = PRIVATE)
    public static class Error<A> extends Outcome<A> {
        @Getter
        private final ImmutableVector<Classified<A>> passedSamples;
        @Getter
        private final Classified<A> errorSample;
        @Getter
        private final Throwable error;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return dFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }
    }

    // It took to long to run all samples
    @AllArgsConstructor(access = PRIVATE)
    public static class TimedOut<A> extends Outcome<A> {
        @Getter
        private final ImmutableVector<Classified<A>> passedSamples;
        @Getter
        private final Duration duration;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return eFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }
    }

    // An InterruptedException was thrown while running the test
    @AllArgsConstructor(access = PRIVATE)
    public static class Interrupted<A> extends Outcome<A> {
        @Getter
        private final ImmutableVector<Classified<A>> passedSamples;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return fFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }
    }

    public static <A> Passed<A> passed(ImmutableVector<Classified<A>> passedSamples) {
        return new Passed<>(passedSamples);
    }

    public static <A> Falsified<A> falsified(ImmutableVector<Classified<A>> passedSamples,
                                             Classified<A> falsifiedSample,
                                             Failure failure,
                                             ImmutableVector<Classified<A>> shrinks) {
        return new Falsified<>(passedSamples, falsifiedSample, failure, shrinks);
    }

    public static <A> SupplyFailed<A> supplyFailed(ImmutableVector<Classified<A>> passedSamples, SupplyFailure supplyFailure) {
        return new SupplyFailed<>(passedSamples, supplyFailure);
    }

    public static <A> Error<A> error(ImmutableVector<Classified<A>> passedSamples, Classified<A> errorSample, Throwable error) {
        return new Error<>(passedSamples, errorSample, error);
    }

    public static <A> TimedOut<A> timedOut(ImmutableVector<Classified<A>> passedSamples, Duration duration) {
        return new TimedOut<>(passedSamples, duration);
    }

    public static <A> Interrupted<A> interrupted(ImmutableVector<Classified<A>> passedSamples) {
        return new Interrupted<>(passedSamples);
    }

}
