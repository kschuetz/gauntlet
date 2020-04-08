package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.DiscreteDomainTest.discreteDomainTest;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class ConcreteDiscreteDomainTestApi<A> implements DiscreteDomainTestApi<A> {
    private final Consumer<DiscreteDomainTest<A>> runner;
    private final DiscreteDomainTestParameters<A> parameters;

    @Override
    public DiscreteDomainTestApi<A> withTimeout(Duration timeout) {
        return new ConcreteDiscreteDomainTestApi<>(runner, parameters.withTimeout(just(timeout)));
    }

    @Override
    public DiscreteDomainTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new ConcreteDiscreteDomainTestApi<>(runner, parameters.addClassifier(classifier));
    }

    @Override
    public void mustSatisfy(Prop<A> property) {
        runner.accept(discreteDomainTest(parameters, property));
    }

    static <A> ConcreteDiscreteDomainTestApi<A> concreteGeneratorTestApi(Consumer<DiscreteDomainTest<A>> runner, DiscreteDomainTestParameters<A> parameters) {
        return new ConcreteDiscreteDomainTestApi<>(runner, parameters);
    }
}
