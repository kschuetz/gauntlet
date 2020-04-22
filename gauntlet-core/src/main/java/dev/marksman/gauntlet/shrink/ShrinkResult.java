package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.gauntlet.shrink.LazyCons.lazyTail;

public final class ShrinkResult {

    private ShrinkResult() {

    }

    public static <A> ImmutableFiniteIterable<A> empty() {
        return emptyImmutableFiniteIterable();
    }

    public static <A> ImmutableNonEmptyFiniteIterable<A> singleton(A value) {
        return ImmutableNonEmptyFiniteIterable.of(value);
    }

    public static <A> ImmutableNonEmptyFiniteIterable<A> cons(A head, Fn0<ImmutableFiniteIterable<A>> tailSupplier) {
        return lazyTail(head, tailSupplier);
    }

    public static <A> Fn0<ImmutableFiniteIterable<A>> maybeCons(boolean condition, Fn0<A> headSupplier, Fn0<ImmutableFiniteIterable<A>> tailSupplier) {
        if (condition) {
            return () -> cons(headSupplier.apply(), tailSupplier);
        } else {
            return tailSupplier;
        }
    }

    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> other) {
        return xs.concat(() -> other.apply().iterator());
    }

    @SafeVarargs
    public static <A> ImmutableFiniteIterable<A> concat(ImmutableFiniteIterable<A> xs,
                                                        Fn0<ImmutableFiniteIterable<A>> first,
                                                        Fn0<ImmutableFiniteIterable<A>> second,
                                                        Fn0<ImmutableFiniteIterable<A>>... more) {
        ImmutableFiniteIterable<A> result = xs.concat(() -> first.apply().iterator())
                .concat(() -> second.apply().iterator());
        for (Fn0<ImmutableFiniteIterable<A>> f : more) {
            result = result.concat(() -> f.apply().iterator());
        }
        return result;
    }

}
