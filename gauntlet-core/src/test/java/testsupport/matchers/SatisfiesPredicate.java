package testsupport.matchers;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public class SatisfiesPredicate<A> extends TypeSafeMatcher<A> {
    private final Fn1<? super A, Boolean> predicate;
    private final Maybe<String> label;

    private SatisfiesPredicate(Fn1<? super A, Boolean> predicate, Maybe<String> label) {
        this.predicate = predicate;
        this.label = label;
    }

    @Override
    protected boolean matchesSafely(A item) {
        return predicate.apply(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(label.orElse("[predicate]"));
    }

    public static <A> SatisfiesPredicate<A> satisfiesPredicate(String label, Fn1<? super A, Boolean> predicate) {
        return new SatisfiesPredicate<>(predicate, just(label));
    }

    public static <A> SatisfiesPredicate<A> satisfiesPredicate(Fn1<? super A, Boolean> predicate) {
        return new SatisfiesPredicate<>(predicate, nothing());
    }
}
