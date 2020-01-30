package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class GeneratorOutput<A> {
    @Getter
    private final A value;
    @Getter
    private final int discardCount;

    static <A> GeneratorOutput<A> generatorOutput(A value, int discardCount) {
        return new GeneratorOutput<>(value, discardCount);
    }
}
