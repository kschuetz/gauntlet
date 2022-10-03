package software.kes.gauntlet.shrink.builtins;

import software.kes.gauntlet.shrink.ShrinkResult;
import software.kes.gauntlet.shrink.ShrinkStrategy;

final class ShrinkCharacter {
    private static final ShrinkStrategy<Character> SHRINK_CHARACTER = input -> {
        if (Character.isUpperCase(input)) {
            // Uppercase letter goes to lowercase letter
            return ShrinkResult.of(Character.toLowerCase(input), ' ');
        } else if (Character.isDigit(input) && input != '0') {
            // Digit goes to zero
            return ShrinkResult.of('0', ' ');
        } else if (input > ' ') {
            // Anything greater than SPACE goes to SPACE
            return ShrinkResult.singleton(' ');
        } else if (input != ' ' && Character.isWhitespace(input)) {
            // Any whitespace other than SPACE (e.g, tab, newline) goes to SPACE
            return ShrinkResult.singleton(' ');
        } else {
            return ShrinkResult.empty();
        }
    };

    private ShrinkCharacter() {

    }

    static ShrinkStrategy<Character> shrinkCharacter() {
        return SHRINK_CHARACTER;
    }
}
