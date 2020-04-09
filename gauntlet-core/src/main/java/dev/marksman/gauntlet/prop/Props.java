package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.gauntlet.FailureReasons;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.SimpleResult;

import java.util.function.Consumer;

import static dev.marksman.gauntlet.FailureReasons.failureReasons;
import static dev.marksman.gauntlet.prop.Executes.executes;

public final class Props {

    public static <A> Prop<A> predicate(Fn1<? super A, Boolean> predicate) {
        return predicate(PredicateProp.class.getSimpleName(), predicate);
    }

    public static <A> Prop<A> predicate(String name, Fn1<? super A, Boolean> predicate) {
        return new PredicateProp<>(name, predicate);
    }

    public static <A> Prop<A> prop(Fn1<? super A, SimpleResult> evaluator) {
        return new BasicProp<>(BasicProp.class.getSimpleName(), evaluator);
    }

    public static <A> Prop<A> prop(String name, Fn1<? super A, SimpleResult> evaluator) {
        return new BasicProp<>(name, evaluator);
    }

    public static <A> Prop<A> alwaysPass() {
        return AlwaysPass.alwaysPass();
    }

    public static <A> Prop<A> alwaysFail() {
        return AlwaysFail.alwaysFail();
    }

    public static <A> Prop<A> alwaysFail(String failureReason) {
        return AlwaysFail.alwaysFail(failureReasons(failureReason));
    }

    public static <A> Prop<A> alwaysFail(FailureReasons failureReasons) {
        return AlwaysFail.alwaysFail(failureReasons);
    }

    public static <A> Prop<A> conjunction(Prop<A> p, Prop<A> q) {
        return new Conjunction<>(Vector.of(p, q));
    }

    public static <A> Prop<A> conjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        return new Conjunction<>(operands);
    }

    public static <A> Prop<A> disjunction(Prop<A> p, Prop<A> q) {
        return new Disjunction<>(Vector.of(p, q));
    }

    public static <A> Prop<A> disjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        return new Disjunction<>(operands);
    }

    public static <A> Prop<A> exclusiveDisjunction(Prop<A> p, Prop<A> q) {
        return new ExclusiveDisjunction<>(p, q);
    }

    public static <A> Prop<A> negation(Prop<A> operand) {
        if (operand instanceof Negation<?>) {
            return ((Negation<A>) operand).operand;
        } else {
            return new Negation<>(operand);
        }
    }

    public static <A> Prop<A> implication(Prop<A> antecedent, Prop<A> consequent) {
        return new Implication<>(antecedent, consequent);
    }

    public static <A> Prop<A> biconditional(Prop<A> antecedent, Prop<A> consequent) {
        return new Biconditional<>(antecedent, consequent);
    }

    public static <A> Prop<A> named(String name, Prop<A> prop) {
        while (prop instanceof Renamed<?>) {
            prop = ((Renamed<A>) prop).underlying;
        }
        return new Renamed<>(name, prop);
    }

    public static <A, B> Prop<B> mapped(Fn1<? super B, ? extends A> fn, Prop<A> prop) {
        return new Mapped<>(fn, prop);
    }

    public static <A> Prop<A> dynamic(Fn1<A, Prop<A>> selector) {
        return new Dynamic<>(selector);
    }

    public static <A> Prop<A> safe(Prop<A> prop) {
        if (prop instanceof Safe<?>) {
            return prop;
        } else {
            return new Safe<>(prop);
        }
    }

    public static <A> WhenExecuting<A> whenExecuting(Consumer<A> executable) {
        return new WhenExecuting<A>() {
            @Override
            public <T extends Throwable> Prop<A> throwsClass(Class<T> expectedClass) {
                return ThrowsExceptionMatching.throwsExceptionMatching("throws exception of class " + expectedClass.getSimpleName(),
                        e -> e.getClass().equals(expectedClass),
                        executes(executable));
            }

            @Override
            public Prop<A> throwsExceptionMatching(Fn1<? super Throwable, Boolean> exceptionMatcher) {
                return ThrowsExceptionMatching.throwsExceptionMatching("throws exception matching",
                        exceptionMatcher,
                        executes(executable));
            }

            @Override
            public Prop<A> doesNotThrow() {
                return executes("does not throw", executable).safe();
            }
        };
    }

}
