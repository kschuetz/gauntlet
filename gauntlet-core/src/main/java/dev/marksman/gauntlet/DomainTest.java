package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class DomainTest<A> {
    @Getter
    private final Quantifier quantifier;
    @Getter
    private final Domain<A> domain;
    @Getter
    private final Prop<A> property;
    @Getter
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    @Getter
    private final Duration timeout;
    @Getter
    private final Executor executor;

    static <A> DomainTest<A> domainTest(Quantifier quantifier,
                                        Domain<A> domain,
                                        Prop<A> property,
                                        ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                        Duration timeout,
                                        Executor executor) {
        return new DomainTest<>(quantifier, domain, property, classifiers, timeout, executor);
    }

}
