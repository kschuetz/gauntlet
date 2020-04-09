package testsupport.matchers;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Failure;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.core.IsAnything.anything;

public final class EvalResultFailureMatcher extends TypeSafeMatcher<EvalResult> {

    private final Matcher<? super Failure> failureMatcher;

    private EvalResultFailureMatcher(Matcher<? super Failure> failureMatcher) {
        this.failureMatcher = failureMatcher;
    }

    @Override
    protected boolean matchesSafely(EvalResult item) {
        return item.match(__ -> false,
                failureMatcher::matches);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("EvalResult failure of ");
        failureMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(EvalResult item, Description mismatchDescription) {
        mismatchDescription.appendText("was ");
        item.match(__ -> io(() -> mismatchDescription.appendText("EvalResult success")),
                f -> io(() -> {
                    mismatchDescription.appendText("EvalResult failure of ");
                    failureMatcher.describeMismatch(f, mismatchDescription);
                }))
                .unsafePerformIO();
    }

    public static EvalResultFailureMatcher isEvalResultFailureThat(Matcher<? super Failure> matcher) {
        return new EvalResultFailureMatcher(matcher);
    }

    public static EvalResultFailureMatcher isEvalResultFailure() {
        return isEvalResultFailureThat(anything());
    }

}
