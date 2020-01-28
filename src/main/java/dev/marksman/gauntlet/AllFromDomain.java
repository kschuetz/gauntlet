package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import static dev.marksman.gauntlet.Prop.prop;

public final class AllFromDomain<A> {

    public void mustSatisfy(Prop<A> prop) {

    }

    public void mustSatisfy(Fn1<A, Boolean> predicate) {
        mustSatisfy(prop(predicate));
    }

    public void mustSatisfy(Name name, Fn1<A, Boolean> predicate) {
        mustSatisfy(prop(name, predicate));
    }

    public void mustSatisfy(String name, Fn1<A, Boolean> predicate) {
        mustSatisfy(prop(name, predicate));
    }

}
