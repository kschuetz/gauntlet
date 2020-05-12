package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn0;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static dev.marksman.gauntlet.DomainTest.domainTest;

public final class DomainTestApi<A> {
    private final Fn0<Executor> getParentExecutor;
    private final Consumer<DomainTest<A>> runner;
    private final DomainTestParameters<A> parameters;

    private DomainTestApi(Fn0<Executor> getParentExecutor, Consumer<DomainTest<A>> runner, DomainTestParameters<A> parameters) {
        this.getParentExecutor = getParentExecutor;
        this.runner = runner;
        this.parameters = parameters;
    }

    static <A> DomainTestApi<A> domainTestApi(Fn0<Executor> getParentExecutor,
                                              Consumer<DomainTest<A>> runner,
                                              DomainTestParameters<A> parameters) {
        return new DomainTestApi<>(getParentExecutor, runner, parameters);
    }

    public DomainTestApi<A> withTimeout(Duration timeout) {
        return new DomainTestApi<>(getParentExecutor, runner, parameters.withTimeout(timeout));
    }

    public DomainTestApi<A> withExecutor(Executor executor) {
        return new DomainTestApi<>(getParentExecutor, runner, parameters.withExecutorOverride(just(executor)));
    }

    public void mustSatisfy(Prop<A> property) {
        DomainTest<A> domainTest = domainTest(parameters.getQuantifier(), parameters.getDomain(), property,
                parameters.getTimeout(), parameters.getExecutorOverride().orElseGet(getParentExecutor));
        runner.accept(domainTest);
    }

    @SafeVarargs
    public final void mustSatisfyAllOf(Prop<A> first, Prop<A>... more) {
        mustSatisfy(Prop.allOf(first, more));
    }

    @SafeVarargs
    public final void mustSatisfyAnyOf(Prop<A> first, Prop<A>... more) {
        mustSatisfy(Prop.anyOf(first, more));
    }

    public void mustNotSatisfy(Prop<A> prop) {
        mustSatisfy(prop.not());
    }
}
