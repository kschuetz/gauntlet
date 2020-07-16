package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.gauntlet.shrink.ShrinkStrategy;

final class ShrinkString {
    private ShrinkString() {

    }

    static ShrinkStrategy<String> shrinkString() {
        return shrinkString(0);
    }

    static ShrinkStrategy<String> shrinkString(int minLength) {
        // TODO: shrink string
        return ShrinkStrategy.none();
    }
}
