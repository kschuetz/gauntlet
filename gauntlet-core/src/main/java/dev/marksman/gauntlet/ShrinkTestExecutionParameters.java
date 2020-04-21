package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
public class ShrinkTestExecutionParameters {
    public static final int DEFAULT_BLOCK_SIZE = 16;

    private final Executor executor;
    private final int blockSize;

    public static ShrinkTestExecutionParameters shrinkTestExecutionParameters(Executor executor, int blockSize) {
        return new ShrinkTestExecutionParameters(executor, blockSize);
    }

    public static ShrinkTestExecutionParameters shrinkTestExecutionParameters(Executor executor) {
        return new ShrinkTestExecutionParameters(executor, DEFAULT_BLOCK_SIZE);
    }
}
