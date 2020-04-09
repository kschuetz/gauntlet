package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.Prop;

public interface WhenExecuting<A> {

    <T extends Throwable> Prop<A> throwsClass(Class<T> expectedClass);

    Prop<A> throwsExceptionMatching(Fn1<? super Throwable, Boolean> exceptionMatcher);

    Prop<A> doesNotThrow();

}
