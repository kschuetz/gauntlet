package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public abstract class UniversalTestResult<A> implements CoProduct2<UniversalTestResult.Unfalsified<A>, UniversalTestResult.Falsified<A>, UniversalTestResult<A>>, Functor<A, UniversalTestResult<?>> {

    public static <A> Unfalsified<A> unfalsified(int successCount) {
        return new Unfalsified<>(successCount);
    }

    public static <A> Falsified<A> falsified(Counterexample<A> counterexample, int successCount) {
        return new Falsified<>(counterexample, successCount, nothing());
    }

    public static <A> Falsified<A> falsified(Counterexample<A> counterexample, int successCount,
                                             Maybe<RefinedCounterexample<A>> refinedCounterexample) {
        return new Falsified<>(counterexample, successCount, refinedCounterexample);
    }

    public abstract boolean isSuccess();

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UniversalTestResult)) return false;
        final UniversalTestResult<?> other = (UniversalTestResult<?>) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UniversalTestResult;
    }

    public int hashCode() {
        return 1;
    }

    public static final class Unfalsified<A> extends UniversalTestResult<A> {
        private final int successCount;

        private Unfalsified(int successCount) {
            this.successCount = successCount;
        }

        @Override
        public <B> Unfalsified<B> fmap(Fn1<? super A, ? extends B> f) {
            return new Unfalsified<>(successCount);
        }

        @Override
        public <R> R match(Fn1<? super Unfalsified<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn) {
            return aFn.apply(this);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public Unfalsified<A> combine(Unfalsified<A> other) {
            return new Unfalsified<>(successCount + other.getSuccessCount());
        }

        public Falsified<A> combine(Falsified<A> other) {
            return new Falsified<>(other.getCounterexample(), other.getSuccessCount() + successCount, other.getRefinedCounterexample());
        }

        @Override
        public String toString() {
            return "Unfalsified{" +
                    "successCount=" + successCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Unfalsified<?> unfalsified = (Unfalsified<?>) o;

            return successCount == unfalsified.successCount;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + successCount;
            return result;
        }
    }

    public static final class Falsified<A> extends UniversalTestResult<A> {
        private final Counterexample<A> counterexample;
        private final int successCount;
        private final Maybe<RefinedCounterexample<A>> refinedCounterexample;

        private Falsified(Counterexample<A> counterexample, int successCount, Maybe<RefinedCounterexample<A>> refinedCounterexample) {
            this.successCount = successCount;
            this.counterexample = counterexample;
            this.refinedCounterexample = refinedCounterexample;
        }

        @Override
        public <B> Falsified<B> fmap(Fn1<? super A, ? extends B> f) {
            return new Falsified<>(counterexample.fmap(f), successCount, refinedCounterexample.fmap(rc -> rc.fmap(f)));
        }

        @Override
        public <R> R match(Fn1<? super Unfalsified<A>, ? extends R> aFn, Fn1<? super Falsified<A>, ? extends R> bFn) {
            return bFn.apply(this);
        }

        public Falsified<A> withRefinedCounterexample(RefinedCounterexample<A> refinedCounterexample) {
            return new Falsified<>(counterexample, successCount, just(refinedCounterexample));
        }

        @Override
        public boolean isSuccess() {
            return false;
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
}
