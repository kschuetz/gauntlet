package dev.marksman.gauntlet.prop;

import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.SimpleResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static dev.marksman.gauntlet.Prop.alwaysFail;
import static dev.marksman.gauntlet.Prop.alwaysPass;
import static dev.marksman.gauntlet.prop.Facade.biconditional;
import static dev.marksman.gauntlet.prop.Facade.conjunction;
import static dev.marksman.gauntlet.prop.Facade.disjunction;
import static dev.marksman.gauntlet.prop.Facade.exclusiveDisjunction;
import static dev.marksman.gauntlet.prop.Facade.implication;
import static dev.marksman.gauntlet.prop.Facade.negation;
import static dev.marksman.gauntlet.prop.Facade.whenExecuting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static testsupport.matchers.EvalFailureMatcher.isEvalFailure;
import static testsupport.matchers.EvalFailureMatcher.isEvalFailureThat;
import static testsupport.matchers.EvalSuccessMatcher.isEvalSuccess;
import static testsupport.matchers.SatisfiesPredicate.satisfiesPredicate;

final class PropsTest {
    private static final Prop<Object> yes = Prop.predicate("yes", constantly(true));
    private static final Prop<Object> no = Prop.predicate("no", constantly(false));

    @Nested
    @DisplayName("basic prop")
    class BasicProp {
        @Test
        void successCase() {
            assertThat(Facade.prop(constantly(SimpleResult.pass())).evaluate(0), isEvalSuccess());
        }

        @Test
        void failureCase() {
            assertThat(Facade.prop(constantly(SimpleResult.fail("it is not true"))).evaluate(0),
                    isEvalFailureThat(satisfiesPredicate(ef -> ef.getReasons().getPrimary().equals("it is not true"))));
        }

        @Test
        void takesNameThatWasGiven() {
            assertEquals("custom name", Facade.prop("custom name", constantly(SimpleResult.pass())).getName());
        }
    }

    @Nested
    @DisplayName("predicate prop")
    class PredicateProp {
        @Test
        void successCase() {
            assertThat(Facade.predicate(constantly(true)).evaluate(0), isEvalSuccess());
        }

        @Test
        void failureCase() {
            assertThat(Facade.predicate(constantly(false)).evaluate(0), isEvalFailure());
        }
    }

    @Nested
    @DisplayName("conjunction")
    class Conjunction {
        @Nested
        @DisplayName("prefix")
        class Prefix {
            @Test
            void successCases() {
                assertThat(conjunction(yes, yes).evaluate(0), isEvalSuccess());
                assertThat(conjunction(Vector.of(yes)).evaluate(0), isEvalSuccess());
                assertThat(conjunction(Vector.of(yes, yes)).evaluate(0), isEvalSuccess());
                assertThat(conjunction(Vector.of(yes, yes, yes)).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(conjunction(yes, no).evaluate(0), isEvalFailure());
                assertThat(conjunction(no, yes).evaluate(0), isEvalFailure());
                assertThat(conjunction(no, no).evaluate(0), isEvalFailure());
                assertThat(conjunction(Vector.of(no)).evaluate(0), isEvalFailure());
                assertThat(conjunction(Vector.of(yes, yes, no)).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(conjunction(yes.rename("prop1"), no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }

        @Nested
        @DisplayName("infix")
        class Infix {
            @Test
            void successCases() {
                assertThat(yes.and(yes).evaluate(0), isEvalSuccess());
                assertThat(yes.and(yes).and(yes).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(yes.and(no).evaluate(0), isEvalFailure());
                assertThat(no.and(yes).evaluate(0), isEvalFailure());
                assertThat(no.and(no).evaluate(0), isEvalFailure());
                assertThat(yes.and(yes).and(no).evaluate(0), isEvalFailure());
            }

            @Test
            void associative() {
                assertThat(yes.and(yes).and(yes.and(yes)).evaluate(0), isEvalSuccess());
                assertThat(yes.and(yes).and(no.and(yes)).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(yes.rename("prop1").and(no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }
    }

    @Nested
    @DisplayName("disjunction")
    class Disjunction {
        @Nested
        @DisplayName("prefix")
        class Prefix {
            @Test
            void successCases() {
                assertThat(disjunction(yes, yes).evaluate(0), isEvalSuccess());
                assertThat(disjunction(no, yes).evaluate(0), isEvalSuccess());
                assertThat(disjunction(yes, no).evaluate(0), isEvalSuccess());
                assertThat(disjunction(Vector.of(yes)).evaluate(0), isEvalSuccess());
                assertThat(disjunction(Vector.of(no, no, yes)).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(disjunction(no, no).evaluate(0), isEvalFailure());
                assertThat(disjunction(Vector.of(no)).evaluate(0), isEvalFailure());
                assertThat(disjunction(Vector.of(no, no, no)).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(disjunction(yes.rename("prop1"), no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }

        @Nested
        @DisplayName("infix")
        class Infix {
            @Test
            void successCases() {
                assertThat(yes.or(yes).evaluate(0), isEvalSuccess());
                assertThat(no.or(yes).evaluate(0), isEvalSuccess());
                assertThat(yes.or(no).evaluate(0), isEvalSuccess());
                assertThat(yes.or(yes).or(yes).evaluate(0), isEvalSuccess());
                assertThat(no.or(no).or(yes).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(no.or(no).evaluate(0), isEvalFailure());
                assertThat(no.or(no).or(no).evaluate(0), isEvalFailure());
            }

            @Test
            void associative() {
                assertThat(no.or(no).or(no.or(yes)).evaluate(0), isEvalSuccess());
                assertThat(no.or(no).or(no.or(no)).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(yes.rename("prop1").or(no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }
    }

    @Nested
    @DisplayName("negation")
    class Negation {
        @Nested
        @DisplayName("prefix")
        class Prefix {
            @Test
            void successCase() {
                assertThat(negation(no).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCase() {
                assertThat(negation(yes).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(negation(yes.rename("prop1")).getName(),
                        containsString("prop1"));
            }
        }

        @Nested
        @DisplayName("postfix")
        class Postfix {
            @Test
            void successCase() {
                assertThat(no.not().evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCase() {
                assertThat(yes.not().evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(yes.rename("prop1").not().getName(),
                        containsString("prop1"));
            }
        }

        @Test
        void doubleNegationYieldsOriginalProperty() {
            Prop<Object> prop = Prop.predicate(Objects::nonNull);
            assertSame(prop, prop.not().not());
        }
    }

    @Nested
    @DisplayName("implication")
    class Implication {
        @Nested
        @DisplayName("prefix")
        class Prefix {

            @Test
            void successCases() {
                assertThat(implication(yes, yes).evaluate(0), isEvalSuccess());
                assertThat(implication(no, yes).evaluate(0), isEvalSuccess());
                assertThat(implication(no, no).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(implication(yes, no).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(implication(yes.rename("prop1"), no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }

        @Nested
        @DisplayName("infix")
        class Infix {
            @Test
            void successCases() {
                assertThat(yes.implies(yes).evaluate(0), isEvalSuccess());
                assertThat(no.implies(yes).evaluate(0), isEvalSuccess());
                assertThat(no.implies(no).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(yes.implies(no).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(yes.rename("prop1").implies(no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }
    }

    @Nested
    @DisplayName("biconditional")
    class Biconditional {
        @Nested
        @DisplayName("prefix")
        class Prefix {
            @Test
            void successCases() {
                assertThat(biconditional(yes, yes).evaluate(0), isEvalSuccess());
                assertThat(biconditional(no, no).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(biconditional(yes, no).evaluate(0), isEvalFailure());
                assertThat(biconditional(no, yes).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(biconditional(yes.rename("prop1"), no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }

        @Nested
        @DisplayName("infix")
        class Infix {
            @Test
            void successCases() {
                assertThat(yes.iff(yes).evaluate(0), isEvalSuccess());
                assertThat(no.iff(no).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(yes.iff(no).evaluate(0), isEvalFailure());
                assertThat(no.iff(yes).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(yes.rename("prop1").iff(no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }
    }

    @Nested
    @DisplayName("exclusiveDisjunction")
    class ExclusiveDisjunction {
        @Nested
        @DisplayName("prefix")
        class Prefix {
            @Test
            void successCases() {
                assertThat(exclusiveDisjunction(yes, no).evaluate(0), isEvalSuccess());
                assertThat(exclusiveDisjunction(no, yes).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(exclusiveDisjunction(yes, yes).evaluate(0), isEvalFailure());
                assertThat(exclusiveDisjunction(no, no).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(exclusiveDisjunction(yes.rename("prop1"), no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }

        @Nested
        @DisplayName("infix")
        class Infix {
            @Test
            void successCases() {
                assertThat(yes.xor(no).evaluate(0), isEvalSuccess());
                assertThat(no.xor(yes).evaluate(0), isEvalSuccess());
            }

            @Test
            void failureCases() {
                assertThat(yes.xor(yes).evaluate(0), isEvalFailure());
                assertThat(no.xor(no).evaluate(0), isEvalFailure());
            }

            @Test
            void nameContainsComponentNames() {
                assertThat(yes.rename("prop1").xor(no.rename("prop2")).getName(),
                        allOf(containsString("prop1"),
                                containsString("prop2")));
            }
        }
    }

    @Nested
    @DisplayName("anyOf")
    class AnyOf {
        @Test
        void successCases() {
            assertThat(Prop.anyOf(yes).evaluate(0), isEvalSuccess());
            assertThat(Prop.anyOf(no, yes).evaluate(0), isEvalSuccess());
            assertThat(Prop.anyOf(no, no, yes).evaluate(0), isEvalSuccess());
            assertThat(Prop.anyOf(yes, yes, yes).evaluate(0), isEvalSuccess());
        }

        @Test
        void failureCases() {
            assertThat(Prop.anyOf(no).evaluate(0), isEvalFailure());
            assertThat(Prop.anyOf(no, no).evaluate(0), isEvalFailure());
            assertThat(Prop.anyOf(no, no, no).evaluate(0), isEvalFailure());
        }
    }

    @Nested
    @DisplayName("allOf")
    class AllOf {
        @Test
        void successCases() {
            assertThat(Prop.allOf(yes).evaluate(0), isEvalSuccess());
            assertThat(Prop.allOf(yes, yes).evaluate(0), isEvalSuccess());
            assertThat(Prop.allOf(yes, yes, yes).evaluate(0), isEvalSuccess());
        }

        @Test
        void failureCases() {
            assertThat(Prop.allOf(no).evaluate(0), isEvalFailure());
            assertThat(Prop.allOf(no, yes).evaluate(0), isEvalFailure());
            assertThat(Prop.allOf(no, yes, yes).evaluate(0), isEvalFailure());
            assertThat(Prop.allOf(no, no, no).evaluate(0), isEvalFailure());
        }
    }

    @Nested
    @DisplayName("noneOf")
    class NoneOf {
        @Test
        void successCases() {
            assertThat(Prop.noneOf(no).evaluate(0), isEvalSuccess());
            assertThat(Prop.noneOf(no, no).evaluate(0), isEvalSuccess());
            assertThat(Prop.noneOf(no, no, no).evaluate(0), isEvalSuccess());
        }

        @Test
        void failureCases() {
            assertThat(Prop.noneOf(yes).evaluate(0), isEvalFailure());
            assertThat(Prop.noneOf(no, yes).evaluate(0), isEvalFailure());
            assertThat(Prop.noneOf(no, no, yes).evaluate(0), isEvalFailure());
        }
    }

    @Nested
    @DisplayName("notAllOf")
    class NotAllOf {
        @Test
        void successCases() {
            assertThat(Prop.notAllOf(no).evaluate(0), isEvalSuccess());
            assertThat(Prop.notAllOf(no, yes).evaluate(0), isEvalSuccess());
            assertThat(Prop.notAllOf(no, yes, yes).evaluate(0), isEvalSuccess());
        }

        @Test
        void failureCases() {
            assertThat(Prop.notAllOf(yes).evaluate(0), isEvalFailure());
            assertThat(Prop.notAllOf(yes, yes).evaluate(0), isEvalFailure());
            assertThat(Prop.notAllOf(yes, yes, yes).evaluate(0), isEvalFailure());
        }
    }

    @Nested
    @DisplayName("rename")
    class Rename {
        @Nested
        @DisplayName("prefix")
        class Prefix {
            @Test
            void takesOnNameGiven() {
                assertEquals("new name", Prop.named("new name", yes).getName());
                assertEquals("new name", Prop.named("new name", yes.and(yes)).getName());
            }

            @Test
            void replacesPreviousCallsToNamed() {
                assertEquals("new name", Prop.named("new name", Facade.named("temporary", yes)).getName());
            }
        }

        @Nested
        @DisplayName("infix")
        class Infix {
            @Test
            void takesOnNameGiven() {
                assertEquals("new name", yes.rename("new name").getName());
                assertEquals("new name", yes.and(yes).rename("new name").getName());
            }

            @Test
            void replacesPreviousCallsToNamed() {
                assertEquals("new name", yes.rename("temporary").rename("new name").getName());
            }
        }
    }

    @Nested
    @DisplayName("safe")
    class Safe {
        @Test
        void failsIfUnderlyingThrows() {
            Prop<Object> prop = Prop.predicate(x -> {
                throw new RuntimeException();
            })
                    .safe();
            assertThat(prop.evaluate(0), isEvalFailure());
        }

        @Test
        void succeedsIfUnderlyingSucceeds() {
            assertThat(yes.safe().evaluate(0), isEvalSuccess());
        }

        @Test
        void failsIfUnderlyingFails() {
            assertThat(no.safe().evaluate(0), isEvalFailure());
        }

        @Test
        void safeOnAnAlreadySafePropIsANoOp() {
            Prop<Object> prop = Prop.predicate(Objects::nonNull).safe();
            assertSame(prop, prop.safe());
        }
    }

    @Nested
    @DisplayName("alwaysPass")
    class AlwaysPass {
        @Test
        void passes() {
            assertThat(alwaysPass().evaluate(0), isEvalSuccess());
        }
    }

    @Nested
    @DisplayName("alwaysFail")
    class AlwaysFail {
        @Test
        void fails() {
            assertThat(alwaysFail().evaluate(0), isEvalFailure());
        }

        @Test
        void failsWithReasonGiven() {
            assertThat(alwaysFail("because").evaluate(0),
                    isEvalFailureThat(satisfiesPredicate(ef -> ef.getReasons().getPrimary().equals("because"))));
        }
    }

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
                                .evaluate(0),
                        isEvalSuccess());
            }

            @Test
            void throwsButClassDoesNotMatch() {
                assertThat(whenExecuting(throwingConsumer(ExceptionB::new))
                                .throwsClass(ExceptionA.class)
                                .evaluate(0),
                        isEvalFailure());
            }

            @Test
            void doesNotThrow() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .throwsClass(ExceptionA.class)
                                .evaluate(0),
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
                                .evaluate(0),
                        isEvalSuccess());
            }

            @Test
            void throwsButClassDoesNotMatch() {
                assertThat(whenExecuting(throwingConsumer(() -> new RuntimeException("something else")))
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .evaluate(0),
                        isEvalFailure());
            }

            @Test
            void doesNotThrow() {
                assertThat(whenExecuting(nonThrowingConsumer())
                                .throwsExceptionMatching(e -> e.getMessage().equals("message"))
                                .evaluate(0),
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
                                .evaluate(0),
                        isEvalSuccess());
            }

            @Test
            void failureCase() {
                assertThat(whenExecuting(throwingConsumer(RuntimeException::new))
                                .doesNotThrow()
                                .evaluate(0),
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
