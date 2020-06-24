package dev.marksman.gauntlet.examples;

import dev.marksman.gauntlet.GauntletApiBase;
import dev.marksman.gauntlet.Prop;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.Domains.asciiCharacters;
import static dev.marksman.gauntlet.Prop.allOf;
import static dev.marksman.gauntlet.Prop.named;
import static dev.marksman.gauntlet.Prop.noneOf;

public final class CharacterTests extends GauntletApiBase {
    public static final Prop<Character> isUpperCaseLetter = Prop.predicate("is uppercase letter", Character::isUpperCase);
    public static final Prop<Character> isLowerCaseLetter = Prop.predicate("is lowercase letter", Character::isLowerCase);

    @Test
    void cannotBeBothUppercaseAndLowercase1() {
        checkThat(all(asciiCharacters())
                .satisfy(named("never both uppercase and lowercase",
                        (isUpperCaseLetter.and(isLowerCaseLetter)).not())));
    }

    @Test
    void cannotBeBothUppercaseAndLowercase2() {
        checkThat(all(asciiCharacters())
                .satisfy(named("never both uppercase and lowercase",
                        allOf(isUpperCaseLetter.implies(isLowerCaseLetter.not()),
                                isLowerCaseLetter.implies(isUpperCaseLetter.not())))));
    }

    @Test
    void someUppercaseCharactersExist() {
        checkThat(some(asciiCharacters()).satisfy(isUpperCaseLetter));
    }

    @Test
    void someLowercaseCharactersExist() {
        checkThat(some(asciiCharacters()).satisfy(isLowerCaseLetter));
    }

    @Test
    void someCharactersThatAreNeitherUppercaseOrLowercaseExist() {
        checkThat(some(asciiCharacters()).satisfy(noneOf(isUpperCaseLetter, isLowerCaseLetter)));
    }
}
