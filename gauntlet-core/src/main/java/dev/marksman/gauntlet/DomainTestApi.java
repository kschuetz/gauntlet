package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;
import java.util.Set;


public interface DomainTestApi<A> {
    DomainTestApi<A> withTimeout(Duration timeout);

    DomainTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier);

    void mustSatisfy(Prop<A> prop);
}
