package testsupport.shrink;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.Arbitrary;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.shrink.Shrink;
import dev.marksman.kraftwerk.Generator;
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

    public static <A> ShrinkTestCase<A> shrinkTestCase(A input, ImmutableFiniteIterable<A> output) {
        return new ShrinkTestCase<>(input, output);
    }

    public static <A> Arbitrary<ShrinkTestCase<A>> shrinkTestCases(Generator<A> generator,
                                                                   Shrink<A> shrink) {
        return arbitrary(generator.fmap(x -> shrinkTestCase(x, shrink.apply(x))));
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
}
