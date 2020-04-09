package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

final class Recover<A> implements Prop<A> {
    private final Prop<A> underlying;
    private final Fn2<A, ? super Throwable, Maybe<EvalResult>> recoveryFn;

    Recover(Prop<A> underlying, Fn2<A, ? super Throwable, Maybe<EvalResult>> recoveryFn) {
        this.underlying = underlying;
        this.recoveryFn = recoveryFn;
    }

    @Override
    public EvalResult test(A data) {
        try {
            return underlying.test(data);
        } catch (Exception e) {
            EvalResult maybeNewResult = recoveryFn.apply(data, e).orElse(null);
            if (maybeNewResult != null) {
                return maybeNewResult;
            } else {
                throw e;
            }
        }
    }

    @Override
    public String getName() {
        return underlying.getName();
    }

    static <A> Recover<A> recover(Prop<A> underlying, Fn2<A, ? super Throwable, Maybe<EvalResult>> recoveryFn) {
        return new Recover<>(underlying, recoveryFn);
    }

}
