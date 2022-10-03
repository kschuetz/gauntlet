package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.Vector;

import java.time.Duration;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static software.kes.gauntlet.Abnormal.error;
import static software.kes.gauntlet.Abnormal.timedOut;
import static software.kes.gauntlet.Counterexample.counterexample;
import static software.kes.gauntlet.EvalSuccess.evalSuccess;
import static software.kes.gauntlet.ExistentialTestResult.proved;
import static software.kes.gauntlet.ExistentialTestResult.unproved;
import static software.kes.gauntlet.Reasons.reasons;
import static software.kes.gauntlet.UniversalTestResult.falsified;
import static software.kes.gauntlet.UniversalTestResult.unfalsified;

final class ResultCollectorTest {
    private static final Prop<Integer> failureProp = Prop.<Integer>alwaysFail().rename("prop");
    private static final EvalFailure failure = EvalFailure.evalFailure(failureProp, reasons("failed"));

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

        private ResultCollector.UniversalResultCollector<Integer> collector;

        @BeforeEach
        void setUp() {
            collector = ResultCollector.universalResultCollector(samples);
        }

        @Test
        void noSamplesYieldsPassed() {
            collector = ResultCollector.universalResultCollector(Vector.empty());
            Assertions.assertEquals(universal(unfalsified(0)),
                    collector.getResultBlocking(Duration.ZERO));
        }

        @Test
        void allPassedYieldsPassed() {
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(unfalsified(sampleCount)), result);
        }

        @Test
        void allPassedInDifferentOrderYieldsPassed() {
            for (int sampleIndex = sampleCount; sampleIndex >= 0; sampleIndex--) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(unfalsified(sampleCount)), result);
        }

        @Test
        void timeoutBeforeCompleting() {
            int samplesToFinishBeforeTimeout = 3;
            for (int sampleIndex = 0; sampleIndex < samplesToFinishBeforeTimeout; sampleIndex++) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(timedOut(Duration.ZERO, 3)),
                    result);
        }

        @Test
        void singleFailure() {
            int failedIndex = 3;
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex == failedIndex ? right(failure) : right(evalSuccess()));
            }

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(falsified(counterexample(failure, samples.unsafeGet(failedIndex)), failedIndex)),
                    result);
        }

        @Test
        void firstFailureOnly() {
            int firstFailedIndex = 3;
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex >= firstFailedIndex
                        ? right(failure)
                        : right(evalSuccess()));
            }

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(falsified(counterexample(failure, samples.unsafeGet(firstFailedIndex)), firstFailedIndex)),
                    result);
        }

        @Test
        void singleError() {
            Exception exception = new Exception("error");
            int erroredIndex = 3;
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex == erroredIndex ? left(exception) : right(evalSuccess()));
            }

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(error(samples.unsafeGet(erroredIndex), exception, erroredIndex)),
                    result);
        }

        @Test
        void firstErrorOnly() {
            Exception exception = new Exception("error");
            int firstErroredIndex = 3;
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex >= firstErroredIndex
                        ? left(exception)
                        : right(evalSuccess()));
            }

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(error(samples.unsafeGet(firstErroredIndex), exception, firstErroredIndex)),
                    result);
        }

        @Test
        void doesNotTimeOutWhenFalsifiedIfEarlierSamplesPassed() {
            int failedIndex = 3;
            for (int sampleIndex = 0; sampleIndex < failedIndex; sampleIndex++) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }
            collector.reportResult(failedIndex, right(failure));

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(falsified(counterexample(failure, samples.unsafeGet(failedIndex)), failedIndex)),
                    result);
        }

        @Test
        void doesNotTimeOutWhenErroredIfEarlierSamplesPassed() {
            Exception exception = new Exception("error");
            int errorIndex = 3;
            for (int sampleIndex = 0; sampleIndex < errorIndex; sampleIndex++) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }
            collector.reportResult(errorIndex, left(exception));

            Either<Abnormal<Integer>, UniversalTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(universal(error(samples.unsafeGet(errorIndex), exception, errorIndex)),
                    result);
        }

        @Test
        void shouldNotRunTestsBeyondEarliestFailure() {
            collector.reportResult(0, right(failure));
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldNotRunTestsBeyondEarliestError() {
            collector.reportResult(0, left(new Exception("error")));
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldRunTestsBeforeFirstFailure() {
            collector.reportResult(5, right(failure));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstSuccess() {
            collector.reportResult(5, right(evalSuccess()));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstError() {
            collector.reportResult(5, left(new Exception("error")));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void successDoesNotAffectCutoff() {
            collector.reportResult(1, right(evalSuccess()));
            assertTrue(collector.shouldRun(2));
        }

    }

    @Nested
    @DisplayName("existential")
    class Existential {

        private ResultCollector.ExistentialResultCollector<Integer> collector;

        @BeforeEach
        void setUp() {
            collector = ResultCollector.existentialResultCollector(samples);
        }

        @Test
        void noSamplesYieldsUnproved() {
            collector = ResultCollector.existentialResultCollector(Vector.empty());
            Assertions.assertEquals(existential(unproved(0)),
                    collector.getResultBlocking(Duration.ZERO));
        }

        @Test
        void allPassedYieldsProvedWithFirstSample() {
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(proved(samples.unsafeGet(0), 0)),
                    result);
        }

        @Test
        void allPassedInDifferentOrderYieldsProvedWithFirstSample() {
            for (int sampleIndex = sampleCount; sampleIndex >= 0; sampleIndex--) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(proved(samples.unsafeGet(0), 0)),
                    result);
        }

        @Test
        void timeoutBeforeCompleting() {
            int samplesToFinishBeforeTimeout = 3;
            for (int sampleIndex = 0; sampleIndex < samplesToFinishBeforeTimeout; sampleIndex++) {
                collector.reportResult(sampleIndex, right(failure));
            }

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(timedOut(Duration.ZERO, 0)),
                    result);
        }

        @Test
        void firstTwoFailOthersPass() {
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex < 2 ? right(failure) : right(evalSuccess()));
            }

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(proved(samples.unsafeGet(2), 2)),
                    result);
        }

        @Test
        void errorEarlierThanPassedSampleYieldsError() {
            Exception exception = new Exception("error");
            for (int sampleIndex = 2; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }
            collector.reportResult(0, right(failure));
            collector.reportResult(1, left(exception));

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(error(samples.unsafeGet(1), exception, 0)),
                    result);
        }

        @Test
        void passedSampleEarlierThanErrorYieldsProved() {
            Exception exception = new Exception("error");

            collector.reportResult(0, right(failure));
            collector.reportResult(1, right(evalSuccess()));
            collector.reportResult(2, left(exception));
            for (int sampleIndex = 3; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, right(evalSuccess()));
            }

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(proved(samples.unsafeGet(1), 1)),
                    result);
        }

        @Test
        void firstErrorOnly() {
            Exception exception = new Exception("error");
            int firstErroredIndex = 3;
            for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
                collector.reportResult(sampleIndex, sampleIndex >= firstErroredIndex
                        ? left(exception)
                        : right(failure));
            }

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(error(samples.unsafeGet(firstErroredIndex), exception, 0)),
                    result);
        }

        @Test
        void doesNotTimeOutIfErrorWasEncountered() {
            Exception exception = new Exception("error");
            collector.reportResult(3, left(exception));

            Either<Abnormal<Integer>, ExistentialTestResult<Integer>> result = collector.getResultBlocking(Duration.ZERO);

            Assertions.assertEquals(existential(error(samples.unsafeGet(3), exception, 0)),
                    result);
        }

        @Test
        void shouldNotRunTestsBeyondEarliestSuccess() {
            collector.reportResult(0, right(evalSuccess()));
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldNotRunTestsBeyondEarliestError() {
            collector.reportResult(0, left(new Exception("error")));
            assertFalse(collector.shouldRun(1));
        }

        @Test
        void shouldRunTestsBeforeFirstFailure() {
            collector.reportResult(5, right(failure));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstSuccess() {
            collector.reportResult(5, right(evalSuccess()));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void shouldRunTestsBeforeFirstError() {
            collector.reportResult(5, left(new Exception("error")));
            assertTrue(collector.shouldRun(0));
            assertTrue(collector.shouldRun(4));
        }

        @Test
        void failureDoesNotAffectCutoff() {
            collector.reportResult(1, right(failure));
            assertTrue(collector.shouldRun(2));
        }
    }

    private static <A> Either<Abnormal<A>, UniversalTestResult<A>> universal(UniversalTestResult<A> result) {
        return right(result);
    }

    private static <A> Either<Abnormal<A>, ExistentialTestResult<A>> existential(ExistentialTestResult<A> result) {
        return right(result);
    }

    private static <A> Either<Abnormal<A>, UniversalTestResult<A>> universal(Abnormal<A> result) {
        return left(result);
    }

    private static <A> Either<Abnormal<A>, ExistentialTestResult<A>> existential(Abnormal<A> result) {
        return left(result);
    }

}
