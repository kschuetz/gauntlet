package software.kes.gauntlet.examples;

import org.junit.jupiter.api.Test;
import software.kes.gauntlet.Domains;
import software.kes.gauntlet.GauntletApiBase;
import software.kes.gauntlet.Prop;

public final class CharacterTests extends GauntletApiBase {
    public static final Prop<Character> isUpperCaseLetter = Prop.predicate("is uppercase letter", Character::isUpperCase);
    public static final Prop<Character> isLowerCaseLetter = Prop.predicate("is lowercase letter", Character::isLowerCase);

    @Test
    void cannotBeBothUppercaseAndLowercase1() {
        checkThat(all(Domains.asciiCharacters())
                .satisfy(Prop.named("never both uppercase and lowercase",
                        (isUpperCaseLetter.and(isLowerCaseLetter)).not())));
    }

    @Test
    void cannotBeBothUppercaseAndLowercase2() {
        checkThat(all(Domains.asciiCharacters())
                .satisfy(Prop.named("never both uppercase and lowercase",
                        Prop.allOf(isUpperCaseLetter.implies(isLowerCaseLetter.not()),
                                isLowerCaseLetter.implies(isUpperCaseLetter.not())))));
    }

    @Test
    void someUppercaseCharactersExist() {
        checkThat(some(Domains.asciiCharacters()).satisfy(isUpperCaseLetter));
    }

    @Test
    void someLowercaseCharactersExist() {
        checkThat(some(Domains.asciiCharacters()).satisfy(isLowerCaseLetter));
    }

    @Test
    void someCharactersThatAreNeitherUppercaseOrLowercaseExist() {
        checkThat(some(Domains.asciiCharacters()).satisfy(Prop.noneOf(isUpperCaseLetter, isLowerCaseLetter)));
    }
}
