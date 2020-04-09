package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;

final class ThrowsExceptionMatching<A> implements Prop<A> {
    private final String name;
    private final Prop<A> underlying;
    private final Fn1<? super Throwable, Boolean> exceptionMatcher;

    ThrowsExceptionMatching(String name, Fn1<? super Throwable, Boolean> exceptionMatcher, Prop<A> underlying) {
        this.name = name;
        this.exceptionMatcher = exceptionMatcher;
        this.underlying = underlying;
    }

    @Override
    public Prop<A> safe() {
        return this;
    }

    @Override
    public EvalResult evaluate(A data) {
        try {
            underlying.evaluate(data);
            return EvalFailure.evalFailure(this, failureReasons("Did not throw an exception"));
        } catch (Exception e) {
            if (exceptionMatcher.apply(e)) {
                return evalSuccess();
            } else {
                return EvalFailure.evalFailure(this, failureReasons("Threw an exception that didn't match"));
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    static <A> ThrowsExceptionMatching<A> throwsExceptionMatching(String name,
                                                                  Fn1<? super Throwable, Boolean> exceptionMatcher,
                                                                  Prop<A> underlying) {
        return new ThrowsExceptionMatching<>(name, exceptionMatcher, underlying);
    }
}
