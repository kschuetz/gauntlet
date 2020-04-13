package dev.marksman.gauntlet.hamcrest;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import static dev.marksman.gauntlet.EvalFailure.evalFailure;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.Reasons.reasons;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Matches<A> implements Prop<A> {
    private static final String DEFAULT_NAME = "Matches";

    private final String name;
    private final Matcher<A> matcher;

    @Override
    public EvalResult evaluate(A data) {
        if (matcher.matches(data)) {
            return evalSuccess();
        } else {
            Description description = new StringDescription();
            matcher.describeMismatch(data, description);
            return evalFailure(this, reasons(description.toString()));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public static <A> Prop<A> matches(String name, Matcher<A> matcher) {
        return new Matches<>(name, matcher);
    }

    public static <A> Prop<A> matches(Matcher<A> matcher) {
        return new Matches<>(DEFAULT_NAME, matcher);
    }

}
