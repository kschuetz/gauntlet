package dev.marksman.gauntlet.hamcrest;

import dev.marksman.gauntlet.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Matches<A> implements Prop<A> {
    private static final Name DEFAULT_NAME = Name.name("Matches");

    private final Name name;
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
    public Name getName() {
        return name;
    }

    public static <A> Prop<A> matches(Name name, Matcher<A> matcher) {
        return new Matches<>(name, matcher);
    }

    public static <A> Prop<A> matches(String name, Matcher<A> matcher) {
        return new Matches<>(Name.name(name), matcher);
    }

    public static <A> Prop<A> matches(Matcher<A> matcher) {
        return new Matches<>(DEFAULT_NAME, matcher);
    }

}
