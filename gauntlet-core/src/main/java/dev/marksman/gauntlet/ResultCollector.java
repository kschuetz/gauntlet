package dev.marksman.gauntlet;

import java.time.Duration;

public interface ResultCollector<A> extends ResultReceiver {
    TestResult<A> getResultBlocking(Duration timeout);
}
