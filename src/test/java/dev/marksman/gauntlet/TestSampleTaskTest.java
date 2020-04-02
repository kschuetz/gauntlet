package dev.marksman.gauntlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.marksman.gauntlet.Prop.prop;
import static dev.marksman.gauntlet.TestSampleTask.testSampleTask;
import static dev.marksman.gauntlet.TestTaskResult.success;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TestSampleTaskTest {

    private static final Prop<Integer> isOdd = prop(n -> n % 2 == 1);

    @Mock
    Context context;
    @Mock
    TestResultReceiver receiver;

    @Test
    void successfulCase() {
        when(receiver.shouldRun(0)).thenReturn(true);
        TestSampleTask<Integer> task = testSampleTask(context, receiver, isOdd, 0, 1);

        task.run();

        verify(receiver, times(1)).reportResult(eq(0), eq(success()));
        verifyNoMoreInteractions(receiver);
    }

    @Test
    void failureCase() {
        when(receiver.shouldRun(0)).thenReturn(true);
        TestSampleTask<Integer> task = testSampleTask(context, receiver, isOdd, 0, 2);

        task.run();

        verify(receiver, times(1)).reportResult(eq(0), argThat(TestTaskResult::isFailure));
        verifyNoMoreInteractions(receiver);
    }
}