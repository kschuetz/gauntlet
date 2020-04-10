package dev.marksman.gauntlet;

import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;

import static dev.marksman.gauntlet.Domain.domain;

public final class Domains {

    private static final Domain<Character> ASCII = domain(NonEmptyVector.lazyFill(128, idx -> (char) ((int) idx)));

    private static final Domain<Character> ASCII_PRINTABLE = domain(NonEmptyVector.lazyFill(95, idx -> (char) (32 + idx)));

    private Domains() {

    }

    public interface UpperBoundApi<A> {
        Domain<A> to(A upper);

        Domain<A> until(A upper);
    }

    public static UpperBoundApi<Integer> integersFrom(int lower) {
        return new UpperBoundApi<Integer>() {
            @Override
            public Domain<Integer> to(Integer upper) {
                return buildIntegerDomain(lower, upper + 1);
            }

            @Override
            public Domain<Integer> until(Integer upper) {
                return buildIntegerDomain(lower, upper);
            }
        };
    }

    public static Domain<Character> asciiCharacters() {
        return ASCII;
    }

    public static Domain<Character> asciiPrintableCharacters() {
        return ASCII_PRINTABLE;
    }

    private static Domain<Integer> buildIntegerDomain(int lower, int upperExclusive) {
        if (lower >= upperExclusive) {
            throw new IllegalArgumentException("lower must be < upper");
        }
        if (lower == 0) {
            return domain(Vector.range(upperExclusive));
        } else {
            return domain(Vector.range(upperExclusive - lower).fmap(n -> n + lower));
        }
    }

}
