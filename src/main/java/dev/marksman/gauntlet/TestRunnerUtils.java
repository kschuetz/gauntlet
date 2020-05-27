package dev.marksman.gauntlet;

import java.time.Duration;
import java.time.LocalDateTime;

final class TestRunnerUtils {
    static Duration getBlockTimeout(LocalDateTime deadline, LocalDateTime now) {
        return now.isBefore(deadline)
                ? Duration.between(now, deadline)
                : Duration.ZERO;
    }

    static LocalDateTime getDeadline(Duration timeout, LocalDateTime now) {
        return now.plus(timeout);
    }
}
