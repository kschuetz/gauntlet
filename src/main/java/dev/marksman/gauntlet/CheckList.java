package dev.marksman.gauntlet;

import java.util.BitSet;

final class CheckList {
    private final int size;
    private final BitSet markedIndices;
    private int firstUnmarkedIndex;

    CheckList(int size) {
        this.size = size;
        this.firstUnmarkedIndex = 0;
        this.markedIndices = new BitSet(size);
    }

    void mark(int index) {
        if (index >= 0 && index < size) {
            markedIndices.set(index);
            if (firstUnmarkedIndex == index) {
                int idx = index + 1;
                while (idx < size && markedIndices.get(idx)) {
                    idx++;
                }
                firstUnmarkedIndex = idx;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    boolean isMarked(int index) {
        return markedIndices.get(index);
    }

    int firstUnmarkedIndex() {
        return firstUnmarkedIndex;
    }
}
