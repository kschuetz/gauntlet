package software.kes.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.gauntlet.EvalFailure;
import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

import static software.kes.gauntlet.Reasons.reasons;

final class Safe<A> implements Prop<A> {
    private final Prop<A> underlying;

    Safe(Prop<A> underlying) {
        this.underlying = underlying;
    }

    @Override
    public EvalResult evaluate(A data) {
        try {
            return underlying.evaluate(data);
        } catch (Exception e) {
            return EvalFailure.evalFailure(underlying, reasons("Threw an exception: " + e));
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
