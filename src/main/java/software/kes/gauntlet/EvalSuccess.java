package software.kes.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

public final class EvalSuccess extends EvalResult {
    private static final EvalSuccess INSTANCE = new EvalSuccess();

    private EvalSuccess() {
    }

    public static EvalSuccess evalSuccess() {
        return INSTANCE;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public <R> R match(Fn1<? super EvalSuccess, ? extends R> aFn, Fn1<? super EvalFailure, ? extends R> bFn) {
        return aFn.apply(this);
    }

    public String toString() {
        return "EvalSuccess()";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EvalSuccess)) return false;
        final EvalSuccess other = (EvalSuccess) o;
        if (!other.canEqual(this)) return false;
        return super.equals(o);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EvalSuccess;
    }
}
