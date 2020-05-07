package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

// TODO: decide the fate of SupplyTree

public abstract class SupplyTree implements CoProduct3<SupplyTree.Leaf, SupplyTree.Composite, SupplyTree.Collection, SupplyTree> {
    public static SupplyTree leaf(String name) {
        return new Leaf(name);
    }

    public static SupplyTree collection(SupplyTree child) {
        return new Collection(child);
    }

    public static SupplyTree composite(SupplyTree first, SupplyTree second, SupplyTree... rest) {
        return new Composite(Vector.of(first, second).concat(Vector.copyFrom(rest)));
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Leaf extends SupplyTree {
        String label;

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Composite extends SupplyTree {
        ImmutableNonEmptyFiniteIterable<SupplyTree> children;

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn) {
            return bFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Collection extends SupplyTree {
        SupplyTree child;

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn) {
            return cFn.apply(this);
        }
    }
}
