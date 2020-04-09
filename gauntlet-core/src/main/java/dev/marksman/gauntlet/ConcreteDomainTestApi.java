package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.DomainTest.domainTest;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class ConcreteDomainTestApi<A> implements DomainTestApi<A> {
    private final Consumer<DomainTest<A>> runner;
    private final DomainTestParameters<A> parameters;

    @Override
    public DomainTestApi<A> withTimeout(Duration timeout) {
        return new ConcreteDomainTestApi<>(runner, parameters.withTimeout(just(timeout)));
    }

    @Override
    public DomainTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new ConcreteDomainTestApi<>(runner, parameters.addClassifier(classifier));
    }

    @Override
    public void mustSatisfy(Prop<A> property) {
        runner.accept(domainTest(parameters, property));
    }

    static <A> ConcreteDomainTestApi<A> concreteGeneratorTestApi(Consumer<DomainTest<A>> runner, DomainTestParameters<A> parameters) {
        return new ConcreteDomainTestApi<>(runner, parameters);
    }

}
