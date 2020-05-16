package dev.marksman.gauntlet;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.DomainTest.domainTest;
import static dev.marksman.gauntlet.DomainTestSettingsAdjustments.domainTestSettingsAdjustments;
import static dev.marksman.gauntlet.SettingAdjustment.absolute;

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

    public DomainTestApi<A> withTimeout(Duration timeout) {
        return new DomainTestApi<>(quantifier, domain, settings.adjustTimeout(absolute(timeout)));
    }

    public DomainTestApi<A> withExecutor(Executor executor) {
        return new DomainTestApi<>(quantifier, domain, settings.adjustExecutor(absolute(executor)));
    }

    public DomainTest<A> satisfy(Prop<A> property) {
        return domainTest(quantifier, domain, property, settings);
    }
}
