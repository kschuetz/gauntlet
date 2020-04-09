package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;
import java.util.Set;

import static dev.marksman.gauntlet.Prop.predicate;


public interface DiscreteDomainTestApi<A> {
    DiscreteDomainTestApi<A> withTimeout(Duration timeout);

    DiscreteDomainTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier);

    void mustSatisfy(Prop<A> prop);

    default void mustSatisfy(Fn1<? super A, Boolean> predicate) {
        mustSatisfy(Prop.predicate(predicate));
    }

    default void mustSatisfy(String name, Fn1<? super A, Boolean> predicate) {
        mustSatisfy(predicate(name, predicate));
    }
}
