package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TestResult<A> {
    @Getter
    private final long initialSeedValue;
}
