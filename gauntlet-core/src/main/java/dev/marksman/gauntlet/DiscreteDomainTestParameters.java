package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
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
public final class DiscreteDomainTestParameters<A> {
    private final ImmutableFiniteIterable<A> discreteDomain;
    private final Quantifier quantifier;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Maybe<Duration> timeout;

    public DiscreteDomainTestParameters<A> addClassifier(Fn1<A, Set<String>> classifier) {
        return new DiscreteDomainTestParameters<>(discreteDomain, quantifier, classifiers.prepend(classifier), timeout);
    }

    public static <A> DiscreteDomainTestParameters<A> discreteDomainTestParameters(ImmutableFiniteIterable<A> discreteDomain,
                                                                                   Quantifier quantifier,
                                                                                   ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                                                                   Maybe<Duration> timeout) {
        return new DiscreteDomainTestParameters<>(discreteDomain, quantifier, classifiers, timeout);
    }
}
