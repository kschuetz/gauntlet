package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TestResult<A> {
    @Getter
    private final Maybe<Long> initialSeedValue;

}
