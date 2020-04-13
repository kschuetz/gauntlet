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
public class Reasons {
    ImmutableNonEmptyVector<String> items;

    public String getPrimary() {
        return items.head();
    }

    public static Reasons reasons(String primaryReason, String... additional) {
        if (additional.length == 0) {
            return new Reasons(Vector.of(primaryReason));
        } else {
            NonEmptyVectorBuilder<String> builder = NonEmptyVector.builder(primaryReason);
            for (String reason : additional) {
                builder = builder.add(reason);
            }
            return new Reasons(builder.build());
        }
    }

    public static Reasons reasons(NonEmptyIterable<String> reasons) {
        return new Reasons(NonEmptyVector.nonEmptyCopyFrom(reasons));
    }

}
