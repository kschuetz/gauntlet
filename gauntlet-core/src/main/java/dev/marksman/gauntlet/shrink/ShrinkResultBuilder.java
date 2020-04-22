package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;
import static dev.marksman.gauntlet.shrink.LazyCons.lazyHeadLazyTail;

public final class ShrinkResultBuilder<A> {
    private final ImmutableFiniteIterable<A> result;

    private ShrinkResultBuilder(ImmutableFiniteIterable<A> result) {
        this.result = result;
    }

    public static <A> ShrinkResultBuilder<A> shrinkResultBuilder() {
        return new ShrinkResultBuilder<>(emptyImmutableFiniteIterable());
    }

    public ShrinkResultBuilder<A> prepend(A element) {
        return new ShrinkResultBuilder<>(result.prepend(element));
    }

    public ShrinkResultBuilder<A> append(A element) {
        return new ShrinkResultBuilder<>(result.append(element));
    }

    public ShrinkResultBuilder<A> concat(ImmutableFiniteIterable<A> elements) {
        return new ShrinkResultBuilder<>(result.concat(elements));
    }

    public ShrinkResultBuilder<A> lazyPrepend(Fn0<A> elementSupplier) {
        return new ShrinkResultBuilder<>(lazyHeadLazyTail(elementSupplier, () -> result));
    }

    public ShrinkResultBuilder<A> lazyAppend(Fn0<A> elementSupplier) {
        return new ShrinkResultBuilder<>(result.concat(() -> ImmutableFiniteIterable.of(elementSupplier.apply()).iterator()));
    }

    public ShrinkResultBuilder<A> lazyConcat(Fn0<ImmutableFiniteIterable<A>> elementsSupplier) {
        return new ShrinkResultBuilder<>(result.concat(() -> elementsSupplier.apply().iterator()));
    }

    public ShrinkResultBuilder<A> when(boolean condition, Fn1<ShrinkResultBuilder<A>, ShrinkResultBuilder<A>> modifyFn) {
        if (condition) {
            return modifyFn.apply(this);
        } else {
            return this;
        }
    }

    public ImmutableFiniteIterable<A> build() {
        return result;
    }

}
