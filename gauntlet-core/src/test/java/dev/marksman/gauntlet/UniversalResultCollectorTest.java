package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static dev.marksman.gauntlet.Failure.failure;
import static dev.marksman.gauntlet.TestResult.*;
import static dev.marksman.gauntlet.TestTaskResult.success;
import static org.junit.jupiter.api.Assertions.*;

class UniversalResultCollectorTest {

    private static final Prop<Integer> failureProp = Prop.<Integer>fail().rename(Name.name("prop"));
    private static final Failure failure = failure(failureProp, "failed");

    private UniversalResultCollector<Integer> collector;
    private ImmutableVector<Integer> samples;
    private int sampleCount;

    @BeforeEach
    void setUp() {
        sampleCount = 16;
        samples = Vector.range(sampleCount);
        collector = new UniversalResultCollector<>(samples);
    }

    @Test
    void allPassed() {
        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            collector.reportResult(sampleIndex, success());
        }

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(passed(samples), result);
    }

    @Test
    void allPassedInDifferentOrder() {
        for (int sampleIndex = sampleCount; sampleIndex >= 0; sampleIndex--) {
            collector.reportResult(sampleIndex, success());
        }

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(passed(samples), result);
    }

    @Test
    void timeoutBeforeCompleting() {
        int samplesToFinishBeforeTimeout = 3;
        for (int sampleIndex = 0; sampleIndex < samplesToFinishBeforeTimeout; sampleIndex++) {
            collector.reportResult(sampleIndex, success());
        }

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(timedOut(samples.take(samplesToFinishBeforeTimeout), Duration.ZERO),
                result);
    }

    @Test
    void singleFailure() {
        int failedIndex = 3;
        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            collector.reportResult(sampleIndex, sampleIndex == failedIndex ? TestTaskResult.failure(failure) : success());
        }

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(falsified(samples.take(failedIndex), samples.unsafeGet(failedIndex), failure),
                result);
    }

    @Test
    void firstFailureOnly() {
        int firstFailedIndex = 3;
        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            collector.reportResult(sampleIndex, sampleIndex >= firstFailedIndex
                    ? TestTaskResult.failure(failure)
                    : success());
        }

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(falsified(samples.take(firstFailedIndex), samples.unsafeGet(firstFailedIndex), failure),
                result);
    }

    @Test
    void singleError() {
        Exception exception = new Exception("error");
        int erroredIndex = 3;
        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            collector.reportResult(sampleIndex, sampleIndex == erroredIndex ? TestTaskResult.error(exception) : success());
        }

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(error(samples.take(erroredIndex), samples.unsafeGet(erroredIndex), exception),
                result);
    }

    @Test
    void firstErrorOnly() {
        Exception exception = new Exception("error");
        int firstErroredIndex = 3;
        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            collector.reportResult(sampleIndex, sampleIndex >= firstErroredIndex
                    ? TestTaskResult.error(exception)
                    : success());
        }

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(error(samples.take(firstErroredIndex), samples.unsafeGet(firstErroredIndex), exception),
                result);
    }

    @Test
    void doesNotTimeOutWhenFalsifiedIfEarlierSamplesPassed() {
        int failedIndex = 3;
        for (int sampleIndex = 0; sampleIndex < failedIndex; sampleIndex++) {
            collector.reportResult(sampleIndex, success());
        }
        collector.reportResult(failedIndex, TestTaskResult.failure(failure));

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(falsified(samples.take(failedIndex), samples.unsafeGet(failedIndex), failure),
                result);
    }

    @Test
    void doesNotTimeOutWhenErroredIfEarlierSamplesPassed() {
        Exception exception = new Exception("error");
        int errorIndex = 3;
        for (int sampleIndex = 0; sampleIndex < errorIndex; sampleIndex++) {
            collector.reportResult(sampleIndex, success());
        }
        collector.reportResult(errorIndex, TestTaskResult.error(exception));

        TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

        assertEquals(error(samples.take(errorIndex), samples.unsafeGet(errorIndex), exception),
                result);
    }

    @Test
    void shouldNotRunTestsBeyondEarliestFailure() {
        collector.reportResult(0, TestTaskResult.failure(failure));
        assertFalse(collector.shouldRun(1));
    }

    @Test
    void shouldNotRunTestsBeyondEarliestError() {
        collector.reportResult(0, TestTaskResult.error(new Exception("error")));
        assertFalse(collector.shouldRun(1));
    }

    @Test
    void shouldRunTestsBeforeFirstFailure() {
        collector.reportResult(5, TestTaskResult.failure(failure));
        assertTrue(collector.shouldRun(0));
        assertTrue(collector.shouldRun(4));
    }
}