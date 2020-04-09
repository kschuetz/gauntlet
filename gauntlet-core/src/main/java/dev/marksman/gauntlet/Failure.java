package dev.marksman.gauntlet;

import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Value
public class Failure {
    Named propertyName;
    ImmutableNonEmptyFiniteIterable<String> failureReasons;
    ImmutableFiniteIterable<Failure> causes;

    public Failure addCause(Failure failure) {
        return new Failure(propertyName, failureReasons, causes.append(failure));
    }

    public static Failure failure(Named propertyName,
                                  ImmutableNonEmptyFiniteIterable<String> reasons,
                                  ImmutableFiniteIterable<Failure> causes) {
        return new Failure(propertyName, reasons, causes);
    }

    public static Failure failure(Named propertyName, String reason) {
        return new Failure(propertyName, Vector.of(reason), emptyImmutableFiniteIterable());
    }

}
