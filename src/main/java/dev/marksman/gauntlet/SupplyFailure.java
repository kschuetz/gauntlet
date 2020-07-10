package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;

public final class SupplyFailure {
    private final int discardCount;
    private final SupplyTree supplyTree;

    private SupplyFailure(int discardCount, SupplyTree supplyTree) {
        this.discardCount = discardCount;
        this.supplyTree = supplyTree;
    }

    public static SupplyFailure supplyFailure(int discardCount, SupplyTree supplyTree) {
        return new SupplyFailure(discardCount, supplyTree);
    }

    public int getDiscardCount() {
        return discardCount;
    }

    public SupplyTree getSupplyTree() {
        return supplyTree;
    }

    public SupplyFailure modifySupplyTree(Fn1<SupplyTree, SupplyTree> f) {
        return supplyFailure(discardCount, f.apply(supplyTree));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplyFailure that = (SupplyFailure) o;

        if (discardCount != that.discardCount) return false;
        return supplyTree.equals(that.supplyTree);
    }

    @Override
    public int hashCode() {
        int result = discardCount;
        result = 31 * result + supplyTree.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SupplyFailure{" +
                "discardCount=" + discardCount +
                ", supplyTree=" + supplyTree +
                '}';
    }
}
