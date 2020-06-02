package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

public final class Counterexample<A> implements Functor<A, Counterexample<?>> {
    private final EvalFailure failure;
    private final A sample;

    private Counterexample(EvalFailure failure, A sample) {
        this.failure = failure;
        this.sample = sample;
    }

    public static <A> Counterexample<A> counterexample(EvalFailure failure, A sample) {
        return new Counterexample<>(failure, sample);
    }

    @Override
    public <B> Counterexample<B> fmap(Fn1<? super A, ? extends B> f) {
        return new Counterexample<>(failure, f.apply(sample));
    }

    public EvalFailure getFailure() {
        return this.failure;
    }

    public A getSample() {
        return this.sample;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Counterexample)) return false;
        final Counterexample<?> other = (Counterexample<?>) o;
        final Object this$failure = this.getFailure();
        final Object other$failure = other.getFailure();
        if (this$failure == null ? other$failure != null : !this$failure.equals(other$failure)) return false;
        final Object this$sample = this.getSample();
        final Object other$sample = other.getSample();
        return this$sample == null ? other$sample == null : this$sample.equals(other$sample);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $failure = this.getFailure();
        result = result * PRIME + ($failure == null ? 43 : $failure.hashCode());
        final Object $sample = this.getSample();
        result = result * PRIME + ($sample == null ? 43 : $sample.hashCode());
        return result;
    }

    public String toString() {
        return "Counterexample(failure=" + this.getFailure() + ", sample=" + this.getSample() + ")";
    }
}
