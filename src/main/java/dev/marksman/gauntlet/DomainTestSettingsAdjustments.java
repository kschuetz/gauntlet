package dev.marksman.gauntlet;

import java.time.Duration;
import java.util.concurrent.Executor;

import static dev.marksman.gauntlet.SettingAdjustment.inherit;

final class DomainTestSettingsAdjustments {
    private static final DomainTestSettingsAdjustments BASE = new DomainTestSettingsAdjustments(inherit(), inherit());

    private final SettingAdjustment<Duration> timeout;
    private final SettingAdjustment<Executor> executor;

    private DomainTestSettingsAdjustments(SettingAdjustment<Duration> timeout,
                                          SettingAdjustment<Executor> executor) {
        this.timeout = timeout;
        this.executor = executor;
    }

    public static DomainTestSettingsAdjustments domainTestSettingsAdjustments() {
        return BASE;
    }

    public SettingAdjustment<Duration> getTimeout() {
        return timeout;
    }

    public SettingAdjustment<Executor> getExecutor() {
        return executor;
    }

    public DomainTestSettingsAdjustments adjustTimeout(SettingAdjustment<Duration> timeout) {
        return new DomainTestSettingsAdjustments(this.timeout.add(timeout), executor);
    }

    public DomainTestSettingsAdjustments adjustExecutor(SettingAdjustment<Executor> executor) {
        return new DomainTestSettingsAdjustments(timeout, this.executor.add(executor));
    }
}
