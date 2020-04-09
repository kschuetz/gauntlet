package dev.marksman.gauntlet.hamcrest;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Failure;
import dev.marksman.gauntlet.Prop;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Matches<A> implements Prop<A> {
    private static final String DEFAULT_NAME = "Matches";

    private final String name;
    private final Matcher<A> matcher;

    @Override
    public EvalResult test(Context context, A data) {
        if (matcher.matches(data)) {
            return EvalResult.success();
        } else {
            Description description = new StringDescription();
            matcher.describeMismatch(data, description);
            return EvalResult.evalResult(Failure.failure(this, description.toString()));
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
