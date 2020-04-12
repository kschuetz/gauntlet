package testsupport.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.Arbitrary;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.shrink.Shrink;
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
public class ShrinkTestCase<A> {
    A input;
    ImmutableFiniteIterable<A> output;
    Constraint<A> constraint;

    public static <A> ShrinkTestCase<A> shrinkTestCase(A input, ImmutableFiniteIterable<A> output) {
        return new ShrinkTestCase<>(input, output, null);
    }

    public static <A> ShrinkTestCase<A> shrinkTestCase(A input, ImmutableFiniteIterable<A> output, Constraint<A> constraint) {
        return new ShrinkTestCase<>(input, output, constraint);
    }

    public static <A> Arbitrary<ShrinkTestCase<A>> shrinkTestCases(Generator<A> generator,
                                                                   Shrink<A> shrink) {
        return arbitrary(generator.fmap(x -> shrinkTestCase(x, shrink.apply(x))));
    }

    // Generates min:max pair using constraintGenerator.
    // inputSupplier and shrinkSupplier will use the min:max pair to provided a shrink and generator for the test case's inputs.
    public static <A extends Comparable<A>, C extends Constraint<A>> Arbitrary<ShrinkTestCase<A>> constrainedShrinkTestCase(Generator<C> constraintGenerator,
                                                                                                                            Fn1<C, Generator<A>> inputSupplier,
                                                                                                                            Fn1<C, Shrink<A>> shrinkSupplier) {
        return arbitrary(constraintGenerator.flatMap(c -> {
            Shrink<A> shrink = shrinkSupplier.apply(c);
            return inputSupplier.apply(c)
                    .fmap(x -> shrinkTestCase(x, shrink.apply(x), c));
        }));
    }

    public static <A> Prop<ShrinkTestCase<A>> neverRepeatsAnElement() {
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

    public static <A extends Comparable<A>> Prop<ShrinkTestCase<A>> allElementsWithinDomain() {
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

    public static <A extends Comparable<A>> Prop<ShrinkTestCase<A>> shrinkOutputEmptyWhenInputOutsideOfDomain() {
        return ShrinkTestCase.<A>inputOutsideOfShrinkDomain()
                .implies(shrinkOutputIsEmpty())
                .rename("when input is outside of shrink domain, shrink output is empty");
    }

    private static <A extends Comparable<A>> Prop<ShrinkTestCase<A>> inputOutsideOfShrinkDomain() {
        return Prop.predicate("input outside of shrink domain",
                t -> !t.getConstraint().includes(t.getInput()));
    }

    private static <A> Prop<ShrinkTestCase<A>> shrinkOutputIsEmpty() {
        return Prop.predicate("shrink output is empty", tc -> tc.getOutput().isEmpty());
    }

}
