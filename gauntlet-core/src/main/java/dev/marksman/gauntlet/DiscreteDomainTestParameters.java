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
    private final DiscreteDomainTestType testType;
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    private final Maybe<Duration> timeout;

    public DiscreteDomainTestParameters<A> addClassifier(Fn1<A, Set<String>> classifier) {
        return new DiscreteDomainTestParameters<>(discreteDomain, testType, classifiers.prepend(classifier), timeout);
    }

    public static <A> DiscreteDomainTestParameters<A> discreteDomainTestParameters(ImmutableFiniteIterable<A> discreteDomain,
                                                                                   DiscreteDomainTestType testType,
                                                                                   ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers,
                                                                                   Maybe<Duration> timeout) {
        return new DiscreteDomainTestParameters<>(discreteDomain, testType, classifiers, timeout);
    }
}
