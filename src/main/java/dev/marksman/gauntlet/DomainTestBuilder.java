package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import static dev.marksman.gauntlet.Prop.prop;

public interface DomainTestBuilder<A> {

    void mustSatisfy(Prop<A> prop);

    default void mustSatisfy(Fn1<A, Boolean> predicate) {
        mustSatisfy(prop(predicate));
    }

    default void mustSatisfy(Name name, Fn1<A, Boolean> predicate) {
        mustSatisfy(prop(name, predicate));
    }

    default void mustSatisfy(String name, Fn1<A, Boolean> predicate) {
        mustSatisfy(prop(name, predicate));
    }

}
