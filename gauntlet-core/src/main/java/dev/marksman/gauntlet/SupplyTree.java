package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct4;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;


public abstract class SupplyTree implements CoProduct4<SupplyTree.Leaf, SupplyTree.Filter, SupplyTree.FailedFilter, SupplyTree.Composite, SupplyTree> {

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Leaf extends SupplyTree {
        private final String label;

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Filter, ? extends R> bFn, Fn1<? super FailedFilter, ? extends R> cFn, Fn1<? super Composite, ? extends R> dFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Filter extends SupplyTree {
        private final SupplyTree child;

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Filter, ? extends R> bFn, Fn1<? super FailedFilter, ? extends R> cFn, Fn1<? super Composite, ? extends R> dFn) {
            return bFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FailedFilter extends SupplyTree {
        private final int discardedCount;
        private final SupplyTree child;

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Filter, ? extends R> bFn, Fn1<? super FailedFilter, ? extends R> cFn, Fn1<? super Composite, ? extends R> dFn) {
            return cFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Composite extends SupplyTree {
        private final ImmutableNonEmptyFiniteIterable<SupplyTree> children;

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Filter, ? extends R> bFn, Fn1<? super FailedFilter, ? extends R> cFn, Fn1<? super Composite, ? extends R> dFn) {
            return dFn.apply(this);
        }
    }

    public static SupplyTree leaf(String name) {
        return new Leaf(name);
    }

    public static SupplyTree filter(SupplyTree child) {
        return new Filter(child);
    }

    public static SupplyTree failedFilter(int discardedCount, SupplyTree child) {
        return new FailedFilter(discardedCount, child);
    }

    public static SupplyTree composite(SupplyTree first, SupplyTree second, SupplyTree... rest) {
        return new Composite(Vector.of(first, second).concat(Vector.copyFrom(rest)));
    }
}
