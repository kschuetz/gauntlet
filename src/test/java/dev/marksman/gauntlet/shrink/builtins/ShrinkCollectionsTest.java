package dev.marksman.gauntlet.shrink.builtins;

import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.marksman.gauntlet.shrink.builtins.ShrinkCollection.shrinkCollection;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

final class ShrinkCollectionsTest {
    @Nested
    @DisplayName("shrinkCollection")
    class Collection {
        @Nested
        @DisplayName("structural")
        class Structural {
            @Test
            void evenSize() {
                ShrinkStrategy<Vector<Integer>> shrink = shrinkCollection(0, ShrinkStrategy.none());
                ImmutableFiniteIterable<Vector<Integer>> output = shrink.apply(Vector.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
                assertThat(output, contains(
                        Vector.empty(),
                        Vector.of(0),
                        Vector.of(9),
                        Vector.of(0, 1),
                        Vector.of(8, 9),
                        Vector.of(0, 2, 4, 6, 8),
                        Vector.of(1, 3, 5, 7, 9),
                        Vector.of(0, 1, 2, 3, 4),
                        Vector.of(5, 6, 7, 8, 9),
                        Vector.of(0, 1, 2, 3, 4, 5, 6, 7, 8),
                        Vector.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                ));
            }

            @Test
            void oddSize() {
                ShrinkStrategy<Vector<Integer>> shrink = shrinkCollection(0, ShrinkStrategy.none());
                ImmutableFiniteIterable<Vector<Integer>> output = shrink.apply(Vector.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
                assertThat(output, contains(
                        Vector.empty(),
                        Vector.of(0),
                        Vector.of(10),
                        Vector.of(0, 1),
                        Vector.of(9, 10),
                        Vector.of(0, 2, 4, 6, 8, 10),
                        Vector.of(1, 3, 5, 7, 9),
                        Vector.of(0, 1, 2, 3, 4),
                        Vector.of(6, 7, 8, 9, 10),
                        Vector.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                        Vector.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                ));
            }

            @Test
            void respectsMinimumSize() {
                ShrinkStrategy<Vector<Integer>> shrink = shrinkCollection(3, ShrinkStrategy.none());
                ImmutableFiniteIterable<Vector<Integer>> output = shrink.apply(Vector.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
                assertThat(output, contains(
                        Vector.of(0, 1, 2),
                        Vector.of(7, 8, 9),
                        Vector.of(0, 2, 4, 6, 8),
                        Vector.of(1, 3, 5, 7, 9),
                        Vector.of(0, 1, 2, 3, 4),
                        Vector.of(5, 6, 7, 8, 9),
                        Vector.of(0, 1, 2, 3, 4, 5, 6),
                        Vector.of(3, 4, 5, 6, 7, 8, 9)
                ));
            }

        }

        @Nested
        @DisplayName("with element shrink")
        class WithElementShrink {
            @Test
            void noMinimumSize() {
                ShrinkStrategy<Vector<Integer>> shrink = shrinkCollection(0, shrinkInt());
                ImmutableFiniteIterable<Vector<Integer>> output = shrink.apply(Vector.of(2, 4, 8, 16, 32));
                assertThat(output, contains(
                        Vector.empty(),
                        Vector.of(2),
                        Vector.of(32),
                        Vector.of(2, 8, 32),
                        Vector.of(4, 16),
                        Vector.of(2, 4),
                        Vector.of(16, 32),
                        Vector.of(2, 4, 8, 16),
                        Vector.of(4, 8, 16, 32),
                        Vector.of(0, 4, 8, 16, 32),
                        Vector.of(1, 4, 8, 16, 32),
                        Vector.of(2, 0, 8, 16, 32),
                        Vector.of(2, 2, 8, 16, 32),
                        Vector.of(2, 3, 8, 16, 32),
                        Vector.of(2, 4, 0, 16, 32),
                        Vector.of(2, 4, 4, 16, 32),
                        Vector.of(2, 4, 6, 16, 32),
                        Vector.of(2, 4, 7, 16, 32),
                        Vector.of(2, 4, 8, 0, 32),
                        Vector.of(2, 4, 8, 8, 32),
                        Vector.of(2, 4, 8, 12, 32),
                        Vector.of(2, 4, 8, 14, 32),
                        Vector.of(2, 4, 8, 15, 32),
                        Vector.of(2, 4, 8, 16, 0),
                        Vector.of(2, 4, 8, 16, 16),
                        Vector.of(2, 4, 8, 16, 24),
                        Vector.of(2, 4, 8, 16, 28),
                        Vector.of(2, 4, 8, 16, 30),
                        Vector.of(2, 4, 8, 16, 31)));
            }

            @Test
            void respectsMinimumSize() {
                ShrinkStrategy<Vector<Integer>> shrink = shrinkCollection(3, shrinkInt());
                ImmutableFiniteIterable<Vector<Integer>> output = shrink.apply(Vector.of(2, 4, 8, 16, 32));
                assertThat(output, contains(
                        Vector.of(2, 4, 8),
                        Vector.of(8, 16, 32),
                        Vector.of(0, 4, 8, 16, 32),
                        Vector.of(1, 4, 8, 16, 32),
                        Vector.of(2, 0, 8, 16, 32),
                        Vector.of(2, 2, 8, 16, 32),
                        Vector.of(2, 3, 8, 16, 32),
                        Vector.of(2, 4, 0, 16, 32),
                        Vector.of(2, 4, 4, 16, 32),
                        Vector.of(2, 4, 6, 16, 32),
                        Vector.of(2, 4, 7, 16, 32),
                        Vector.of(2, 4, 8, 0, 32),
                        Vector.of(2, 4, 8, 8, 32),
                        Vector.of(2, 4, 8, 12, 32),
                        Vector.of(2, 4, 8, 14, 32),
                        Vector.of(2, 4, 8, 15, 32),
                        Vector.of(2, 4, 8, 16, 0),
                        Vector.of(2, 4, 8, 16, 16),
                        Vector.of(2, 4, 8, 16, 24),
                        Vector.of(2, 4, 8, 16, 28),
                        Vector.of(2, 4, 8, 16, 30),
                        Vector.of(2, 4, 8, 16, 31)));
            }
        }
    }
}
