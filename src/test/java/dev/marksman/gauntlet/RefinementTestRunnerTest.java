package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ExecutorService;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.RefinementTest.refinementTest;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkInt;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.jupiter.api.Assertions.assertEquals;


final class RefinementTestRunnerTest {
    private static final int BLOCK_SIZE = 3;

    private static final Prop<Integer> lessThan100 = Prop.predicate("< 100", n -> n < 100);
    private static final Prop<Integer> odd = Prop.predicate("odd", n -> n % 2 == 1);

    private static final Duration timeout = Duration.ofSeconds(5);
    private RefinementTestRunner runner;
    private ExecutorService executorService;
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.systemUTC();
        executorService = newFixedThreadPool(2);
        runner = RefinementTestRunner.refinementTestRunner(clock);
    }

    @Test
    void findsRightAway() {
        RefinementTest<Integer> testParams = refinementTest(shrinkInt(), odd,
                11, 1000, timeout, executorService, BLOCK_SIZE);

        RefinedCounterexample<Integer> result = runner.run(testParams).orElseThrow(AssertionError::new);

        assertEquals(0, result.getCounterexample().getSample());
        assertEquals(1, result.getShrinkCount());
    }

    @Test
    void findsEventually() {
        RefinementTest<Integer> testParams = refinementTest(shrinkInt(), lessThan100,
                105, 1000, timeout, executorService, BLOCK_SIZE);

        RefinedCounterexample<Integer> result = runner.run(testParams).orElseThrow(AssertionError::new);

        assertEquals(100, result.getCounterexample().getSample());
        assertEquals(14, result.getShrinkCount());
    }

    @Test
    void doesNotFind() {
        RefinementTest<Integer> testParams = refinementTest(shrinkInt(), lessThan100, 100, 1000, timeout, executorService, BLOCK_SIZE);

        Maybe<RefinedCounterexample<Integer>> result = runner.run(testParams);

        assertEquals(nothing(), result);
    }

    @Test
    void returnsBestCandidateWhenShrinkCountExceeded() {
        RefinementTest<Integer> testParams = refinementTest(shrinkInt(), lessThan100, 105, 10, timeout, executorService, BLOCK_SIZE);

        RefinedCounterexample<Integer> result = runner.run(testParams).orElseThrow(AssertionError::new);

        assertEquals(101, result.getCounterexample().getSample());
        assertEquals(6, result.getShrinkCount());
    }
}
