package dev.marksman.gauntlet.prop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static dev.marksman.gauntlet.prop.Props.whenExecuting;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.EvalFailureMatcher.isEvalFailure;
import static testsupport.matchers.EvalSuccessMatcher.isEvalSuccess;

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
                                .test(0),
                        isEvalSuccess());
            }

            @Test
            void throwsButClassDoesNotMatch() {
                assertThat(whenExecuting(throwingConsumer(ExceptionB::new))
                                .throwsClass(ExceptionA.class)
                                .test(0),
                        isEvalFailure());
            }


            @Test
            void doesNotThrow() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .throwsClass(ExceptionA.class)
                                .test(0),
                        isEvalFailure());
            }

        }

        @Nested
        @DisplayName("throws exception matching")
        class ThrowsExceptionMatching {

            @Test
            void successCase() {
                assertThat(whenExecuting(throwingConsumer(() -> new RuntimeException("message")))
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .test(0),
                        isEvalSuccess());
            }

            @Test
            void throwsButClassDoesNotMatch() {
                assertThat(whenExecuting(throwingConsumer(() -> new RuntimeException("something else")))
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .test(0),
                        isEvalFailure());
            }


            @Test
            void doesNotThrow() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .test(0),
                        isEvalFailure());
            }

        }

        @Nested
        @DisplayName("does not throw")
        class DoesNotThrow {

            @Test
            void successCase() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .doesNotThrow()
                                .test(0),
                        isEvalSuccess());
            }

            @Test
            void failureCase() {
                assertThat(whenExecuting(throwingConsumer(RuntimeException::new))
                                .doesNotThrow()
                                .test(0),
                        isEvalFailure());
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