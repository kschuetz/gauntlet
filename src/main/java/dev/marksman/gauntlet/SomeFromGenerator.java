package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.shrink.Shrink;

import static dev.marksman.gauntlet.Prop.prop;

public final class SomeFromGenerator<A> {

    public SomeFromGenerator<A> withShrink(Shrink<A> shrink) {
        return this;
    }

    public SomeFromGenerator<A> withNoShrink() {
        return this;
    }

    public SomeFromGenerator<A> withSampleCount(int sampleCount) {
        return this;
    }

    public SomeFromGenerator<A> withInitialSeed(long initialSeed) {
        return this;
    }

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
