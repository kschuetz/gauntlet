package testsupport.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import software.kes.gauntlet.EvalResult;

public final class EvalSuccessMatcher extends TypeSafeMatcher<EvalResult> {
    private EvalSuccessMatcher() {
    }

    @Override
    protected boolean matchesSafely(EvalResult item) {
        return item.match(__ -> true,
                __ -> false);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("EvalSuccess");
    }

    public static EvalSuccessMatcher isEvalSuccess() {
        return new EvalSuccessMatcher();
    }
}
