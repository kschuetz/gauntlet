package testsupport.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.Arbitrary;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.Constraint;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.HashSet;

import static dev.marksman.gauntlet.Arbitrary.arbitrary;
import static dev.marksman.gauntlet.Prop.prop;
import static dev.marksman.gauntlet.SimpleResult.fail;
import static dev.marksman.gauntlet.SimpleResult.pass;
import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class ShrinkStrategyTestCase<A> {
    A input;
    ImmutableFiniteIterable<A> output;
    Constraint<A> constraint;

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

}
