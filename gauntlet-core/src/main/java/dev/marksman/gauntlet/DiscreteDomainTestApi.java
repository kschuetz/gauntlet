package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;
import java.util.Set;


public interface DiscreteDomainTestApi<A> {
    DiscreteDomainTestApi<A> withTimeout(Duration timeout);

    DiscreteDomainTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier);

    void mustSatisfy(Prop<A> prop);
}
