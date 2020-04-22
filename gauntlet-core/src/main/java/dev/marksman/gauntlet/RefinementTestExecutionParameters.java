package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Executor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
public class RefinementTestExecutionParameters {
    public static final int DEFAULT_BLOCK_SIZE = 16;

    private final Executor executor;
    private final int blockSize;

    public static RefinementTestExecutionParameters refinementTestExecutionParameters(Executor executor, int blockSize) {
        return new RefinementTestExecutionParameters(executor, blockSize);
    }

    public static RefinementTestExecutionParameters refinementTestExecutionParameters(Executor executor) {
        return new RefinementTestExecutionParameters(executor, DEFAULT_BLOCK_SIZE);
    }
}
