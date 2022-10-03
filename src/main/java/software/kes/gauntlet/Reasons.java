package software.kes.gauntlet;

import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.collectionviews.NonEmptyVector;
import software.kes.collectionviews.NonEmptyVectorBuilder;
import software.kes.collectionviews.Vector;
import software.kes.enhancediterables.NonEmptyIterable;

public final class Reasons {
    private final ImmutableNonEmptyVector<String> items;

    private Reasons(ImmutableNonEmptyVector<String> items) {
        this.items = items;
    }

    public static Reasons reasons(String primaryReason, String... additional) {
        if (additional.length == 0) {
            return new Reasons(Vector.of(primaryReason));
        } else {
            NonEmptyVectorBuilder<String> builder = NonEmptyVector.builder(primaryReason);
            for (String reason : additional) {
                builder = builder.add(reason);
            }
            return new Reasons(builder.build());
        }
    }

    public static Reasons reasons(NonEmptyIterable<String> reasons) {
        return new Reasons(NonEmptyVector.nonEmptyCopyFrom(reasons));
    }

    public String getPrimary() {
        return items.head();
    }

    public Reasons concat(Reasons other) {
        return reasons(this.items.concat(other.getItems()));
    }

    public ImmutableNonEmptyVector<String> getItems() {
        return this.items;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Reasons)) return false;
        final Reasons other = (Reasons) o;
        final Object this$items = this.getItems();
        final Object other$items = other.getItems();
        return this$items == null ? other$items == null : this$items.equals(other$items);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $items = this.getItems();
        result = result * PRIME + ($items == null ? 43 : $items.hashCode());
        return result;
    }

    public String toString() {
        return "Reasons(items=" + this.getItems() + ")";
    }
}
