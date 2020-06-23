package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.DomainTest.domainTest;
import static dev.marksman.gauntlet.DomainTestSettingsAdjustments.domainTestSettingsAdjustments;
import static dev.marksman.gauntlet.SettingAdjustment.absolute;
import static dev.marksman.gauntlet.SettingAdjustment.inherit;
import static dev.marksman.gauntlet.SettingAdjustment.modify;

public final class DomainTestApi<A> {
    private final Quantifier quantifier;
    private final Domain<A> domain;
    private final DomainTestSettingsAdjustments settings;

    private DomainTestApi(Quantifier quantifier, Domain<A> domain, DomainTestSettingsAdjustments settings) {
        this.quantifier = quantifier;
        this.domain = domain;
        this.settings = settings;
    }

    static <A> DomainTestApi<A> domainTestApi(Quantifier quantifier, Domain<A> domain) {
        return new DomainTestApi<>(quantifier, domain, domainTestSettingsAdjustments());
    }

    public DomainTest<A> satisfy(Prop<A> property) {
        return domainTest(quantifier, domain, property, settings);
    }

    public DomainTestApi<A> withTimeout(Duration timeout) {
        return new DomainTestApi<>(quantifier, domain, settings.adjustTimeout(absolute(timeout)));
    }

    public DomainTestApi<A> withExecutor(Executor executor) {
        return new DomainTestApi<>(quantifier, domain, settings.adjustExecutor(absolute(executor)));
    }

    public DomainTestApi<A> modifyTimeout(Fn1<Duration, Duration> f) {
        return new DomainTestApi<>(quantifier, domain, settings.adjustTimeout(modify(f)));
    }

    public DomainTestApi<A> modifyExecutor(Fn1<Executor, Executor> f) {
        return new DomainTestApi<>(quantifier, domain, settings.adjustExecutor(modify(f)));
    }

    public DomainTestApi<A> withDefaultTimeout() {
        return new DomainTestApi<>(quantifier, domain, settings.adjustTimeout(inherit()));
    }

    public DomainTestApi<A> withDefaultExecutor() {
        return new DomainTestApi<>(quantifier, domain, settings.adjustExecutor(inherit()));
    }
}
