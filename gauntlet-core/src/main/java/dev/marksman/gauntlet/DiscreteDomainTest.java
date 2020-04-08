package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class DiscreteDomainTest<A> {
    @Getter
    private final ImmutableFiniteIterable<A> discreteDomain;
    @Getter
    private final DiscreteDomainTestType testType;
    @Getter
    private final ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers;
    @Getter
    private final Maybe<Duration> timeout;
    @Getter
    private final Prop<A> property;

    public static <A> DiscreteDomainTest<A> discreteDomainTest(DiscreteDomainTestParameters<A> parameters,
                                                               Prop<A> property) {
        return new DiscreteDomainTest<>(parameters.getDiscreteDomain(), parameters.getTestType(),
                parameters.getClassifiers(), parameters.getTimeout(), property);
    }

}
