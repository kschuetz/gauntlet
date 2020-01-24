package dev.marksman.gauntlet;

import lombok.Value;

@Value
public final class Success {
    private static Success INSTANCE = new Success();

    private Success() {

    }

    public static Success success() {
        return INSTANCE;
    }
}
