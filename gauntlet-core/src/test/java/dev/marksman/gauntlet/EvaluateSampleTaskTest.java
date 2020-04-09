package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jnape.palatable.lambda.adt.Either.right;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.EvaluateSampleTask.evaluateSampleTask;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class EvaluateSampleTaskTest {

    private static final Prop<Integer> isOdd = Prop.predicate(n -> n % 2 == 1);

    @Mock
    ResultReceiver receiver;

    private static final ArgumentMatcher<Either<Throwable, EvalResult>> IS_FAILURE =
            x -> x.projectB().fmap(EvalResult::isFailure).orElse(false);

    private static final ArgumentMatcher<Either<Throwable, EvalResult>> IS_ERROR =
            x -> x.match(__ -> true, __ -> false);

    @Test
    void successfulCase() {
        when(receiver.shouldRun(0)).thenReturn(true);
        EvaluateSampleTask<Integer> task = evaluateSampleTask(receiver, isOdd, 0, 1);

        task.run();

        verify(receiver, times(1)).reportResult(eq(0), eq(right(evalSuccess())));
        verifyNoMoreInteractions(receiver);
    }

    @Test
    void failureCase() {
        when(receiver.shouldRun(0)).thenReturn(true);
        EvaluateSampleTask<Integer> task = evaluateSampleTask(receiver, isOdd, 0, 2);

        task.run();

        verify(receiver, times(1)).reportResult(eq(0), argThat(IS_FAILURE));
        verifyNoMoreInteractions(receiver);
    }

    @Test
    void errorCase() {
        when(receiver.shouldRun(0)).thenReturn(true);
        EvaluateSampleTask<Integer> task = evaluateSampleTask(receiver, Prop.predicate(n -> {
            throw new Exception("error!");
        }), 0, 2);

        task.run();

        verify(receiver, times(1)).reportResult(eq(0), argThat(IS_ERROR));
        verifyNoMoreInteractions(receiver);
    }

    @Test
    void doesNotRunTestIfNotNeeded() {
        Prop<Integer> prop = (Prop<Integer>) mock(Prop.class);

        when(receiver.shouldRun(0)).thenReturn(false);
        EvaluateSampleTask<Integer> task = evaluateSampleTask(receiver, prop, 0, 2);

        task.run();

        verifyNoInteractions(prop);
        verifyNoMoreInteractions(receiver);
    }
}
