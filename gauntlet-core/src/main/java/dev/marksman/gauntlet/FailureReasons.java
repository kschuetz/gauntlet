package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVectorBuilder;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.NonEmptyIterable;
import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class FailureReasons {
    ImmutableNonEmptyVector<String> items;

    public String getPrimary() {
        return items.head();
    }

    public static FailureReasons failureReasons(String primaryReason, String... additional) {
        if (additional.length == 0) {
            return new FailureReasons(Vector.of(primaryReason));
        } else {
            NonEmptyVectorBuilder<String> builder = NonEmptyVector.builder(primaryReason);
            for (String reason : additional) {
                builder = builder.add(reason);
            }
            return new FailureReasons(builder.build());
        }
    }

    public static FailureReasons failureReasons(NonEmptyIterable<String> reasons) {
        return new FailureReasons(NonEmptyVector.nonEmptyCopyFrom(reasons));
    }

}
