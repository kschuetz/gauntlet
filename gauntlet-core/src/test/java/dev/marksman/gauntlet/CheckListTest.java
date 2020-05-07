package dev.marksman.gauntlet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckListTest {
    @Test
    void startsWithNoneMarked() {
        CheckList checkList = new CheckList(100);
        int markedCount = 0;
        for (int i = 0; i < 100; i++) {
            if (checkList.isMarked(i)) {
                markedCount += 1;
            }
        }
        assertEquals(0, markedCount);
    }

    @Test
    void markIndex() {
        CheckList checkList = new CheckList(5);

        checkList.mark(3);

        assertFalse(checkList.isMarked(0));
        assertFalse(checkList.isMarked(1));
        assertFalse(checkList.isMarked(2));
        assertTrue(checkList.isMarked(3));
        assertFalse(checkList.isMarked(4));
    }

    @Test
    void startWithFirstUnmarkedIndexIsZero() {
        CheckList checkList = new CheckList(100);

        assertEquals(0, checkList.firstUnmarkedIndex());
    }

    @Test
    void firstUnmarkedIndexUpdates() {
        CheckList checkList = new CheckList(100);

        checkList.mark(0);
        checkList.mark(1);
        checkList.mark(3);

        assertEquals(2, checkList.firstUnmarkedIndex());
    }


    @Test
    void firstUnmarkedIndexWhenAllAreMarked() {
        CheckList checkList = new CheckList(5);

        checkList.mark(0);
        checkList.mark(1);
        checkList.mark(2);
        checkList.mark(3);
        checkList.mark(4);

        assertEquals(5, checkList.firstUnmarkedIndex());
    }

    @Test
    void markingNegativeIndexIsError() {
        CheckList checkList = new CheckList(5);

        assertThrows(IndexOutOfBoundsException.class, () -> checkList.mark(-1));
    }

    @Test
    void markingIndexBeyondSizeIsError() {
        CheckList checkList = new CheckList(5);

        assertThrows(IndexOutOfBoundsException.class, () -> checkList.mark(6));
    }
}
