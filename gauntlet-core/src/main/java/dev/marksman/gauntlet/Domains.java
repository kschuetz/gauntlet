package dev.marksman.gauntlet;

import dev.marksman.collectionviews.Vector;

import static dev.marksman.gauntlet.Domain.domain;

public final class Domains {

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
