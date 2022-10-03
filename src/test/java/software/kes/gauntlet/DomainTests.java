package software.kes.gauntlet;

import org.junit.jupiter.api.Test;
import software.kes.collectionviews.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.kes.gauntlet.Domains.integersFrom;

final class DomainTests {
    @Test
    void filterOnce() {
        assertEquals(Vector.of(0, 2, 4, 6, 8),
                integersFrom(0).until(10)
                        .suchThat(n -> n % 2 == 0).getElements());
    }

    @Test
    void filterTwice() {
        assertEquals(Vector.of(0, 2, 4, 6, 8),
                integersFrom(0).until(100)
                        .suchThat(n -> n % 2 == 0)
                        .suchThat(n -> n < 10)
                        .getElements());
    }

    @Test
    void filteringIsStackSafe() {
        Domain<Integer> domain = integersFrom(0).until(10);
        for (int i = 0; i < 50_000; i++) {
            domain = domain.suchThat(n -> n % 2 == 0);
        }
        assertEquals(Vector.of(0, 2, 4, 6, 8),
                domain.getElements());
    }
}
