package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.ShrinkTest.shrinkTest;
import static dev.marksman.gauntlet.ShrinkTestExecutionParameters.shrinkTestExecutionParameters;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkNumerics.shrinkInt;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultShrinkTestRunnerTest {

    private static final Prop<Integer> lessThan100 = Prop.predicate("< 100", n -> n < 100);
    private static final Prop<Integer> odd = Prop.predicate("odd", n -> n % 2 == 1);

    private static final Duration timeout = Duration.ofSeconds(5);
    private ShrinkTestExecutionParameters executionParameters;
    private DefaultShrinkTestRunner runner;

    @BeforeEach
    void setUp() {
        ExecutorService executorService = newFixedThreadPool(2);
        executionParameters = shrinkTestExecutionParameters(executorService, 3);
        runner = DefaultShrinkTestRunner.defaultShrinkTestRunner();
    }

    @Test
    void findsRightAway() {
        ShrinkTest<Integer> testParams = shrinkTest(shrinkInt(), odd,
                11, 1000, timeout);

        RefinedCounterexample<Integer> result = runner.run(executionParameters, testParams)
                .unsafePerformIO().orElseThrow(AssertionError::new);

        assertEquals(0, result.getCounterexample().getSample());
        assertEquals(1, result.getShrinkCount());
    }

    @Test
    void findsEventually() {
        ShrinkTest<Integer> testParams = shrinkTest(shrinkInt(), lessThan100,
                105, 1000, timeout);

        RefinedCounterexample<Integer> result = runner.run(executionParameters, testParams)
                .unsafePerformIO().orElseThrow(AssertionError::new);

        assertEquals(100, result.getCounterexample().getSample());
        assertEquals(14, result.getShrinkCount());
    }

    @Test
    void doesNotFind() {
        ShrinkTest<Integer> testParams = shrinkTest(shrinkInt(), Prop.predicate("< 100", n -> n < 100),
                100, 1000, timeout);

        Maybe<RefinedCounterexample<Integer>> result = runner.run(executionParameters, testParams)
                .unsafePerformIO();

        assertEquals(nothing(), result);
    }

    @Test
    void returnsBestCandidateWhenShrinkCountExceeded() {
        ShrinkTest<Integer> testParams = shrinkTest(shrinkInt(), Prop.predicate("< 100", n -> n < 100),
                105, 10, timeout);

        RefinedCounterexample<Integer> result = runner.run(executionParameters, testParams)
                .unsafePerformIO().orElseThrow(AssertionError::new);

        assertEquals(101, result.getCounterexample().getSample());
        assertEquals(6, result.getShrinkCount());
    }

}