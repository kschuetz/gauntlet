package dev.marksman.gauntlet;

import lombok.Value;

@Value
public class Skip {
    private static Skip INSTANCE = new Skip();

    private Skip() {

    }

    public static Skip skip() {
        return INSTANCE;
    }
}
