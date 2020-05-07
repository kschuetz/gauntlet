package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;

final class DomainTestParameters<A> {
    private final Domain<A> domain;
    private final Quantifier quantifier;
    private final Duration timeout;
    private final Maybe<Executor> executorOverride;

    private DomainTestParameters(Domain<A> domain, Quantifier quantifier, Duration timeout, Maybe<Executor> executorOverride) {
        this.domain = domain;
        this.quantifier = quantifier;
        this.timeout = timeout;
        this.executorOverride = executorOverride;
    }

    public static <A> DomainTestParameters<A> domainTestParameters(Domain<A> domain,
                                                                   Quantifier quantifier,
                                                                   ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                                                   Duration timeout,
                                                                   Maybe<Executor> executorOverride) {
        return new DomainTestParameters<>(domain, quantifier, timeout, executorOverride);
    }

    public Domain<A> getDomain() {
        return this.domain;
    }

    public Quantifier getQuantifier() {
        return this.quantifier;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public Maybe<Executor> getExecutorOverride() {
        return this.executorOverride;
    }

    public DomainTestParameters<A> withTimeout(Duration timeout) {
        return this.timeout == timeout ? this : new DomainTestParameters<>(this.domain, this.quantifier, timeout, this.executorOverride);
    }

    public DomainTestParameters<A> withExecutorOverride(Maybe<Executor> executorOverride) {
        return this.executorOverride == executorOverride ? this : new DomainTestParameters<>(this.domain, this.quantifier, this.timeout, executorOverride);
    }
}
