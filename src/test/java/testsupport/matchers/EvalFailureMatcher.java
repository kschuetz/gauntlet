package testsupport.matchers;

import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.core.IsAnything.anything;

public final class EvalFailureMatcher extends TypeSafeMatcher<EvalResult> {
    private final Matcher<? super EvalFailure> failureMatcher;

    private EvalFailureMatcher(Matcher<? super EvalFailure> failureMatcher) {
        this.failureMatcher = failureMatcher;
    }

    @Override
    protected boolean matchesSafely(EvalResult item) {
        return item.match(__ -> false,
                failureMatcher::matches);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("EvalFailure of ");
        failureMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(EvalResult item, Description mismatchDescription) {
        mismatchDescription.appendText("was ");
        item.match(__ -> io(() -> mismatchDescription.appendText("EvalSuccess")),
                f -> io(() -> {
                    mismatchDescription.appendText("EvalFailure of ");
                    failureMatcher.describeMismatch(f, mismatchDescription);
                }))
                .unsafePerformIO();
    }

    public static EvalFailureMatcher isEvalFailureThat(Matcher<? super EvalFailure> matcher) {
        return new EvalFailureMatcher(matcher);
    }

    public static EvalFailureMatcher isEvalFailure() {
        return isEvalFailureThat(anything());
    }
}
