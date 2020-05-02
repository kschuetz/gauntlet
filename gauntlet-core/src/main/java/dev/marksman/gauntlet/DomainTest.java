package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;

final class DomainTest<A> {
    private final Quantifier quantifier;
    private final Domain<A> domain;
    private final Prop<A> property;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Duration timeout;
    private final Executor executor;

    private DomainTest(Quantifier quantifier, Domain<A> domain, Prop<A> property, ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                       Duration timeout, Executor executor) {
        this.quantifier = quantifier;
        this.domain = domain;
        this.property = property;
        this.classifiers = classifiers;
        this.timeout = timeout;
        this.executor = executor;
    }

    static <A> DomainTest<A> domainTest(Quantifier quantifier,
                                        Domain<A> domain,
                                        Prop<A> property,
                                        ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                        Duration timeout,
                                        Executor executor) {
        return new DomainTest<>(quantifier, domain, property, classifiers, timeout, executor);
    }

    public Quantifier getQuantifier() {
        return this.quantifier;
    }

    public Domain<A> getDomain() {
        return this.domain;
    }

    public Prop<A> getProperty() {
        return this.property;
    }

    public ImmutableFiniteIterable<Fn1<A, Set<String>>> getClassifiers() {
        return this.classifiers;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public Executor getExecutor() {
        return this.executor;
    }
}
