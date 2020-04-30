package dev.marksman.gauntlet;

import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newFixedThreadPool;

public final class DefaultGauntletExecutor {

    private static Executor INSTANCE;

    private DefaultGauntletExecutor() {

    }

    public static Executor defaultGauntletExecutor() {
        if (INSTANCE == null) {
            synchronized (DefaultGauntletExecutor.class) {
                if (INSTANCE == null) {
                    INSTANCE = createExecutor();
                }
            }
        }
        return INSTANCE;
    }

    private static Executor createExecutor() {
        return newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

}
