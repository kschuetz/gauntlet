package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

class Mapped<A, B> implements Prop<B> {
    private final Fn1<? super B, ? extends A> fn;
    private final Prop<A> underlying;

    Mapped(Fn1<? super B, ? extends A> fn, Prop<A> underlying) {
        this.fn = fn;
        this.underlying = underlying;
    }

    @Override
    public EvalResult evaluate(B data) {
        return underlying.evaluate(fn.apply(data));
    }

    @Override
    public String getName() {
        return underlying.getName();
    }
}
