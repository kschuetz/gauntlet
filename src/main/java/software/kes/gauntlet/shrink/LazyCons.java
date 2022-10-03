package software.kes.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn0;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.enhancediterables.ImmutableNonEmptyFiniteIterable;

final class LazyCons {
    private LazyCons() {
    }

    static <A> ImmutableNonEmptyFiniteIterable<A> lazyTail(A head, Fn0<ImmutableFiniteIterable<A>> tailSupplier) {
        return new ImmutableNonEmptyFiniteIterable<A>() {
            @Override
            public ImmutableFiniteIterable<A> tail() {
                return tailSupplier.apply();
            }

            @Override
            public A head() {
                return head;
            }
        };
    }

    static <A> ImmutableNonEmptyFiniteIterable<A> lazyHeadLazyTail(Fn0<A> headSupplier, Fn0<ImmutableFiniteIterable<A>> tailSupplier) {
        return new ImmutableNonEmptyFiniteIterable<A>() {
            @Override
            public ImmutableFiniteIterable<A> tail() {
                return tailSupplier.apply();
            }

            @Override
            public A head() {
                return headSupplier.apply();
            }
        };
    }
}
