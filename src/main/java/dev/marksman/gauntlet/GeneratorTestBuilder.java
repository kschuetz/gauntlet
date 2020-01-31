package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Set;

import static dev.marksman.gauntlet.Prop.prop;

public interface GeneratorTestBuilder<A> {
    GeneratorTestBuilder<A> withSampleCount(int sampleCount);

    GeneratorTestBuilder<A> withInitialSeed(long initialSeed);

    GeneratorTestBuilder<A> classifyUsing(Fn1<A, Set<String>> classifier);

    Report<A> executeFor(Prop<A> prop);

    default void mustSatisfy(Prop<A> prop) {

    }

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
