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
}
