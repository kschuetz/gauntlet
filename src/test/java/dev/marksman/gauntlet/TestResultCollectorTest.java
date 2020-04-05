package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static dev.marksman.gauntlet.Outcome.passed;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestResultCollectorTest {

    @Test
    void allPassed() {
        int sampleCount = 32;
        ImmutableVector<Integer> samples = Vector.range(sampleCount);
        TestResultCollector<Integer> collector = new TestResultCollector<>(samples);
        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            collector.reportResult(sampleIndex, TestTaskResult.success());
        }

        Outcome<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(passed(samples), result);
    }

    @Test
    void allPassedInDifferentOrder() {
        int sampleCount = 32;
        ImmutableVector<Integer> samples = Vector.range(sampleCount);
        TestResultCollector<Integer> collector = new TestResultCollector<>(samples);
        for (int sampleIndex = sampleCount; sampleIndex >= 0; sampleIndex--) {
            collector.reportResult(sampleIndex, TestTaskResult.success());
        }

        Outcome<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(passed(samples), result);
    }

    @Test
    void timedOut() {
        int sampleCount = 32;
        int samplesToFinishBeforeTimeout = 3;
        ImmutableVector<Integer> samples = Vector.range(sampleCount);
        TestResultCollector<Integer> collector = new TestResultCollector<>(samples);
        for (int sampleIndex = 0; sampleIndex < samplesToFinishBeforeTimeout; sampleIndex++) {
            collector.reportResult(sampleIndex, TestTaskResult.success());
        }

        Outcome<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(Outcome.timedOut(samples.take(samplesToFinishBeforeTimeout), Duration.ZERO), result);
    }

    @Test
    void singleFailure() {

    }

}