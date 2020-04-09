package dev.marksman.gauntlet.prop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static dev.marksman.gauntlet.Context.context;
import static dev.marksman.gauntlet.prop.Props.whenExecuting;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.EvalResultFailureMatcher.isEvalResultFailure;
import static testsupport.matchers.EvalResultSuccessMatcher.isEvalResultSuccess;

class PropsTest {

    @Nested
    @DisplayName("when executing")
    class WhenExecuting {

        @Nested
        @DisplayName("throws exception of class")
        class ThrowsExceptionOfClass {

            @Test
            void successCase() {
                assertThat(whenExecuting(throwingConsumer(ExceptionA::new))
                                .throwsClass(ExceptionA.class)
                                .test(context(), 0),
                        isEvalResultSuccess());
            }

            @Test
            void throwsButClassDoesNotMatch() {
                assertThat(whenExecuting(throwingConsumer(ExceptionB::new))
                                .throwsClass(ExceptionA.class)
                                .test(context(), 0),
                        isEvalResultFailure());
            }


            @Test
            void doesNotThrow() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .throwsClass(ExceptionA.class)
                                .test(context(), 0),
                        isEvalResultFailure());
            }

        }

        @Nested
        @DisplayName("throws exception matching")
        class ThrowsExceptionMatching {

            @Test
            void successCase() {
                assertThat(whenExecuting(throwingConsumer(() -> new RuntimeException("message")))
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .test(context(), 0),
                        isEvalResultSuccess());
            }

            @Test
            void throwsButClassDoesNotMatch() {
                assertThat(whenExecuting(throwingConsumer(() -> new RuntimeException("something else")))
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .test(context(), 0),
                        isEvalResultFailure());
            }


            @Test
            void doesNotThrow() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .test(context(), 0),
                        isEvalResultFailure());
            }

        }

        @Nested
        @DisplayName("does not throw")
        class DoesNotThrow {

            @Test
            void successCase() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .doesNotThrow()
                                .test(context(), 0),
                        isEvalResultSuccess());
            }

            @Test
            void failureCase() {
                assertThat(whenExecuting(throwingConsumer(RuntimeException::new))
                                .doesNotThrow()
                                .test(context(), 0),
                        isEvalResultFailure());
            }

        }

    }

    private static class ExceptionA extends RuntimeException {

    }

    private static class ExceptionB extends RuntimeException {

    }

    private static Consumer<Object> throwingConsumer(Supplier<RuntimeException> exceptionSupplier) {
        return x -> {
            throw exceptionSupplier.get();
        };
    }

    private static Consumer<Object> nonThrowingConsumer() {
        return x -> {
            // do nothing;
        };
    }

}