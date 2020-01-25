package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.Name.name;

public final class Props {

    public static <A> Prop<A> fromPredicate(Name name, Fn1<A, Boolean> predicate) {
        return new PredicateProp<>(name, predicate);
    }

    public static <A> Prop<A> fromPredicate(Fn1<A, Boolean> predicate) {
        return fromPredicate(name(PredicateProp.class.getSimpleName()), predicate);
    }

    public static <A> Prop<A> pass() {
        return Pass.pass();
    }

    public static <A> Prop<A> fail() {
        return Fail.fail();
    }

    public static <A> Prop<A> fail(String failureReason) {
        return Fail.fail(failureReason);
    }
}
