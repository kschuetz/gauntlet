package testsupport.shrink;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.Arbitrary;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.HashSet;

import static com.jnape.palatable.lambda.functions.builtin.fn2.GT.gt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
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
    A min;
    A max;

    public static <A> ShrinkTestCase<A> shrinkTestCase(A input, ImmutableFiniteIterable<A> output) {
        return new ShrinkTestCase<>(input, output, null, null);
    }

    public static <A> ShrinkTestCase<A> shrinkTestCase(A input, ImmutableFiniteIterable<A> output, A min, A max) {
        return new ShrinkTestCase<>(input, output, min, max);
    }

    public static <A> Arbitrary<ShrinkTestCase<A>> shrinkTestCases(Generator<A> generator,
                                                                   Shrink<A> shrink) {
        return arbitrary(generator.fmap(x -> shrinkTestCase(x, shrink.apply(x))));
    }

    // Generates min:max pair using domainGenerator.
    // inputSupplier and shrinkSupplier will use the min:max pair to provided a shrink and generator for the test case's inputs.
    public static <A extends Comparable<A>> Arbitrary<ShrinkTestCase<A>> clampedShrinkTestCases(Generator<Tuple2<A, A>> domainGenerator,
                                                                                                Fn2<A, A, Generator<A>> inputSupplier,
                                                                                                Fn2<A, A, Shrink<A>> shrinkSupplier) {
        return arbitrary(domainGenerator.flatMap(into((min, max) -> {
            Shrink<A> shrink = shrinkSupplier.apply(min, max);
            return inputSupplier.apply(min, max)
                    .fmap(x -> shrinkTestCase(x, shrink.apply(x), min, max));
        })));
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
                    A min = testCase.getMin();
                    A max = testCase.getMax();
                    for (A element : testCase.getOutput()) {
                        if (element.compareTo(min) < 0) {
                            return fail("element '" + element + "' at index " + idx + " is less than minimum (" + min + ")");
                        } else if (element.compareTo(max) > 0) {
                            return fail("element '" + element + "' at index " + idx + " is greater than maximum (" + max + ")");
                        }
                        idx += 1;
                    }
                    return pass();
                });
    }

    public static <A extends Comparable<A>> Prop<ShrinkTestCase<A>> noShrinksWithInputOutsideOfDomain() {
        return prop("all elements within domain",
                testCase -> {
                    int idx = 0;
                    A min = testCase.getMin();
                    A max = testCase.getMax();
                    for (A element : testCase.getOutput()) {
                        if (lt(min, element)) {
                            return fail("element '" + element + "' at index " + idx + " is less than minimum (" + min + ")");
                        } else if (gt(max, element)) {
                            return fail("element '" + element + "' at index " + idx + " is greater than maximum (" + max + ")");
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
                t -> lt(t.getMin(), t.getInput()) || gt(t.getMax(), t.getInput()));
    }

    private static <A> Prop<ShrinkTestCase<A>> shrinkOutputIsEmpty() {
        return Prop.predicate("shrink output is empty", tc -> tc.getOutput().isEmpty());
    }

}
