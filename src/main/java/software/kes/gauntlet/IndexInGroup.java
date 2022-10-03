package software.kes.gauntlet;

public final class IndexInGroup {
    private final int oneBasedIndex;
    private final int groupSize;

    public static IndexInGroup indexInGroup(int oneBasedIndex, int groupSize) {
        if (groupSize < 1) {
            throw new IllegalArgumentException("groupSize must be >= 1");
        }
        if (oneBasedIndex < 1) {
            throw new IllegalArgumentException("oneBasedIndex must be >= 1");
        }
        if (oneBasedIndex > groupSize) {
            throw new IllegalArgumentException("oneBasedIndex must <= groupSize");
        }
        return new IndexInGroup(oneBasedIndex, groupSize);
    }

    private IndexInGroup(int oneBasedIndex, int groupSize) {
        this.oneBasedIndex = oneBasedIndex;
        this.groupSize = groupSize;
    }

    public int getOneBasedIndex() {
        return oneBasedIndex;
    }

    public int getGroupSize() {
        return groupSize;
    }

    @Override
    public String toString() {
        return "" + oneBasedIndex + " of " + groupSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexInGroup that = (IndexInGroup) o;

        if (oneBasedIndex != that.oneBasedIndex) return false;
        return groupSize == that.groupSize;
    }

    @Override
    public int hashCode() {
        int result = oneBasedIndex;
        result = 31 * result + groupSize;
        return result;
    }
}
