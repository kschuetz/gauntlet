package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;

public abstract class ExistentialTestResult<A> implements CoProduct2<ExistentialTestResult.Unproved<A>, ExistentialTestResult.Proved<A>, ExistentialTestResult<A>> {

    public static <A> Unproved<A> unproved(int counterexampleCount) {
        return new Unproved<>(counterexampleCount);
    }

    public static <A> Proved<A> proved(A passedSample, int counterexampleCount) {
        return new Proved<>(passedSample, counterexampleCount);
    }

    public abstract boolean isSuccess();

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ExistentialTestResult)) return false;
        final ExistentialTestResult<?> other = (ExistentialTestResult<?>) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ExistentialTestResult;
    }

    public int hashCode() {
        return 1;
    }

    // No cases were found that prove the property
    public static final class Unproved<A> extends ExistentialTestResult<A> {
        private final int counterexampleCount;

        private Unproved(int counterexampleCount) {
            this.counterexampleCount = counterexampleCount;
        }

        @Override
        public <R> R match(Fn1<? super Unproved<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn) {
            return aFn.apply(this);
        }

        @Override
        public boolean isSuccess() {
            return false;
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

        public Abnormal.Error<A> combine(Abnormal.Error<A> other) {
            return other;
        }

        public Abnormal.TimedOut<A> combine(Abnormal.TimedOut<A> other) {
            return other;
        }

        public Abnormal.Interrupted<A> combine(Abnormal.Interrupted<A> other) {
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

    // A case was found that proved the property
    public static final class Proved<A> extends ExistentialTestResult<A> {
        private final A passedSample;
        private final int counterexampleCount;

        private Proved(A passedSample, int counterexampleCount) {
            this.passedSample = passedSample;
            this.counterexampleCount = counterexampleCount;
        }

        @Override
        public <R> R match(Fn1<? super Unproved<A>, ? extends R> aFn, Fn1<? super Proved<A>, ? extends R> bFn) {
            return bFn.apply(this);
        }

        @Override
        public boolean isSuccess() {
            return true;
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
            return passedSample != null ? passedSample.equals(proved.passedSample) : proved.passedSample == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (passedSample != null ? passedSample.hashCode() : 0);
            result = 31 * result + counterexampleCount;
            return result;
        }
    }
}
