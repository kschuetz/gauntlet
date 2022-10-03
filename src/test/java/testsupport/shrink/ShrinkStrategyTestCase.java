package testsupport.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.gauntlet.Arbitrary;
import software.kes.gauntlet.Prop;
import software.kes.gauntlet.shrink.ShrinkStrategy;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.constraints.Constraint;

import java.util.HashSet;

import static software.kes.gauntlet.Arbitrary.arbitrary;
import static software.kes.gauntlet.Prop.prop;
import static software.kes.gauntlet.SimpleResult.fail;
import static software.kes.gauntlet.SimpleResult.pass;

public final class ShrinkStrategyTestCase<A> {
    private final A input;
    private final ImmutableFiniteIterable<A> output;
    private final Constraint<A> constraint;

    private ShrinkStrategyTestCase(A input, ImmutableFiniteIterable<A> output, Constraint<A> constraint) {
        this.input = input;
        this.output = output;
        this.constraint = constraint;
    }

    public static <A> ShrinkStrategyTestCase<A> shrinkStrategyTestCase(A input, ImmutableFiniteIterable<A> output) {
        return new ShrinkStrategyTestCase<>(input, output, null);
    }

    public static <A> ShrinkStrategyTestCase<A> shrinkStrategyTestCase(A input, ImmutableFiniteIterable<A> output, Constraint<A> constraint) {
        return new ShrinkStrategyTestCase<>(input, output, constraint);
    }

    public static <A> Arbitrary<ShrinkStrategyTestCase<A>> shrinkTestCases(Generator<A> generator,
                                                                           ShrinkStrategy<A> shrinkStrategy) {
        return arbitrary(generator.fmap(x -> shrinkStrategyTestCase(x, shrinkStrategy.apply(x))));
    }

    // Generates min:max pair using constraintGenerator.
    // inputSupplier and shrinkSupplier will use the min:max pair to provided a shrink and generator for the test case's inputs.
    public static <A extends Comparable<A>, C extends Constraint<A>> Arbitrary<ShrinkStrategyTestCase<A>> constrainedShrinkTestCase(Generator<C> constraintGenerator,
                                                                                                                                    Fn1<C, Generator<A>> inputSupplier,
                                                                                                                                    Fn1<C, ShrinkStrategy<A>> shrinkStrategySupplier) {
        return arbitrary(constraintGenerator.flatMap(c -> {
            ShrinkStrategy<A> shrink = shrinkStrategySupplier.apply(c);
            return inputSupplier.apply(c)
                    .fmap(x -> shrinkStrategyTestCase(x, shrink.apply(x), c));
        }));
    }

    public static <A> Prop<ShrinkStrategyTestCase<A>> neverRepeatsAnElement() {
        return prop("never repeats an element",
                testCase -> {
                    HashSet<A> seen = new HashSet<>();
                    seen.add(testCase.getInput());
                    for (A element : testCase.getOutput()) {
                        if (seen.contains(element)) {
                            return fail("repeated element " + element);
                        }
                        seen.add(element);
                    }
                    return pass();
                });
    }

    public static <A extends Comparable<A>> Prop<ShrinkStrategyTestCase<A>> allElementsWithinDomain() {
        return prop("all elements within domain",
                testCase -> {
                    int idx = 0;
                    Constraint<A> constraint = testCase.getConstraint();
                    for (A element : testCase.getOutput()) {
                        if (!constraint.includes(element)) {
                            return fail("element '" + element + "' at index " + idx + " is outside of the domain");
                        }
                        idx += 1;
                    }
                    return pass();
                });
    }

    public static <A extends Comparable<A>> Prop<ShrinkStrategyTestCase<A>> shrinkOutputEmptyWhenInputOutsideOfDomain() {
        return ShrinkStrategyTestCase.<A>inputOutsideOfShrinkDomain()
                .implies(shrinkOutputIsEmpty())
                .rename("when input is outside of shrink domain, shrink output is empty");
    }

    private static <A extends Comparable<A>> Prop<ShrinkStrategyTestCase<A>> inputOutsideOfShrinkDomain() {
        return Prop.predicate("input outside of shrink domain",
                t -> !t.getConstraint().includes(t.getInput()));
    }

    private static <A> Prop<ShrinkStrategyTestCase<A>> shrinkOutputIsEmpty() {
        return Prop.predicate("shrink output is empty", tc -> tc.getOutput().isEmpty());
    }

    public A getInput() {
        return this.input;
    }

    public ImmutableFiniteIterable<A> getOutput() {
        return this.output;
    }

    public Constraint<A> getConstraint() {
        return this.constraint;
    }
}
