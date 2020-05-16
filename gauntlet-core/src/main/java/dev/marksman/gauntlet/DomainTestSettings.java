package dev.marksman.gauntlet;

import java.time.Duration;
import java.util.concurrent.Executor;

final class DomainTestSettings {
    private final Duration timeout;
    private final Executor executor;

    private DomainTestSettings(Duration timeout, Executor executor) {
        this.timeout = timeout;
        this.executor = executor;
    }

    static DomainTestSettings domainTestSettings(Duration timeout,
                                                 Executor executor) {
        return new DomainTestSettings(timeout, executor);
    }

    public Duration getTimeout() {
        return timeout;
    }

    public Executor getExecutor() {
        return executor;
    }
}
