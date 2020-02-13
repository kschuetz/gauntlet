package dev.marksman.gauntlet;

import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeneratorFailure {
    @Getter
    private final ImmutableNonEmptyFiniteIterable<String> path;

    @Getter
    private final int discardedCount;

    public final GeneratorFailure prepend(String label) {
        return new GeneratorFailure(path.prepend(label), discardedCount);
    }

    public static GeneratorFailure generatorFailure(String initialLabel, int discardedCount) {
        return new GeneratorFailure(ImmutableNonEmptyFiniteIterable.of(initialLabel), discardedCount);
    }
}
