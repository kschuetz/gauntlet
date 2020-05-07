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
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Duration timeout;
    private final Maybe<Executor> executorOverride;

    private DomainTestParameters(Domain<A> domain, Quantifier quantifier, ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers, Duration timeout, Maybe<Executor> executorOverride) {
        this.domain = domain;
        this.quantifier = quantifier;
        this.classifiers = classifiers;
        this.timeout = timeout;
        this.executorOverride = executorOverride;
    }

    public static <A> DomainTestParameters<A> domainTestParameters(Domain<A> domain,
                                                                   Quantifier quantifier,
                                                                   ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                                                   Duration timeout,
                                                                   Maybe<Executor> executorOverride) {
        return new DomainTestParameters<>(domain, quantifier, classifiers, timeout, executorOverride);
    }

    public DomainTestParameters<A> addClassifier(Fn1<A, Set<String>> classifier) {
        return new DomainTestParameters<>(domain, quantifier, classifiers.prepend(classifier), timeout, executorOverride);
    }

    public Domain<A> getDomain() {
        return this.domain;
    }

    public Quantifier getQuantifier() {
        return this.quantifier;
    }

    public ImmutableFiniteIterable<Fn1<A, Set<String>>> getClassifiers() {
        return this.classifiers;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public Maybe<Executor> getExecutorOverride() {
        return this.executorOverride;
    }

    public DomainTestParameters<A> withDomain(Domain<A> domain) {
        return this.domain == domain ? this : new DomainTestParameters<>(domain, this.quantifier, this.classifiers, this.timeout, this.executorOverride);
    }

    public DomainTestParameters<A> withQuantifier(Quantifier quantifier) {
        return this.quantifier == quantifier ? this : new DomainTestParameters<>(this.domain, quantifier, this.classifiers, this.timeout, this.executorOverride);
    }

    public DomainTestParameters<A> withClassifiers(ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers) {
        return this.classifiers == classifiers ? this : new DomainTestParameters<>(this.domain, this.quantifier, classifiers, this.timeout, this.executorOverride);
    }

    public DomainTestParameters<A> withTimeout(Duration timeout) {
        return this.timeout == timeout ? this : new DomainTestParameters<>(this.domain, this.quantifier, this.classifiers, timeout, this.executorOverride);
    }

    public DomainTestParameters<A> withExecutorOverride(Maybe<Executor> executorOverride) {
        return this.executorOverride == executorOverride ? this : new DomainTestParameters<>(this.domain, this.quantifier, this.classifiers, this.timeout, executorOverride);
    }
}
