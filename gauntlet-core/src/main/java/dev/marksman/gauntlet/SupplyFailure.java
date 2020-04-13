package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SupplyFailure {
    int discardCount;
    SupplyTree tree;

    public static SupplyFailure supplyFailure(int discardCount, SupplyTree tree) {
        return new SupplyFailure(discardCount, tree);
    }
}
