package dev.marksman.gauntlet.hamcrest;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;
import static dev.marksman.gauntlet.Name.name;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Matches<A> implements Prop<A> {
    private static final Name DEFAULT_NAME = name("Matches");

    private final Name name;
    private final Matcher<A> matcher;

    @Override
    public EvalResult test(Context context, A data) {
        if (matcher.matches(data)) {
            return success();
        } else {
            Description description = new StringDescription();
            matcher.describeMismatch(data, description);
            return evalResult(failure(this, description.toString()));
        }
    }

    @Override
    public Name getName() {
        return name;
    }

    public static <A> Prop<A> matches(Name name, Matcher<A> matcher) {
        return new Matches<>(name, matcher);
    }

    public static <A> Prop<A> matches(String name, Matcher<A> matcher) {
        return new Matches<>(name(name), matcher);
    }

    public static <A> Prop<A> matches(Matcher<A> matcher) {
        return new Matches<>(DEFAULT_NAME, matcher);
    }

}
