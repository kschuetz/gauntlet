package software.kes.gauntlet.shrink.builtins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.collectionviews.Vector;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.gauntlet.shrink.ShrinkStrategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShrinkStringTest {
    @Nested
    @DisplayName("structural")
    class Structural {
        @Test
        void evenSize() {
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(ShrinkStrategy.none()).apply("abcdefghij");
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
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(ShrinkStrategy.none()).apply("abcdefghijk");
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
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(3, ShrinkStrategy.none())
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
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(ShrinkStrategy.none())
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
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(0, ShrinkStrategy.none())
                    .apply("aaabbbcccddd");
            assertEquals("abcd", Vector.copyFrom(output).reverse().unsafeGet(0));
        }

        @Test
        void withWhitespace() {
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(0, ShrinkStrategy.none())
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
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(ShrinkCharacter.shrinkCharacter())
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
            ImmutableFiniteIterable<String> output = ShrinkString.shrinkString(3, ShrinkCharacter.shrinkCharacter())
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
