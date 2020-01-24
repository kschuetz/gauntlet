package dev.marksman.gauntlet;

import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Errors {
    private final ImmutableNonEmptyFiniteIterable<Error> items;

    public Errors combine(Errors other) {
        return errors(items.concat(other.items));
    }

    public static Errors errors(ImmutableNonEmptyFiniteIterable<Error> items) {
        return new Errors(items);
    }

    public static Errors errors(Error item) {
        return new Errors(Vector.of(item));
    }
}
