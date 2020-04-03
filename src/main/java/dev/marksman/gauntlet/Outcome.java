package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct6;
import com.jnape.palatable.lambda.functions.Fn1;

public abstract class Outcome<A> implements CoProduct6<Outcome.Passed<A>,
        Outcome.Falsified<A>, Outcome.SupplyFailed<A>, Outcome.Error<A>, Outcome.TimedOut<A>,
        Outcome.Interrupted<A>, Outcome<A>> {

    // All cases succeeded
    public static class Passed<A> extends Outcome<A> {
        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return aFn.apply(this);
        }
    }

    // A case was found that falsified the property
    public static class Falsified<A> extends Outcome<A> {
        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return bFn.apply(this);
        }
    }

    // Generator encounted a supply failure before a case was falsified
    public static class SupplyFailed<A> extends Outcome<A> {
        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return cFn.apply(this);
        }
    }

    // An exception was thrown from a test
    public static class Error<A> extends Outcome<A> {
        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return dFn.apply(this);
        }
    }

    // It took to long to run all samples
    public static class TimedOut<A> extends Outcome<A> {
        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return eFn.apply(this);
        }
    }

    // An InterruptedException was thrown while running the test
    public static class Interrupted<A> extends Outcome<A> {
        @Override
        public <R> R match(Fn1<? super Passed<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn, Fn1<? super SupplyFailed<A>, ? extends R> cFn, Fn1<? super Error<A>, ? extends R> dFn, Fn1<? super TimedOut<A>, ? extends R> eFn, Fn1<? super Interrupted<A>, ? extends R> fFn) {
            return fFn.apply(this);
        }
    }
}
