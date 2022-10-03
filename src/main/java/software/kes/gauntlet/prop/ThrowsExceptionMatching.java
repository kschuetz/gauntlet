package software.kes.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

import static software.kes.gauntlet.EvalFailure.evalFailure;
import static software.kes.gauntlet.EvalSuccess.evalSuccess;
import static software.kes.gauntlet.Reasons.reasons;

final class ThrowsExceptionMatching<A> implements Prop<A> {
    private final String name;
    private final Prop<A> underlying;
    private final Fn1<? super Throwable, Boolean> exceptionMatcher;

    ThrowsExceptionMatching(String name, Fn1<? super Throwable, Boolean> exceptionMatcher, Prop<A> underlying) {
        this.name = name;
        this.exceptionMatcher = exceptionMatcher;
        this.underlying = underlying;
    }

    static <A> ThrowsExceptionMatching<A> throwsExceptionMatching(String name,
                                                                  Fn1<? super Throwable, Boolean> exceptionMatcher,
                                                                  Prop<A> underlying) {
        return new ThrowsExceptionMatching<>(name, exceptionMatcher, underlying);
    }

    @Override
    public Prop<A> safe() {
        return this;
    }

    @Override
    public EvalResult evaluate(A data) {
        try {
            underlying.evaluate(data);
            return evalFailure(this, reasons("Did not throw an exception"));
        } catch (Exception e) {
            if (exceptionMatcher.apply(e)) {
                return evalSuccess();
            } else {
                return evalFailure(this, reasons("Threw an exception that didn't match"));
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
