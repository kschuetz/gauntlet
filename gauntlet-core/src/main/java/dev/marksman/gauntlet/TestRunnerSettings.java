package dev.marksman.gauntlet;

import java.time.Duration;
import java.util.concurrent.Executor;

final class TestRunnerSettings {
    private final Duration timeout;
    private final Executor executor;

    private TestRunnerSettings(Duration timeout, Executor executor) {
        this.timeout = timeout;
        this.executor = executor;
    }

    static TestRunnerSettings testRunnerSettings(Duration timeout,
                                                 Executor executor) {
        return new TestRunnerSettings(timeout, executor);
    }

    public Duration getTimeout() {
        return timeout;
    }

    public Executor getExecutor() {
        return executor;
    }
}
