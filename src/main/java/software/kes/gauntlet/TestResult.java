package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;

public final class TestResult<A> implements CoProduct3<UniversalTestResult<A>, ExistentialTestResult<A>, Abnormal<A>, TestResult<A>> {
    private final Choice3<UniversalTestResult<A>, ExistentialTestResult<A>, Abnormal<A>> underlying;

    private TestResult(Choice3<UniversalTestResult<A>, ExistentialTestResult<A>, Abnormal<A>> underlying) {
        this.underlying = underlying;
    }

    public static <A> TestResult<A> testResult(UniversalTestResult<A> universalTestResult) {
        return new TestResult<>(Choice3.a(universalTestResult));
    }

    public static <A> TestResult<A> testResult(ExistentialTestResult<A> existentialTestResult) {
        return new TestResult<>(Choice3.b(existentialTestResult));
    }

    public static <A> TestResult<A> testResult(Abnormal<A> abnormal) {
        return new TestResult<>(Choice3.c(abnormal));
    }

    public boolean isSuccess() {
        return underlying.match(UniversalTestResult::isSuccess, ExistentialTestResult::isSuccess, __ -> false);
    }

    @Override
    public <R> R match(Fn1<? super UniversalTestResult<A>, ? extends R> aFn, Fn1<? super ExistentialTestResult<A>, ? extends R> bFn, Fn1<? super Abnormal<A>, ? extends R> cFn) {
        return underlying.match(aFn, bFn, cFn);
    }
}
