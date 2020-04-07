package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct6;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Duration;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
public abstract class TestResult<A> implements CoProduct6<TestResult.Passed<A>,
        TestResult.Falsified<A>, TestResult.SupplyFailed<A>, TestResult.Error<A>, TestResult.TimedOut<A>,
        TestResult.Interrupted<A>, TestResult<A>> {

    public abstract ImmutableVector<A> getPassedSamples();

    public abstract boolean isPassed();

    public abstract TestResult<Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers);

    // All cases succeeded
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Passed<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;

        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return aFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return true;
        }

        @Override
        public Passed<Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers) {
            return passed(passedSamples.fmap(Classified.applyClassifiers(classifiers)));
        }
    }

    // A case was found that falsified the property
    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Falsified<A> extends TestResult<A> {
        ImmutableVector<A> passedSamples;
        A falsifiedSample;
        Failure failure;
        ImmutableVector<A> shrinks;


        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return bFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }

        @Override
        public Falsified<Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers) {
            Fn1<A, Classified<A>> f = Classified.applyClassifiers(classifiers);
            return falsified(passedSamples.fmap(f), f.apply(falsifiedSample), failure, shrinks.fmap(f));
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
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return cFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }

        @Override
        public SupplyFailed<Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers) {
            return supplyFailed(passedSamples.fmap(Classified.applyClassifiers(classifiers)), supplyFailure);
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
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return dFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }

        @Override
        public Error<Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers) {
            Fn1<A, Classified<A>> f = Classified.applyClassifiers(classifiers);
            return error(passedSamples.fmap(f), f.apply(errorSample), error);
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
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return eFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }

        @Override
        public TimedOut<Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers) {
            return timedOut(passedSamples.fmap(Classified.applyClassifiers(classifiers)), duration);
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
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return fFn.apply(this);
        }

        @Override
        public boolean isPassed() {
            return false;
        }

        @Override
        public Interrupted<Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers) {
            return interrupted(passedSamples.fmap(Classified.applyClassifiers(classifiers)), message);
        }
    }

    public static <A> Passed<A> passed(ImmutableVector<A> passedSamples) {
        return new Passed<>(passedSamples);
    }

    public static <A> Falsified<A> falsified(ImmutableVector<A> passedSamples,
                                             A falsifiedSample,
                                             Failure failure,
                                             ImmutableVector<A> shrinks) {
        return new Falsified<>(passedSamples, falsifiedSample, failure, shrinks);
    }

    public static <A> Falsified<A> falsified(ImmutableVector<A> passedSamples,
                                             A falsifiedSample,
                                             Failure failure) {
        return new Falsified<>(passedSamples, falsifiedSample, failure, Vector.empty());
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
