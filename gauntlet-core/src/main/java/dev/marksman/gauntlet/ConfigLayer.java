package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;

public abstract class ConfigLayer<A> implements CoProduct3<ConfigLayer.Inherit<A>, ConfigLayer.Modify<A>, ConfigLayer.Absolute<A>, ConfigLayer<A>> {

    @SuppressWarnings("unchecked")
    public static <A> Inherit<A> inherit() {
        return (Inherit<A>) Inherit.INSTANCE;
    }

    public static <A> Modify<A> modify(Fn1<A, A> transform) {
        return new Modify<>(transform);
    }

    public static <A> Absolute<A> absolute(A value) {
        return new Absolute<>(value);
    }

    public abstract A apply(A input);

    public abstract ConfigLayer<A> add(ConfigLayer<A> other);

    public static class Inherit<A> extends ConfigLayer<A> {
        private static final Inherit<?> INSTANCE = new Inherit<>();

        private Inherit() {
        }

        @Override
        public A apply(A input) {
            return input;
        }

        @Override
        public ConfigLayer<A> add(ConfigLayer<A> other) {
            return other;
        }

        @Override
        public <R> R match(Fn1<? super Inherit<A>, ? extends R> aFn, Fn1<? super Modify<A>, ? extends R> bFn, Fn1<? super Absolute<A>, ? extends R> cFn) {
            return aFn.apply(this);
        }
    }

    public static class Modify<A> extends ConfigLayer<A> {
        private final Fn1<A, A> fn;

        private Modify(Fn1<A, A> fn) {
            this.fn = fn;
        }

        @Override
        public A apply(A input) {
            return fn.apply(input);
        }

        @Override
        public ConfigLayer<A> add(ConfigLayer<A> other) {
            return other.match(__ -> this,
                    m -> modify(fn.fmap(m.getFn())),
                    __ -> other);
        }

        @Override
        public <R> R match(Fn1<? super Inherit<A>, ? extends R> aFn, Fn1<? super Modify<A>, ? extends R> bFn, Fn1<? super Absolute<A>, ? extends R> cFn) {
            return bFn.apply(this);
        }

        public Fn1<A, A> getFn() {
            return fn;
        }
    }

    public static class Absolute<A> extends ConfigLayer<A> {
        private final A value;

        private Absolute(A value) {
            this.value = value;
        }

        @Override
        public A apply(A input) {
            return value;
        }

        @Override
        public ConfigLayer<A> add(ConfigLayer<A> other) {
            return other.match(__ -> this,
                    m -> absolute(m.getFn().apply(value)),
                    __ -> other);
        }

        @Override
        public <R> R match(Fn1<? super Inherit<A>, ? extends R> aFn, Fn1<? super Modify<A>, ? extends R> bFn, Fn1<? super Absolute<A>, ? extends R> cFn) {
            return cFn.apply(this);
        }

        public A getValue() {
            return value;
        }
    }
}
