package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class DomainTest<A> {
    @Getter
    private final Domain<A> domain;
    @Getter
    private final Quantifier quantifier;
    @Getter
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    @Getter
    private final Duration timeout;
    @Getter
    private final Prop<A> property;

    public static <A> DomainTest<A> domainTest(DomainTestParameters<A> parameters,
                                               Prop<A> property) {
        return new DomainTest<>(parameters.getDomain(), parameters.getQuantifier(),
                parameters.getClassifiers(), parameters.getTimeout(), property);
    }

}
