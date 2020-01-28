package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.shrink.Shrink;

import static dev.marksman.gauntlet.Prop.prop;

public interface GeneratorTestBuilder<A> {

    GeneratorTestBuilder<A> withShrink(Shrink<A> shrink);

    GeneratorTestBuilder<A> withNoShrink();

    GeneratorTestBuilder<A> withSampleCount(int sampleCount);

    GeneratorTestBuilder<A> withInitialSeed(long initialSeed);

    GeneratorTestBuilder<A> suchThat(Fn1<A, Boolean> predicate);

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
