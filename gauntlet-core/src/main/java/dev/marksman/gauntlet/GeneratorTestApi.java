package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;
import java.util.Set;

import static dev.marksman.gauntlet.Prop.prop;

public interface GeneratorTestApi<A> {
    GeneratorTestApi<A> withInitialSeed(long initialSeed);

    GeneratorTestApi<A> withSampleCount(int sampleCount);

    GeneratorTestApi<A> withTimeout(Duration timeout);

    GeneratorTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier);

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
