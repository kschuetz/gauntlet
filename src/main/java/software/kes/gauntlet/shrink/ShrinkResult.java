package software.kes.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn0;
import software.kes.collectionviews.Vector;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.enhancediterables.ImmutableNonEmptyFiniteIterable;

import static software.kes.gauntlet.shrink.LazyCons.lazyTail;

public final class ShrinkResult {
    private ShrinkResult() {

    }

    public static <A> ImmutableFiniteIterable<A> empty() {
        return Vector.empty();
    }

    public static <A> ImmutableNonEmptyFiniteIterable<A> singleton(A value) {
        return Vector.of(value);
    }

    public static <A> ImmutableNonEmptyFiniteIterable<A> cons(A head, Fn0<ImmutableFiniteIterable<A>> tailSupplier) {
        return lazyTail(head, tailSupplier);
    }

    @SafeVarargs
    public static <A> ImmutableNonEmptyFiniteIterable<A> of(A firstElement, A... more) {
        return Vector.of(firstElement, more);
    }
}
