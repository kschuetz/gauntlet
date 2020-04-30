package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;

public interface GeneratorTestApi<A> {
    GeneratorTestApi<A> withInitialSeed(long initialSeed);

    GeneratorTestApi<A> withSampleCount(int sampleCount);

    GeneratorTestApi<A> withMaximumShrinkCount(int maximumShrinkCount);

    GeneratorTestApi<A> withTimeout(Duration timeout);

    GeneratorTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier);

    GeneratorTestApi<A> withExecutor(Executor executor);

    void mustSatisfy(Prop<A> prop);

    default void mustNotSatisfy(Prop<A> prop) {
        mustSatisfy(prop.not());
    }

}
