package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

// TODO: move this to kraftwerk

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Weighted<A> {
    int weight;
    A value;

    public static <A> Weighted<A> weighted(int weight, A value) {
        if (weight < 0) {
            throw new IllegalArgumentException("weight must be >= 0");
        }
        return new Weighted<>(weight, value);
    }

    public static <A> Weighted<A> weighted(A value) {
        return weighted(1, value);
    }
}
