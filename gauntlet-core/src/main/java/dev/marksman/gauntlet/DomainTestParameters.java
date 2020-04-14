package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import java.time.Duration;
import java.util.Set;

@Wither
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainTestParameters<A> {
    private final Domain<A> domain;
    private final Quantifier quantifier;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Duration timeout;

    public DomainTestParameters<A> addClassifier(Fn1<A, Set<String>> classifier) {
        return new DomainTestParameters<>(domain, quantifier, classifiers.prepend(classifier), timeout);
    }

    public static <A> DomainTestParameters<A> domainTestParameters(Domain<A> domain,
                                                                   Quantifier quantifier,
                                                                   ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                                                   Duration timeout) {
        return new DomainTestParameters<>(domain, quantifier, classifiers, timeout);
    }
}
