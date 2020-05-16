package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;

public abstract class SettingAdjustment<A> implements CoProduct3<SettingAdjustment.Inherit<A>, SettingAdjustment.Modify<A>, SettingAdjustment.Absolute<A>, SettingAdjustment<A>> {

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

    public abstract A apply(Fn0<A> getDefault);

    public abstract SettingAdjustment<A> add(SettingAdjustment<A> other);

    public static class Inherit<A> extends SettingAdjustment<A> {
        private static final Inherit<?> INSTANCE = new Inherit<>();

        private Inherit() {
        }

        @Override
        public A apply(Fn0<A> getDefault) {
            return getDefault.apply();
        }

        @Override
        public SettingAdjustment<A> add(SettingAdjustment<A> other) {
            return other;
        }

        @Override
        public <R> R match(Fn1<? super Inherit<A>, ? extends R> aFn, Fn1<? super Modify<A>, ? extends R> bFn, Fn1<? super Absolute<A>, ? extends R> cFn) {
            return aFn.apply(this);
        }
    }

    public static class Modify<A> extends SettingAdjustment<A> {
        private final Fn1<A, A> fn;

        private Modify(Fn1<A, A> fn) {
            this.fn = fn;
        }

        @Override
        public A apply(Fn0<A> getDefault) {
            return fn.apply(getDefault.apply());
        }

        @Override
        public SettingAdjustment<A> add(SettingAdjustment<A> other) {
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

    public static class Absolute<A> extends SettingAdjustment<A> {
        private final A value;

        private Absolute(A value) {
            this.value = value;
        }

        @Override
        public A apply(Fn0<A> getDefault) {
            return value;
        }

        @Override
        public SettingAdjustment<A> add(SettingAdjustment<A> other) {
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
