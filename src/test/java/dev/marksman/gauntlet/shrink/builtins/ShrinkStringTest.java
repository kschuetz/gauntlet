package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.shrink.builtins.ShrinkCharacter.shrinkCharacter;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkString.shrinkString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShrinkStringTest {
    @Nested
    @DisplayName("structural")
    class Structural {
        @Test
        void evenSize() {
            ImmutableFiniteIterable<String> output = shrinkString(ShrinkStrategy.none()).apply("abcdefghij");
            assertThat(output, contains(
                    "",
                    "a",
                    "j",
                    "ab",
                    "ij",
                    "acegi",
                    "bdfhj",
                    "abcde",
                    "fghij",
                    "abcdefghi",
                    "bcdefghij"
            ));
        }

        @Test
        void oddSize() {
            ImmutableFiniteIterable<String> output = shrinkString(ShrinkStrategy.none()).apply("abcdefghijk");
            assertThat(output, contains(
                    "",
                    "a",
                    "k",
                    "ab",
                    "jk",
                    "acegik",
                    "bdfhj",
                    "abcde",
                    "ghijk",
                    "abcdefghij",
                    "bcdefghijk"
            ));
        }

        @Test
        void respectsMinimumLength() {
            ImmutableFiniteIterable<String> output = shrinkString(3, ShrinkStrategy.none())
                    .apply("abcdefghij");
            assertThat(output, contains(
                    "abc",
                    "hij",
                    "acegi",
                    "bdfhj",
                    "abcde",
                    "fghij",
                    "abcdefg",
                    "defghij"
            ));
        }

        @Test
        void willNotIncludeLastIfEqualToFirst() {
            ImmutableFiniteIterable<String> output = shrinkString(ShrinkStrategy.none())
                    .apply("01233210");

            assertThat(output, contains(
                    "",
                    "0",
                    "01",
                    "10",
                    "0231",
                    "1320",
                    "0123",
                    "3210",
                    "0123321",
                    "1233210",
                    "0123210"
            ));
        }

        @Test
        void runsOfCharacters() {
            ImmutableFiniteIterable<String> output = shrinkString(0, ShrinkStrategy.none())
                    .apply("aaabbbcccddd");
            assertEquals("abcd", Vector.copyFrom(output).reverse().unsafeGet(0));
        }

        @Test
        void withWhitespace() {
            ImmutableFiniteIterable<String> output = shrinkString(0, ShrinkStrategy.none())
                    .apply("The   quick    brown    fox");
            assertEquals(Vector.of("Thequickbrownfox", "The quick brown fox"),
                    Vector.copyFrom(output).reverse().take(2));
        }
    }

    @Nested
    @DisplayName("structural")
    class WithElementShrink {
        @Test
        void noMinimumSize() {
            ImmutableFiniteIterable<String> output = shrinkString(shrinkCharacter())
                    .apply("Ab9 !0\t");
            assertThat(output, contains(
                    "",
                    "A",
                    "\t",
                    "A9!\t",
                    "b 0",
                    "Ab9",
                    "!0	",
                    "Ab9 !0",
                    "b9 !0\t",
                    "Ab9!0",
                    "ab9 !0	",
                    " b9 !0	",
                    "A 9 !0	",
                    "Ab0 !0	",
                    "Ab  !0	",
                    "Ab9  0	",
                    "Ab9 ! 	",
                    "Ab9 !0 "
            ));
        }

        @Test
        void respectsMinimumSize() {
            ImmutableFiniteIterable<String> output = shrinkString(3, shrinkCharacter())
                    .apply("Ab9 !0\t");

            assertThat(output, contains(
                    "Ab9",
                    "!0	",
                    "A9!\t",
                    "b 0",
                    "Ab9 ",
                    " !0\t",
                    "Ab9!0",
                    "ab9 !0	",
                    " b9 !0	",
                    "A 9 !0	",
                    "Ab0 !0	",
                    "Ab  !0	",
                    "Ab9  0	",
                    "Ab9 ! 	",
                    "Ab9 !0 "
            ));
        }
    }
}
