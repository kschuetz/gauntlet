package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class SupplyFailure {

    private final SupplyTree tree;

    public static SupplyFailure supplyFailure(SupplyTree tree) {
        return new SupplyFailure(tree);
    }
}
