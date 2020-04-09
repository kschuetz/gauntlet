package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static dev.marksman.gauntlet.Counterexample.counterexample;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;
import static dev.marksman.gauntlet.ResultCollector.existentialResultCollector;
import static dev.marksman.gauntlet.ResultCollector.universalResultCollector;
import static dev.marksman.gauntlet.TestResult.*;
import static dev.marksman.gauntlet.TestTaskResult.success;
import static dev.marksman.gauntlet.TestTaskResult.testTaskResult;
import static org.junit.jupiter.api.Assertions.*;

class ResultCollectorTest {

    private static final Prop<Integer> failureProp = Prop.<Integer>alwaysFail().rename("prop");
    private static final EvalFailure failure = EvalFailure.evalFailure(failureProp, failureReasons("failed"));

    private ImmutableVector<Integer> samples;
    private int sampleCount;

    @BeforeEach
    void setUp() {
        sampleCount = 16;
        samples = Vector.range(sampleCount);
    }

    @Nested
    @DisplayName("universal")
    class Universal {

        private ResultCollector<Integer> collector;

        @BeforeEach
        void setUp() {
            collector = universalResultCollector(samples);
        }

        @Test
        void noSamplesYieldsPassed() {
            collector = universalResultCollector(Vector.empty());
            assertEquals(passed(Vector.empty()), collector.getResultBlocking(Duration.ZERO));
        }

        @Test
        void allPassedYieldsPassed() {
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, success());
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(passed(samples), result);
        }

        @Test
        void allPassedInDifferentOrderYieldsPassed() {
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
                collector.reportResult(sampleIndex, sampleIndex == failedIndex ? testTaskResult(failure) : success());
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(falsified(samples.take(failedIndex), counterexample(failure, samples.unsafeGet(failedIndex))),
                    result);
        }

        @Test
        void firstFailureOnly() {
            int firstFailedIndex = 3;
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex >= firstFailedIndex
                        ? testTaskResult(failure)
                        : success());
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(falsified(samples.take(firstFailedIndex), counterexample(failure, samples.unsafeGet(firstFailedIndex))),
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
            collector.reportResult(failedIndex, testTaskResult(failure));

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(falsified(samples.take(failedIndex), counterexample(failure, samples.unsafeGet(failedIndex))),
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
            collector.reportResult(0, testTaskResult(failure));
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldNotRunTestsBeyondEarliestError() {
            collector.reportResult(0, TestTaskResult.error(new Exception("error")));
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldRunTestsBeforeFirstFailure() {
            collector.reportResult(5, testTaskResult(failure));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstSuccess() {
            collector.reportResult(5, TestTaskResult.success());
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstError() {
            collector.reportResult(5, TestTaskResult.error(new Exception("error")));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void successDoesNotAffectCutoff() {
            collector.reportResult(1, TestTaskResult.success());
            assertTrue(collector.shouldRun(2));
        }

    }

    @Nested
    @DisplayName("existential")
    class Existential {

        private ResultCollector<Integer> collector;

        @BeforeEach
        void setUp() {
            collector = existentialResultCollector(samples);
        }

        @Test
        void noSamplesYieldsUnproved() {
            collector = existentialResultCollector(Vector.empty());
            assertEquals(unproved(Vector.empty()), collector.getResultBlocking(Duration.ZERO));
        }

        @Test
        void allPassedYieldsProvedWithFirstSample() {
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, success());
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(proved(samples.unsafeGet(0), Vector.empty()), result);
        }

        @Test
        void allPassedInDifferentOrderYieldsProvedWithFirstSample() {
            for (int sampleIndex = sampleCount; sampleIndex >= 0; sampleIndex--) {
                collector.reportResult(sampleIndex, success());
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(proved(samples.unsafeGet(0), Vector.empty()), result);
        }

        @Test
        void timeoutBeforeCompleting() {
            int samplesToFinishBeforeTimeout = 3;
            for (int sampleIndex = 0; sampleIndex < samplesToFinishBeforeTimeout; sampleIndex++) {
                collector.reportResult(sampleIndex, testTaskResult(failure));
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(timedOut(Vector.empty(), Duration.ZERO),
                    result);
        }

        @Test
        void firstTwoFailOthersPass() {
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex < 2 ? testTaskResult(failure) : success());
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(proved(samples.unsafeGet(2),
                    Vector.of(counterexample(failure, samples.unsafeGet(0)),
                            counterexample(failure, samples.unsafeGet(1)))),
                    result);
        }

        @Test
        void errorEarlierThanPassedSampleYieldsError() {
            Exception exception = new Exception("error");
            for (int sampleIndex = 2; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, success());
            }
            collector.reportResult(0, testTaskResult(failure));
            collector.reportResult(1, TestTaskResult.error(exception));

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(error(Vector.empty(), samples.unsafeGet(1), exception), result);
        }

        @Test
        void passedSampleEarlierThanErrorYieldsProved() {
            Exception exception = new Exception("error");

            collector.reportResult(0, testTaskResult(failure));
            collector.reportResult(1, success());
            collector.reportResult(2, TestTaskResult.error(exception));
            for (int sampleIndex = 3; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, success());
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(proved(samples.unsafeGet(1), Vector.of(counterexample(failure, samples.unsafeGet(0)))), result);
        }

        @Test
        void firstErrorOnly() {
            Exception exception = new Exception("error");
            int firstErroredIndex = 3;
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex >= firstErroredIndex
                        ? TestTaskResult.error(exception)
                        : testTaskResult(failure));
            }

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(error(Vector.empty(), samples.unsafeGet(firstErroredIndex), exception),
                    result);
        }

        @Test
        void doesNotTimeOutIfErrorWasEncountered() {
            Exception exception = new Exception("error");
            collector.reportResult(3, TestTaskResult.error(exception));

            TestResult<Integer> result = collector.getResultBlocking(Duration.ZERO);

            assertEquals(error(Vector.empty(), samples.unsafeGet(3), exception),
                    result);
        }

        @Test
        void shouldNotRunTestsBeyondEarliestSuccess() {
            collector.reportResult(0, TestTaskResult.success());
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldNotRunTestsBeyondEarliestError() {
            collector.reportResult(0, TestTaskResult.error(new Exception("error")));
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldRunTestsBeforeFirstFailure() {
            collector.reportResult(5, testTaskResult(failure));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstSuccess() {
            collector.reportResult(5, TestTaskResult.success());
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstError() {
            collector.reportResult(5, TestTaskResult.error(new Exception("error")));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void failureDoesNotAffectCutoff() {
            collector.reportResult(1, testTaskResult(failure));
            assertTrue(collector.shouldRun(2));
        }
    }
}