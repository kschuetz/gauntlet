package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
public class ShrinkTestExecutionParameters {
    private final Executor executor;

    public static ShrinkTestExecutionParameters shrinkTestExecutionParameters(Executor executor) {
        return new ShrinkTestExecutionParameters(executor);
    }
}
