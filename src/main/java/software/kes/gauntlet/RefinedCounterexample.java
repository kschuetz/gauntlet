package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

public final class RefinedCounterexample<A> implements Functor<A, RefinedCounterexample<?>> {
    private final Counterexample<A> counterexample;
    private final int shrinkCount;

    private RefinedCounterexample(Counterexample<A> counterexample, int shrinkCount) {
        this.counterexample = counterexample;
        this.shrinkCount = shrinkCount;
    }

    public static <A> RefinedCounterexample<A> refinedCounterexample(Counterexample<A> counterexample,
                                                                     int shrinkCount) {
        return new RefinedCounterexample<>(counterexample, shrinkCount);
    }

    @Override
    public <B> RefinedCounterexample<B> fmap(Fn1<? super A, ? extends B> f) {
        return new RefinedCounterexample<>(counterexample.fmap(f), shrinkCount);
    }

    public Counterexample<A> getCounterexample() {
        return this.counterexample;
    }

    public int getShrinkCount() {
        return this.shrinkCount;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RefinedCounterexample)) return false;
        final RefinedCounterexample<?> other = (RefinedCounterexample<?>) o;
        final Object this$counterexample = this.getCounterexample();
        final Object other$counterexample = other.getCounterexample();
        if (this$counterexample == null ? other$counterexample != null : !this$counterexample.equals(other$counterexample))
            return false;
        return this.getShrinkCount() == other.getShrinkCount();
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $counterexample = this.getCounterexample();
        result = result * PRIME + ($counterexample == null ? 43 : $counterexample.hashCode());
        result = result * PRIME + this.getShrinkCount();
        return result;
    }

    public String toString() {
        return "RefinedCounterexample(counterexample=" + this.getCounterexample() + ", shrinkCount=" + this.getShrinkCount() + ")";
    }
}
