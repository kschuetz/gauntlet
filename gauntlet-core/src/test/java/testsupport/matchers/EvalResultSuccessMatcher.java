package testsupport.matchers;

import dev.marksman.gauntlet.EvalResult;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class EvalResultSuccessMatcher extends TypeSafeMatcher<EvalResult> {

    private EvalResultSuccessMatcher() {
    }

    @Override
    protected boolean matchesSafely(EvalResult item) {
        return item.match(__ -> true,
                __ -> false);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("EvalResult success");
    }

    public static EvalResultSuccessMatcher isEvalResultSuccess() {
        return new EvalResultSuccessMatcher();
    }

}
