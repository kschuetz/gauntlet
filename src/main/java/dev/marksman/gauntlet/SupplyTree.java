package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct6;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.NonEmptyVector;

public abstract class SupplyTree implements CoProduct6<SupplyTree.Leaf, SupplyTree.Composite, SupplyTree.Collection,
        SupplyTree.Mapping, SupplyTree.Filter, SupplyTree.Exhausted, SupplyTree> {
    public static SupplyTree leaf(String name) {
        return new Leaf(name);
    }

    public static SupplyTree composite(SupplyTree first, SupplyTree... rest) {
        return new Composite(NonEmptyVector.of(first, rest));
    }

    public static SupplyTree collection(SupplyTree child) {
        return new Collection(child);
    }

    public static SupplyTree mapping(SupplyTree underlying) {
        return new Mapping(underlying);
    }

    public static SupplyTree filter(SupplyTree underlying) {
        return new Filter(underlying);
    }

    public static SupplyTree exhausted(SupplyTree underlying, int attemptCount) {
        return new Exhausted(underlying, attemptCount);
    }

    public static class Leaf extends SupplyTree {
        private final String label;

        private Leaf(String label) {
            this.label = label;
        }

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn, Fn1<? super Mapping, ? extends R> dFn, Fn1<? super Filter, ? extends R> eFn, Fn1<? super Exhausted, ? extends R> fFn) {
            return aFn.apply(this);
        }

        public String getLabel() {
            return label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Leaf leaf = (Leaf) o;

            return label.equals(leaf.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }

        @Override
        public String toString() {
            return "Leaf{" +
                    "label='" + label + '\'' +
                    '}';
        }
    }

    public static class Composite extends SupplyTree {
        private final ImmutableVector<SupplyTree> children;

        private Composite(ImmutableVector<SupplyTree> children) {
            this.children = children;
        }

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn, Fn1<? super Mapping, ? extends R> dFn, Fn1<? super Filter, ? extends R> eFn, Fn1<? super Exhausted, ? extends R> fFn) {
            return bFn.apply(this);
        }

        public ImmutableVector<SupplyTree> getChildren() {
            return children;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Composite composite = (Composite) o;

            return children.equals(composite.children);
        }

        @Override
        public int hashCode() {
            return children.hashCode();
        }

        @Override
        public String toString() {
            return "Composite{" +
                    "children=" + children +
                    '}';
        }
    }

    public static class Collection extends SupplyTree {
        private final SupplyTree child;

        private Collection(SupplyTree child) {
            this.child = child;
        }

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn, Fn1<? super Mapping, ? extends R> dFn, Fn1<? super Filter, ? extends R> eFn, Fn1<? super Exhausted, ? extends R> fFn) {
            return cFn.apply(this);
        }

        public SupplyTree getChild() {
            return child;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Collection that = (Collection) o;

            return child.equals(that.child);
        }

        @Override
        public int hashCode() {
            return child.hashCode();
        }

        @Override
        public String toString() {
            return "Collection{" +
                    "child=" + child +
                    '}';
        }
    }

    public static class Mapping extends SupplyTree {
        private final SupplyTree underlying;

        private Mapping(SupplyTree underlying) {
            this.underlying = underlying;
        }

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn, Fn1<? super Mapping, ? extends R> dFn, Fn1<? super Filter, ? extends R> eFn, Fn1<? super Exhausted, ? extends R> fFn) {
            return dFn.apply(this);
        }

        public SupplyTree getUnderlying() {
            return underlying;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Mapping mapping = (Mapping) o;

            return underlying.equals(mapping.underlying);
        }

        @Override
        public int hashCode() {
            return underlying.hashCode();
        }

        @Override
        public String toString() {
            return "Mapping{" +
                    "underlying=" + underlying +
                    '}';
        }
    }

    public static class Filter extends SupplyTree {
        private final SupplyTree underlying;

        private Filter(SupplyTree underlying) {
            this.underlying = underlying;
        }

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn, Fn1<? super Mapping, ? extends R> dFn, Fn1<? super Filter, ? extends R> eFn, Fn1<? super Exhausted, ? extends R> fFn) {
            return eFn.apply(this);
        }

        public SupplyTree getUnderlying() {
            return underlying;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter filter = (Filter) o;

            return underlying.equals(filter.underlying);
        }

        @Override
        public int hashCode() {
            return underlying.hashCode();
        }

        @Override
        public String toString() {
            return "Filter{" +
                    "underlying=" + underlying +
                    '}';
        }
    }

    public static class Exhausted extends SupplyTree {
        private final SupplyTree underlying;
        private final int attemptCount;

        private Exhausted(SupplyTree underlying, int attemptCount) {
            this.underlying = underlying;
            this.attemptCount = attemptCount;
        }

        @Override
        public <R> R match(Fn1<? super Leaf, ? extends R> aFn, Fn1<? super Composite, ? extends R> bFn, Fn1<? super Collection, ? extends R> cFn, Fn1<? super Mapping, ? extends R> dFn, Fn1<? super Filter, ? extends R> eFn, Fn1<? super Exhausted, ? extends R> fFn) {
            return fFn.apply(this);
        }

        public SupplyTree getUnderlying() {
            return underlying;
        }

        public int getAttemptCount() {
            return attemptCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Exhausted exhausted = (Exhausted) o;

            if (attemptCount != exhausted.attemptCount) return false;
            return underlying.equals(exhausted.underlying);
        }

        @Override
        public int hashCode() {
            int result = underlying.hashCode();
            result = 31 * result + attemptCount;
            return result;
        }

        @Override
        public String toString() {
            return "Exhausted{" +
                    "underlying=" + underlying +
                    ", attemptCount=" + attemptCount +
                    '}';
        }
    }
}
