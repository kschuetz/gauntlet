package dev.marksman.gauntlet;

import java.time.Duration;
import java.time.Instant;

final class TestRunnerUtils {
    static Duration getBlockTimeout(Instant deadline, Instant now) {
        return now.isBefore(deadline)
                ? Duration.between(now, deadline)
                : Duration.ZERO;
    }

    static Instant getDeadline(Duration timeout, Instant now) {
        return now.plus(timeout);
    }
}
