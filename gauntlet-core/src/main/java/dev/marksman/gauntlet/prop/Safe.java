package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.FailureReasons.failureReasons;

final class Safe<A> implements Prop<A> {
    private final Prop<A> underlying;

    Safe(Prop<A> underlying) {
        this.underlying = underlying;
    }

    @Override
    public EvalResult test(A data) {
        try {
            return underlying.test(data);
        } catch (Exception e) {
            return EvalFailure.evalFailure(underlying, failureReasons("Threw an exception: " + e));
        }
    }

    @Override
    public String getName() {
        return underlying.getName();
    }

    @Override
    public Prop<A> safe() {
        return this;
    }

    @Override
    public <B> Prop<B> contraMap(Fn1<? super B, ? extends A> fn) {
        return new Safe<>(underlying.contraMap(fn));
    }

}
