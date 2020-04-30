package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.DomainTest.domainTest;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class ConcreteDomainTestApi<A> implements DomainTestApi<A> {
    private final Fn0<Executor> getParentExecutor;
    private final Consumer<DomainTest<A>> runner;
    private final DomainTestParameters<A> parameters;

    static <A> ConcreteDomainTestApi<A> concreteDomainTestApi(Fn0<Executor> getParentExecutor,
                                                              Consumer<DomainTest<A>> runner,
                                                              DomainTestParameters<A> parameters) {
        return new ConcreteDomainTestApi<>(getParentExecutor, runner, parameters);
    }

    @Override
    public DomainTestApi<A> withTimeout(Duration timeout) {
        return new ConcreteDomainTestApi<>(getParentExecutor, runner, parameters.withTimeout(timeout));
    }

    @Override
    public DomainTestApi<A> classifyUsing(Fn1<A, Set<String>> classifier) {
        return new ConcreteDomainTestApi<>(getParentExecutor, runner, parameters.addClassifier(classifier));
    }

    @Override
    public DomainTestApi<A> withExecutor(Executor executor) {
        return new ConcreteDomainTestApi<>(getParentExecutor, runner, parameters.withExecutorOverride(just(executor)));
    }

    @Override
    public void mustSatisfy(Prop<A> property) {
        DomainTest<A> domainTest = domainTest(parameters.getQuantifier(), parameters.getDomain(), property, parameters.getClassifiers(),
                parameters.getTimeout(), parameters.getExecutorOverride().orElseGet(getParentExecutor));
        runner.accept(domainTest);
    }

}
